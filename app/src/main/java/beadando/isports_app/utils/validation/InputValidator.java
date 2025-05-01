package beadando.isports_app.utils.validation;

import android.util.Patterns;

import beadando.isports_app.R;

public class InputValidator {

    public static void isValidEmail(String email) throws ValidationException {
        if (email.isEmpty() || email.trim().isEmpty()) {
            throw new ValidationException(Integer.toString(R.string.error_email_required));
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new ValidationException(Integer.toString(R.string.error_email_invalid));
        }

        if (email.length() > ValidationConstants.AUTH_EMAIL_MAX_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_email_too_long));
        }

        if (email.length() < ValidationConstants.AUTH_EMAIL_MIN_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_email_too_short));
        }
    }

    public static void isValidPassword(String password) throws ValidationException {
        if (password.isEmpty() || password.trim().isEmpty()) {
            throw new ValidationException(Integer.toString(R.string.error_password_required));
        }

        if (password.length() < ValidationConstants.AUTH_PASSWORD_MIN_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_password_too_short));
        }

        if (password.length() > ValidationConstants.AUTH_PASSWORD_MAX_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_password_too_long));
        }
    }

    public static void doPasswordsMatch(String password, String passwordAgain) throws ValidationException {
        if (!password.equals(passwordAgain)) {
            throw new ValidationException(Integer.toString(R.string.error_password_not_match));
        }
    }

    public static void isRegistrationValid( String email, String password, String passwordAgain) throws ValidationException {
        isValidEmail(email);
        isValidPassword(password);
        doPasswordsMatch(password, passwordAgain);
    }

    public static void isLoginValid(String email, String password) throws ValidationException {
        isValidEmail(email);
        isValidPassword(password);
    }
}