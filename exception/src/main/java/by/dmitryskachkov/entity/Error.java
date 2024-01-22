package by.dmitryskachkov.entity;

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

    @java.lang.Override
    public java.lang.String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}