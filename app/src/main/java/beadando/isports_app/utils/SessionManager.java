package beadando.isports_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.Timestamp;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import beadando.isports_app.domains.User;

@Singleton
public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    @Inject
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
    private static final String KEY_USER_FULL_NAME = "user_full_name";
    private static final String KEY_USER_DESCRIPTION = "user_description";
    private static final String KEY_USER_AGE = "user_age";
    private static final String KEY_USER_SEARCH_NAME = "user_search_name";
    private static final String KEY_USER_CREATED_AT = "user_created_at";
    private static final String KEY_USER_LAST_ONLINE = "user_last_online";

    public void saveUser(User user) {
        if (user != null) {
            editor.putString(KEY_USER_UID, user.getUid());
            editor.putString(KEY_USER_EMAIL, user.getEmail());
            editor.putString(KEY_USER_USERNAME, user.getUsername());
            editor.putString(KEY_USER_SEARCH_NAME, user.getSearchName());
            editor.putString(KEY_USER_FULL_NAME, user.getFullName());
            editor.putString(KEY_USER_DESCRIPTION, user.getDescription());
            editor.putInt(KEY_USER_AGE, user.getAge());

            if (user.getCreatedAt() != null) {
                editor.putString(KEY_USER_CREATED_AT, user.getCreatedAt().toDate().toString());
            }
            if (user.getLastOnline() != null) {
                editor.putString(KEY_USER_LAST_ONLINE, user.getLastOnline().toDate().toString());
            }

            editor.apply();
        }
    }

    public User getUser() {
        String uid = prefs.getString(KEY_USER_UID, null);
        String email = prefs.getString(KEY_USER_EMAIL, null);
        String username = prefs.getString(KEY_USER_USERNAME, null);
        String searchName = prefs.getString(KEY_USER_SEARCH_NAME, null);

        String fullName = prefs.getString(KEY_USER_FULL_NAME, null);
        String description = prefs.getString(KEY_USER_DESCRIPTION, null);
        int age = prefs.getInt(KEY_USER_AGE, 0);

        String createdAtString = prefs.getString(KEY_USER_CREATED_AT, null);
        Timestamp createdAt = null;
        if (createdAtString != null) {
            Date date = new Date(createdAtString);
            createdAt = new Timestamp(date);
        }

        String lastOnlineString = prefs.getString(KEY_USER_LAST_ONLINE, null);
        Timestamp lastOnline = null;
        if (lastOnlineString != null) {
            Date date = new Date(lastOnlineString);
            lastOnline = new Timestamp(date);
        }

        if (uid == null) {
            return null;
        }
        return new User(uid, email, username,fullName,description,age, searchName, createdAt, lastOnline);
    }

    public String updateFullName(String fullName) {
        editor.putString(KEY_USER_FULL_NAME, fullName);
        editor.apply();
        return fullName;
    }

    public String updateDescription(String description) {
        editor.putString(KEY_USER_DESCRIPTION, description);
        editor.apply();
        return description;
    }

    public int updateAge(int age) {
        editor.putInt(KEY_USER_AGE, age);
        editor.apply();
        return age;
    }


    public String getUserUid() {
        return prefs.getString(KEY_USER_UID, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
