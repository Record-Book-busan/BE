package busim.kkilogbu.global.Ex;

import org.springframework.http.HttpStatus;

public class InvalidCategoryException extends BaseException {
    public InvalidCategoryException(String message) {
        super(message, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }
}
