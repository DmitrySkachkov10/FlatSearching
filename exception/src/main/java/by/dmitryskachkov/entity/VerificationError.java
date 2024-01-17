package by.dmitryskachkov.entity;




public class VerificationError extends Error{

    public VerificationError(String message) {
        super(message);
    }

    public VerificationError(String message, String field) {
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
