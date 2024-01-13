package org.example.exception.dto.structured;

import java.util.List;
import java.util.Objects;

public class StructuredErrorDto {

    private List<ErrorDto> errorDtos;

    public StructuredErrorDto() {
    }

    public StructuredErrorDto(List<org.example.exception.dto.structured.ErrorDto> errorDtos) {
        this.errorDtos = errorDtos;
    }

    public void addToErrors(ErrorDto errorDto) {
        errorDtos.add(errorDto);
    }

    public org.example.exception.dto.structured.ErrorDto getById(int id) {
        return errorDtos.get(id);
    }

    public List<org.example.exception.dto.structured.ErrorDto> getErrors() {
        return errorDtos;
    }

    public void setErrors(List<org.example.exception.dto.structured.ErrorDto> errorDtos) {
        this.errorDtos = errorDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructuredErrorDto that)) return false;
        return Objects.equals(errorDtos, that.errorDtos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorDtos);
    }
}
