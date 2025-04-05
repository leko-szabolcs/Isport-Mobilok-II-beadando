package beadando.isports_app.util;

import android.content.Context;
import android.util.Patterns;

public class InputValidator {

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 250;
    public static final int EMAIL_MAX_LENGTH = 50;

    public static boolean isValidEmail(Context context, String email) {
        if (email.isEmpty()) {
            toast(context, "Az email mező nem lehet üres");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast(context, "Nem megfelelő email formátum");
            return false;
        }

        if (email.length() > EMAIL_MAX_LENGTH) {
            toast(context, "Túl hosszú email cím");
            return false;
        }

        return true;
    }

    public static boolean isValidPassword(Context context, String password) {
        if (password.isEmpty()) {
            toast(context, "A jelszó nem lehet üres");
            return false;
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            toast(context, "A jelszónak legalább " + PASSWORD_MIN_LENGTH + " karakter hosszúnak kell lennie");
            return false;
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            toast(context, "A jelszó túl hosszú");
            return false;
        }

        return true;
    }

    public static boolean doPasswordsMatch(Context context, String pw1, String pw2) {
        if (!pw1.equals(pw2)) {
            toast(context, "A jelszavak nem egyeznek");
            return false;
        }
        return true;
    }

    public static boolean isRegistrationValid(Context context, String email, String password, String confirmPassword) {
        return isValidEmail(context, email)
                && isValidPassword(context, password)
                && doPasswordsMatch(context, password, confirmPassword);
    }

    private static void toast(Context context, String msg) {
        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}