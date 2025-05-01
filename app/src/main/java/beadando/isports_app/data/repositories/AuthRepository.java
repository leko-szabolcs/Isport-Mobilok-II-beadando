package beadando.isports_app.data.repositories;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import beadando.isports_app.domains.User;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    @Inject
    public AuthRepository(FirebaseFirestore firestore) {
        auth = FirebaseAuth.getInstance();
        this.firestore = firestore;
    }

    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface LogoutCallback {
        void onLogout();
    }

    public void login(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        String uid = user.getUid();
                        getUserData(uid, callback);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void register(String email, String password, RegisterCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    String username = email.substring(0, email.indexOf("@"));
                    String searchName = username.toLowerCase();
                    Timestamp timestamp = Timestamp.now();

                    User newUser = new User(uid, email, username,"","",0, searchName, timestamp, timestamp);

                    firestore.collection("users").document(uid).set(newUser)
                            .addOnSuccessListener(aVoid -> callback.onSuccess())
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getUserData(String uid, AuthCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User userModel = documentSnapshot.toObject(User.class);
                        callback.onSuccess(userModel);
                    } else {
                        callback.onFailure(new Exception("Felhasználói adat nem található."));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void logout(LogoutCallback callback) {
        auth.signOut();
        if (callback != null) {
            callback.onLogout();
        }
    }

    public void isLoggedIn(AuthCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            getUserData(uid, callback);
        } else {
            callback.onFailure(new Exception("no_logged_in_user_found"));
        }
    }
}
