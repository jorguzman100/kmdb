package tech.kood.kmdb.exception;

// Indicates that a entity was not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
