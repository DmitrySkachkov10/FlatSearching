package by.dmitryskachkov.flatservice.controller;


import by.dmitryskachkov.dto.ErrorDto;
import by.dmitryskachkov.dto.ErrorResponseDto;
import by.dmitryskachkov.dto.StructuredErrorDto;
import by.dmitryskachkov.entity.*;
import by.dmitryskachkov.entity.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(StructuredError.class)
    public ResponseEntity<StructuredErrorDto> defaultErrorHandler(StructuredError e) {

        StructuredErrorDto structuredErrorDto = new StructuredErrorDto();
        List<ErrorDto> errorDtos = e.getErrors().getErrorList().stream()
                .map(er -> new ErrorDto(((Error) er).getMessage(), ((Error) er).getField()))
                .toList();
        structuredErrorDto.setErrors(errorDtos);
        return new ResponseEntity<>(structuredErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationError.class)
    public ResponseEntity<ErrorResponseDto> defaultErrorHandler(ValidationError e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VerificationError.class)
    public ResponseEntity<ErrorResponseDto> defaultErrorHandler(VerificationError e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(DataBaseError.class)
    public ResponseEntity<ErrorResponseDto> defaultErrorHandler(DataBaseError e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
