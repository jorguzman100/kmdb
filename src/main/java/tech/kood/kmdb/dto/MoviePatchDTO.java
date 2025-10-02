package tech.kood.kmdb.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

// Allows partial updates without sending the entire movie
public class MoviePatchDTO {
    // Only validate if present (null = not provided)
    @Size(max = 200, message = "title must be at most 200 characters")
    private String title;

    @Min(value = 1888, message = "releaseYear must be >= 1888")
    @Max(value = 2100, message = "releaseYear must be <= 2100")
    private Integer releaseYear;

    @Min(value = 1, message = "duration must be >= 1")
    @Max(value = 600, message = "duration must be <= 600")
    private Integer duration;

    public MoviePatchDTO() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
}
