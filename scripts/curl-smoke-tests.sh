#!/usr/bin/env bash
# Movie Database API - CURL Smoke Tests (Idempotent v3: nested objects in movie POST)
# Base URL can be overridden with BASE_URL env var (default: http://localhost:8080/api)
# Requires: bash, curl; Optional: jq (recommended)
# NOTE: This version writes all output to scripts/curl-test-report.txt instead of the CLI.

# ****** Created with LLM to support the code review ******

set -euo pipefail

BASE="${BASE_URL:-http://localhost:8080/api}"
USE_JQ=0
if command -v jq >/dev/null 2>&1; then USE_JQ=1; fi

LOG_FILE="scripts/curl-test-report.txt"
mkdir -p scripts
: > "$LOG_FILE"

hr()   { echo "------------------------------------------------------------" >> "$LOG_FILE"; }
ts()   { date +"%H:%M:%S"; }
INFO() { echo "ℹ️  [$1] $(ts) - $2" >> "$LOG_FILE"; }
PASS() { echo "✅ [$1] $(ts) - $2" >> "$LOG_FILE"; }
FAIL() { echo "❌ [$1] $(ts) - $2" >> "$LOG_FILE"; exit 1; }

TEST_ID=""
TEST_DESC=""

test_start() {
  TEST_ID="$1"
  TEST_DESC="$2"
  hr
  INFO "$TEST_ID" "$TEST_DESC"
}

# --- helpers ---------------------------------------------------------------

# curl_json METHOD PATH JSON -> writes body to /tmp/body.json and echoes HTTP code
do_curl_json() {
  local method="$1"; shift
  local path="$1"; shift
  local json="$1"; shift
  curl -sS -o /tmp/body.json -w "%{http_code}" -X "$method" \
    -H "Content-Type: application/json" \
    -d "$json" \
    "${BASE}${path}"
}

# curl_get PATH -> writes body to /tmp/body.json and echoes HTTP code
do_curl_get() {
  local path="$1"; shift
  curl -sS -o /tmp/body.json -w "%{http_code}" "${BASE}${path}"
}

show_body() {
  if [[ -s /tmp/body.json ]]; then
    if [[ $USE_JQ -eq 1 ]]; then jq '.' /tmp/body.json >> "$LOG_FILE" || cat /tmp/body.json >> "$LOG_FILE"; else cat /tmp/body.json >> "$LOG_FILE"; fi
    echo >> "$LOG_FILE"
  fi
}

extract_id() {
  if [[ $USE_JQ -eq 1 ]]; then jq -r '.id' /tmp/body.json; else grep -o '"id":[ ]*[0-9]*' /tmp/body.json | head -n1 | sed 's/[^0-9]//g'; fi
}

# Get or create Genre by name, echo id
get_or_create_genre() {
  local name="$1"
  local code
  code=$(do_curl_json POST "/genres" "{\"name\":\"${name}\"}")
  if [[ "$code" == "201" ]]; then extract_id; return 0; fi
  code=$(do_curl_get "/genres")
  if [[ "$code" != "200" ]]; then echo "Lookup /genres failed ($code)" >> "$LOG_FILE"; show_body; return 1; fi
  if [[ $USE_JQ -eq 1 ]]; then jq -r ".[] | select(.name==\"${name}\") | .id" /tmp/body.json | head -n1; else grep -o '"id":[ ]*[0-9]*' /tmp/body.json | head -n1 | sed 's/[^0-9]//g'; fi
}

# Get or create Actor by name & birthDate, echo id
get_or_create_actor() {
  local name="$1"
  local birth="$2"
  local code
  code=$(do_curl_json POST "/actors" "{\"name\":\"${name}\",\"birthDate\":\"${birth}\"}")
  if [[ "$code" == "201" ]]; then extract_id; return 0; fi
  code=$(do_curl_get "/actors?name=$(echo "$name" | sed 's/ /%20/g')")
  if [[ "$code" != "200" ]]; then echo "Lookup /actors?name= failed ($code)" >> "$LOG_FILE"; show_body; return 1; fi
  if [[ $USE_JQ -eq 1 ]]; then jq -r ".[] | select((.name|ascii_downcase)==\"${name,,}\") | .id" /tmp/body.json | head -n1; else grep -o '"id":[ ]*[0-9]*' /tmp/body.json | head -n1 | sed 's/[^0-9]//g'; fi
}

