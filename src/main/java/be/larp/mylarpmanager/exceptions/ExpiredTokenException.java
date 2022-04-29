package be.larp.mylarpmanager.exceptions;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String s) {
        super(s);
    }

    public ExpiredTokenException() {
        super();
    }
}
