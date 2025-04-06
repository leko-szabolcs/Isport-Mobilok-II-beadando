package beadando.isports_app.util;

import android.content.Context;
import android.content.SharedPreferences;
import beadando.isports_app.domain.User;
public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    private static final String KEY_USER_UID = "user_uid";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_USERNAME = "user_username";
    private static final String KEY_USER_SEARCH_NAME = "user_search_name";

    // Save user data to SharedPreferences
    public void saveUser(User user) {
        if (user != null) {
            editor.putString(KEY_USER_UID, user.getUid());
            editor.putString(KEY_USER_EMAIL, user.getEmail());
            editor.putString(KEY_USER_USERNAME, user.getUsername());
            editor.putString(KEY_USER_SEARCH_NAME, user.getSearchName());
            editor.apply();
        }
    }

    // Get user data from SharedPreferences
    public User getUser() {
        String uid = prefs.getString(KEY_USER_UID, null);
        String email = prefs.getString(KEY_USER_EMAIL, null);
        String username = prefs.getString(KEY_USER_USERNAME, null);
        String searchName = prefs.getString(KEY_USER_SEARCH_NAME, null);

        // If no UID is stored, return null
        if (uid == null) {
            return null;
        }

        // Create a new User object with the stored data
        // Note: Timestamp objects are set to null as they aren't stored in SharedPreferences
        return new User(uid, email, username, searchName, null, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
