package by.dmitryskachkov.entity;

import org.springframework.http.HttpStatusCode;

public class TokenError extends Error {

    private HttpStatusCode httpStatusCode;

    public TokenError(String message) {
        super(message);
    }

    public TokenError(String message, String field) {
        super(message, field);
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
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
