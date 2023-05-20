package com.backend.softue.utils.response;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@NoArgsConstructor
@Component
public class ErrorFactory {

    public String errorGenerator(BindingResult bindingResult){
        String errorMessages = "";
        for (FieldError error : bindingResult.getFieldErrors()) {
            String errorMessage = error.getDefaultMessage();
            errorMessages+=errorMessage+",";
        }
        return errorMessages;
    }
}
