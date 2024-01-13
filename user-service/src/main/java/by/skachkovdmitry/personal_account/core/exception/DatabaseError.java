package by.skachkovdmitry.personal_account.core.exception;

public class DatabaseError extends Error{

  public DatabaseError() {
  }

  public DatabaseError(String message) {
    super(message);
  }

  public DatabaseError(String message, String field) {
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
