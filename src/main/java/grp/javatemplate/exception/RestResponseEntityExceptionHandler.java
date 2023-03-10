package grp.javatemplate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleThrowableException( Throwable ex ) {
        log.warn(ex.toString());
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException( RuntimeException ex ) {
        log.warn(ex.toString());
        return ResponseEntity.badRequest().body(List.of(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleConflict( BusinessException ex ) {
        log.warn(ex.toString());
        return ResponseEntity.badRequest().body(List.of(ex.getMessage()));
    }
}