package io.project.fastwork.services.exception;

public class TokenRefreshException extends Exception{
    public TokenRefreshException() {
        super();
    }

    public TokenRefreshException(String message) {
        super(message);
    }
}
