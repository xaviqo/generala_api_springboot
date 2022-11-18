package tech.xavi.generalabe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class GeneralaException extends RuntimeException{

    private final GeneralaError error;
    private final HttpStatus httpStatus;

    public GeneralaException(GeneralaError error, HttpStatus httpStatus){
        super();
        this.error = error;
        this.httpStatus = httpStatus;
    }
}
