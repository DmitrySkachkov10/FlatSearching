package by.dmitryskachkov.dto;
import java.util.Objects;

public class ErrorResponseDto {

    private final String logref = "error";

    private String message;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(String message) {

        this.message = message;
    }

    public String getLogref() {
        return logref;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponseDto that)) return false;
        return Objects.equals(logref, that.logref) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logref, message);
    }
}