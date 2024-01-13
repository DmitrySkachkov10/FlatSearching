package by.skachkovdmitry.personal_account.core.exception;

public class StructuredError extends RuntimeException {

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
