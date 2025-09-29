package tech.kood.kmdb.exception;

// Bonus: Ensures creating genre duplicates will not be allowed
public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message) {
        super(message);
    }
}
