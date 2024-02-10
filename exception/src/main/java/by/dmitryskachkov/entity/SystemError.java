package by.dmitryskachkov.entity;

public class SystemError extends Error {

    public SystemError() {}

    public SystemError(String message) {
        super(message);
    }

    public SystemError(String message, String field) {
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
