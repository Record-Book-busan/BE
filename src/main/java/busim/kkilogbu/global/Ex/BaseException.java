package busim.kkilogbu.global.Ex;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private HttpStatus status;
    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
