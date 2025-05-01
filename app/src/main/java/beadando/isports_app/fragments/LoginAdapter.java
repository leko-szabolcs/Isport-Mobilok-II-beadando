package beadando.isports_app.fragments;

import android.widget.EditText;

import beadando.isports_app.utils.validation.InputValidator;

public class LoginAdapter {

    private final EditText etEmail;
    private final EditText etPassword;

    public LoginAdapter(EditText etEmail, EditText etPassword) {
        this.etEmail = etEmail;
        this.etPassword = etPassword;
    }

    public String getEmail() {
        return etEmail.getText().toString().trim();
    }

    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    public boolean isValidLogin(android.content.Context context) {
        return InputValidator.isLoginValid(context, getEmail(), getPassword());
    }

    private void toast(android.content.Context context, String msg) {
        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}