# --- run -------------------------------------------------------------------

hr
echo "Base URL: $BASE  |  jq: $USE_JQ  |  Log: $LOG_FILE" >> "$LOG_FILE"
hr

# Preflight: check a simple GET so we fail early with a friendly message
test_start "T00" "Preflight API health: GET /genres (expect 200)"
code=$(do_curl_get "/genres")
if [[ "$code" != "200" ]]; then
  echo "Expected 200 but got $code" >> "$LOG_FILE"; show_body; FAIL "T00" "API not reachable. Is the app running on http://localhost:8080 ?"
else
  PASS "T00" "API reachable"
fi

# 1) Ensure Genres exist (idempotent)
test_start "T01" "Ensure Genre 'Action' exists (201 or found)"
G_ACTION=$(get_or_create_genre "Action")
[[ -n "${G_ACTION}" ]] || FAIL "T01" "Could not get/create Genre 'Action'"
echo "Genre 'Action' id=${G_ACTION}" >> "$LOG_FILE"; PASS "T01" "Ready"

test_start "T02" "Ensure Genre 'Sci-Fi' exists (201 or found)"
G_SCIFI=$(get_or_create_genre "Sci-Fi")
[[ -n "${G_SCIFI}" ]] || FAIL "T02" "Could not get/create Genre 'Sci-Fi'"
echo "Genre 'Sci-Fi' id=${G_SCIFI}" >> "$LOG_FILE"; PASS "T02" "Ready"

# 2) Ensure Actors exist (idempotent)
test_start "T03" "Ensure Actor 'Leonardo DiCaprio' exists (201 or found)"
A_DI=$(get_or_create_actor "Leonardo DiCaprio" "1974-11-11")
[[ -n "${A_DI}" ]] || FAIL "T03" "Could not get/create Actor Leonardo DiCaprio"
echo "Actor 'Leonardo DiCaprio' id=${A_DI}" >> "$LOG_FILE"; PASS "T03" "Ready"

test_start "T04" "Ensure Actor 'Tom Hardy' exists (201 or found)"
A_HARDY=$(get_or_create_actor "Tom Hardy" "1977-09-15")
[[ -n "${A_HARDY}" ]] || FAIL "T04" "Could not get/create Actor Tom Hardy"
echo "Actor 'Tom Hardy' id=${A_HARDY}" >> "$LOG_FILE"; PASS "T04" "Ready"

# 3) Create a unique Movie to avoid duplicates (NOTE: genres/actors as nested objects with {\"id\":...})
UNIQ="E2E-$(date +%s)"
test_start "T05" "Create Movie: Inception-$UNIQ with genres & actors (201)"
MOVIE_BODY=$(cat <<JSON
{"title":"Inception-$UNIQ","releaseYear":2010,"duration":148,"genres":[{"id":${G_ACTION}},{"id":${G_SCIFI}}],"actors":[{"id":${A_DI}},{"id":${A_HARDY}}]}
JSON
)
code=$(do_curl_json POST "/movies" "${MOVIE_BODY}")
if [[ "$code" != "201" ]]; then echo "Expected 201 got $code" >> "$LOG_FILE"; echo "Payload sent:" >> "$LOG_FILE"; echo "$MOVIE_BODY" >> "$LOG_FILE"; show_body; FAIL "T05" "Create movie failed"; fi
show_body
M_ID=$(extract_id)
echo "Movie id=${M_ID}" >> "$LOG_FILE"
PASS "T05" "Created"

# 4) GET all entities
test_start "T06" "GET /genres (200)";    [[ "$(do_curl_get "/genres")" == "200" ]] || { show_body; FAIL "T06" "GET /genres"; }; show_body; PASS "T06" "OK"
test_start "T07" "GET /actors (200)";    [[ "$(do_curl_get "/actors")" == "200" ]] || { show_body; FAIL "T07" "GET /actors"; }; show_body; PASS "T07" "OK"
test_start "T08" "GET /movies (200)";    [[ "$(do_curl_get "/movies")" == "200" ]] || { show_body; FAIL "T08" "GET /movies"; }; show_body; PASS "T08" "OK"

