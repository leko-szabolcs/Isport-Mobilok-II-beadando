package beadando.isports_app.fragments;

import android.widget.EditText;

import beadando.isports_app.util.validation.InputValidator;

/**
 * Ez az "adapter" osztály segít az űrlap mezők kezelésében.
 * - Validálja az adatokat
 * - Lekéri őket
 */
public class RegisterAdapter {

    private final EditText etEmail;
    private final EditText etPassword;
    private final EditText etConfirmPassword;

    public RegisterAdapter(EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
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

}
