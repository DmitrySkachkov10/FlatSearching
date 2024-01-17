package by.dmitryskachkov.entity;

public class DataBaseError extends Error{

    public DataBaseError() {
    }

    public DataBaseError(String message) {
        super(message);
    }

    public DataBaseError(String message, String field) {
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