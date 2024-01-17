package by.dmitryskachkov.dto;
import java.util.Objects;

public class ErrorDto {

    private String message;

    private String field;

    public ErrorDto() {
    }

    public ErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorDto errorDto)) return false;
        return Objects.equals(message, errorDto.message) && Objects.equals(field, errorDto.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, field);
    }
}