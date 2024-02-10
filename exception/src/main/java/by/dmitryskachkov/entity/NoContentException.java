package by.dmitryskachkov.entity;

public class NoContentException extends Error{
    public NoContentException(String message) {
        super(message);
    }

    public NoContentException(String message, String field) {
        super(message, field);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getField() {
        return super.getField();
    }
}

