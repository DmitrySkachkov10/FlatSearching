package by.skachkovdmitry.audit.controller;

import by.dmitryskachkov.dto.ErrorResponseDto;
import by.dmitryskachkov.entity.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalHandler {
    @ExceptionHandler(ValidationError.class)
    public ResponseEntity<ErrorResponseDto> defaultErrorHandler(ValidationError e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
