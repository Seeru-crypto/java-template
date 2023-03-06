package grp.javatemplate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request ) {
        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(StringUtils::hasLength)
                .toList());
    }
}