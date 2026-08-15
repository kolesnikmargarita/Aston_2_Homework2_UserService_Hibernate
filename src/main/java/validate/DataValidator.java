package validate;


public class DataValidator {

    public static void checkNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }

    public static void checkMinLength(String value, int min, String fieldName) {
        if (value.length() < min) {
            throw new IllegalArgumentException(fieldName + " must be at least " + min + " characters");
        }
    }

    public static void checkMaxLength(String value, int max, String fieldName) {
        if (value.length() > max) {
            throw new IllegalArgumentException(fieldName + " must be less than " + max + " characters");
        }
    }

    public static void checkPattern(String value, String regex, String message) {
        if (!value.matches(regex)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public static void checkRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
        }
    }

    public static void checkMinValue(long value, long min, String fieldName) {
        if (value < min) {
            throw new IllegalArgumentException(fieldName + " must be greater than " + min);
        }
    }

    public static void validateName(String name) {
        checkNotBlank(name, "Name");
        checkMinLength(name, 2, "Name");
        checkMaxLength(name, 50, "Name");
        checkPattern(name, "[a-zA-Zа-яА-ЯёЁ\\s-]+", "Only letters, spaces, and hyphens");
    }

    public static void validateEmail(String email) {
        checkNotBlank(email, "Email");
        checkMaxLength(email, 254, "Email");
        checkPattern(email, "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", "Invalid email format");
    }

    public static void validateAge(Integer age) {
        checkNotNull(age, "Age");
        checkRange(age, 0, 150, "Age");
    }

    public static void validateId(Long id) {
        checkNotNull(id, "ID");
        checkMinValue(id, 1, "ID");
    }
}
