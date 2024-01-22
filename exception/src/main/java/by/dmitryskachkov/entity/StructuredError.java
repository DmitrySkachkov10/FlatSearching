package by.dmitryskachkov.entity;

public class StructuredError extends Error {

    private Errors errors;

    public StructuredError(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}