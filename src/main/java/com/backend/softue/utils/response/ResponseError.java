package com.backend.softue.utils.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseError{
    private  String errorType;
    private  String errorMessage;
    private  String cause;

    public ResponseError(Exception e) {
        this.errorType = e.getClass().toString();
        this.errorMessage = e.getMessage();
        this.cause = (e.getCause() == null) ? "NULL" : e.getCause().toString();
    }
}
