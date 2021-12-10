package ee.tlu.forum.exception;

// https://www.youtube.com/watch?v=PzK4ZXa2Tbc

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

// Tells spring that this class handles multiple exceptions
@ControllerAdvice
public class ApiExceptionHandler {

    // Tells that this method is responsible for handling eg. BadRequestException
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(e.getMessage(),badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(e.getMessage(),status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(e.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, status);
    }
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleRegexValidation(ConstraintViolationException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String error = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        ApiException apiException = new ApiException(error,badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }
    @ExceptionHandler(value = {NoPermissionException.class})
    public ResponseEntity<Object> handleNoPermissionException(NoPermissionException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(e.getMessage(),status, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, status);
    }
}
