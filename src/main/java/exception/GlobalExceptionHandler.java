package exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public String handleUserNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage());
        return "User not found";
    }

    public String handleEmptyDataException(EmptyDataException e) {
        log.warn(e.getMessage());
        return "Data cannot be empty";
    }

    public String handleNotCorrespondingTypeException(NotCorrespondingTypeException e) {
        log.error("Not corresponding type error: ", e);
        return "Invalid data format";
    }

    public String handleOutOfRangeException(OutOfRangeException e) {
        log.error("Out of range error: ", e);
        return "Value is out of allowed range";
    }

    public String handleDatabaseException(DatabaseException e) {
        log.error("Database error: ", e);
        return "Database error. Please try again later.";
    }

    public String handleGenericException(Exception e) {
        log.error("Unexpected error: ", e);
        return "Unexpected error occurred. Please try again later.";
    }
}
