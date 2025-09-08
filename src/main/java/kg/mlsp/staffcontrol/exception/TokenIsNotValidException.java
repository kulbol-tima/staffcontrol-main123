package kg.mlsp.staffcontrol.exception;

public class TokenIsNotValidException extends RuntimeException {
    public TokenIsNotValidException(String message) {
        super(message);
    }
    public TokenIsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}