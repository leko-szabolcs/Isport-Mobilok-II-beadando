package beadando.isports_app.fragments;

import android.util.Patterns;
import android.widget.EditText;

import beadando.isports_app.util.InputValidator;

/**
 * Ez az "adapter" osztály segít az űrlap mezők kezelésében.
 * - Validálja az adatokat
 * - Lekéri őket
 */
public class AuthAdapter {

    private final EditText etEmail;
    private final EditText etPassword;
    private final EditText etConfirmPassword;

    public AuthAdapter(EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
        this.etEmail = etEmail;
        this.etPassword = etPassword;
        this.etConfirmPassword = etConfirmPassword;
    }

    public String getEmail() {
        return etEmail.getText().toString().trim();
    }

    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    public String getConfirmPassword() {
        return etConfirmPassword != null ? etConfirmPassword.getText().toString().trim() : null;
    }

    public boolean isValidRegistration(android.content.Context context) {
        return InputValidator.isRegistrationValid(context, getEmail(), getPassword(), getConfirmPassword());
    }

    public boolean isValidLogin(android.content.Context context) {
        if (getEmail().isEmpty() || getPassword().isEmpty()) {
            toast(context, "Töltsd ki az emailt és jelszót");
            return false;
        }
        return true;
    }

    private void toast(android.content.Context context, String msg) {
        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}