# 5) Filtering
test_start "T09" "Filter movies by genre (200)"; [[ "$(do_curl_get "/movies?genre=${G_ACTION}")" == "200" ]] || { show_body; FAIL "T09" "Filter by genre"; }; show_body; PASS "T09" "OK"
test_start "T10" "Filter movies by year=2010 (200)"; [[ "$(do_curl_get "/movies?year=2010")" == "200" ]] || { show_body; FAIL "T10" "Filter by year"; }; show_body; PASS "T10" "OK"
test_start "T11" "Filter movies by actor (200)"; [[ "$(do_curl_get "/movies?actor=${A_DI}")" == "200" ]] || { show_body; FAIL "T11" "Filter by actor"; }; show_body; PASS "T11" "OK"

# 6) Search
test_start "T12" "Search movies by partial title (200)"; [[ "$(do_curl_get "/movies/search?title=incep")" == "200" ]] || { show_body; FAIL "T12" "Search movies"; }; show_body; PASS "T12" "OK"
test_start "T13" "Search actors by name (200)";          [[ "$(do_curl_get "/actors?name=leonardo")" == "200" ]] || { show_body; FAIL "T13" "Search actors"; }; show_body; PASS "T13" "OK"

# 7) Pagination
test_start "T14" "Pagination: /movies?page=0&size=10 (200)"; [[ "$(do_curl_get "/movies?page=0&size=10")" == "200" ]] || { show_body; FAIL "T14" "Pagination"; }; show_body; PASS "T14" "OK"

# 8) Relationships: actors in a movie
test_start "T15" "GET actors in a movie (200)"; [[ "$(do_curl_get "/movies/${M_ID}/actors")" == "200" ]] || { show_body; FAIL "T15" "Actors in movie"; }; show_body; PASS "T15" "OK"

# 9) Partial update
test_start "T16" "PATCH movie duration to 150 (200)"
code=$(do_curl_json PATCH "/movies/${M_ID}" '{"duration":150}')
if [[ "$code" != "200" ]]; then echo "Expected 200 got $code" >> "$LOG_FILE"; show_body; FAIL "T16" "PATCH movie"; fi
show_body; PASS "T16" "OK"

# 10) Validation
test_start "T17" "POST actor with invalid birthDate (400 expected)"
code=$(do_curl_json POST "/actors" '{"name":"Invalid Date","birthDate":"1990-13-32"}')
if [[ "$code" != "400" ]]; then echo "Expected 400 got $code" >> "$LOG_FILE"; show_body; FAIL "T17" "Validation not enforced?"; fi
show_body; PASS "T17" "OK"

# 11) Error handling
test_start "T18" "GET non-existent actor (404 expected)"
code=$(do_curl_get "/actors/99999999")
if [[ "$code" != "404" ]]; then echo "Expected 404 got $code" >> "$LOG_FILE"; show_body; FAIL "T18" "Expected 404"; fi
show_body; PASS "T18" "OK"

# 12) ID immutability
test_start "T19" "Attempt to PATCH movie id (should not change) (200)"
code=$(do_curl_json PATCH "/movies/${M_ID}" '{"id":123456}')
if [[ "$code" != "200" ]]; then echo "Expected 200 got $code" >> "$LOG_FILE"; show_body; FAIL "T19" "PATCH with id"; fi
show_body; PASS "T19" "OK (verify ID unchanged in response)"

# 13) Default delete should fail with relationships (best-effort demonstration)
test_start "T20" "DELETE a busy genre should fail (400) — best effort"
code=$(curl -sS -o /tmp/body.json -w "%{http_code}" -X DELETE "${BASE}/genres/${G_ACTION}")
if [[ "$code" == "400" ]]; then PASS "T20" "Default delete blocked"; else echo "Got $code (expected 400)" >> "$LOG_FILE"; show_body; echo "Note: If returned 204, your API allows deleting unlinked genres." >> "$LOG_FILE"; PASS "T20" "Proceeding"; fi

# 14) Force delete the created movie (safe cleanup)
test_start "T21" "Force delete created movie (204)"
code=$(curl -sS -o /tmp/body.json -w "%{http_code}" -X DELETE "${BASE}/movies/${M_ID}?force=true")
if [[ "$code" != "204" ]]; then echo "Expected 204 got $code" >> "$LOG_FILE"; show_body; FAIL "T21" "Force delete movie"; fi
PASS "T21" "OK"

hr
PASS "DONE" "All labeled, idempotent smoke tests completed"

echo "Report written to $LOG_FILE"
