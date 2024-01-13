package by.skachkovdmitry.personal_account.core.exception;

public abstract class Error extends RuntimeException{
    private String message;

    private String field;

    public Error() {
    }

    public Error(String message) {
        this.message = message;
    }

    public Error(String message, String field) {
        this.message = message;
        this.field = field;
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
}
