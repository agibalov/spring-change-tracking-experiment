package me.loki2302.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handle(MethodArgumentNotValidException e) {
        Map<String, String> errorsMap = new HashMap<String, String>();
        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrorList) {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errorsMap.put(fieldName, message);
        }

        return errorsMap;
    }
}
