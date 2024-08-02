package busim.kkilogbu.global.Ex;

import org.springframework.http.HttpStatus;

public class ToiletNotFoundException extends BaseException{
    public ToiletNotFoundException() {
        super("지정된 반경 내에 화장실을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
