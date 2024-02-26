package mate.academy.springbootintro.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        List<String> mappedErrors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                mappedErrors);
        return new ResponseEntity<>(errorResponse, headers, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (!(objectError instanceof FieldError)) {
            return objectError.getDefaultMessage();
        }
        String field = ((FieldError) objectError).getField();
        String defaultMessage = objectError.getDefaultMessage();
        return field + " " + defaultMessage;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now(),
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Entity Not Found",
                List.of(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }
}
