package ee.tlu.forum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiException {
    private final String error_message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
