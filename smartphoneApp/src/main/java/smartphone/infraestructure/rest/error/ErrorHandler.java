package smartphone.infraestructure.rest.error;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import smartphone.infraestructure.rest.config.properties.ExceptionsProperties;

@AllArgsConstructor
@ControllerAdvice
public class ErrorHandler {

    ExceptionsProperties exceptionsProperties;

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> notFoundExceptionHandler(Exception exception, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionsProperties.getNot_found()+exception.getMessage());
    }

    @ExceptionHandler({TimeoutException.class})
    public ResponseEntity<Object> timeoutExceptionHandler(Exception exception, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionsProperties.getTimeout());
    }

    @ExceptionHandler({ConnectionException.class})
    public ResponseEntity<Object> connectionExceptionHandler(Exception exception, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionsProperties.getConnection());
    }

    @ExceptionHandler({UnknownException.class})
    public ResponseEntity<Object> unknownExceptionHandler(Exception exception, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionsProperties.getRuntime());
    }

}
