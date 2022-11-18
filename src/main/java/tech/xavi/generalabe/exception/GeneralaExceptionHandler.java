package tech.xavi.generalabe.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GeneralaExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = GeneralaException.class)
    public ResponseEntity<Map> playerServiceExceptionHandler(GeneralaException e){

        Map<String, String> error = new HashMap<>();
        error.put("code", e.getError().getCode());
        error.put("message", e.getError().getDescription());
        error.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

}
