package by.dmitryskachkov.entity;


public class ValidationError extends Error{
    public ValidationError(String message) {
        super(message);
    }

    public ValidationError(String message, String field) {
        super(message, field);
    }

    @Override
    public String getField() {
        return super.getField();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}