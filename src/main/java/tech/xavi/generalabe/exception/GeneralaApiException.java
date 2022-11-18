package tech.xavi.generalabe.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GeneralaApiException {

    private String code;
    private LocalDate dateTime;
    private String message;

}
