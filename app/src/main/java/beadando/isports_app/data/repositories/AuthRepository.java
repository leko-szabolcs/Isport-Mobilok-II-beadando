package beadando.isports_app.data.repositories;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import beadando.isports_app.domains.User;
import beadando.isports_app.utils.callbacks.FirebaseAuthCallback;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    @Inject
    public AuthRepository(FirebaseFirestore firestore, FirebaseAuth auth) {
        this.auth = auth;
        this.firestore = firestore;
    }

    public void login(String email, String password, FirebaseAuthCallback<User> callback) {
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

    public void register(String email, String password, FirebaseAuthCallback<Void> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    String username = email.substring(0, email.indexOf("@"));
                    String searchName = username.toLowerCase();
                    Timestamp timestamp = Timestamp.now();

                    User newUser = new User(uid, email, username,"","",0, searchName, timestamp, timestamp);

                    firestore.collection("users").document(uid).set(newUser)
                            .addOnSuccessListener(callback::onSuccess)
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getUserData(String uid, FirebaseAuthCallback<User> callback) {
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

    public void logout(FirebaseAuthCallback<User> callback) {
        auth.signOut();
        callback.onSuccess(null);
    }

    public void isLoggedIn(FirebaseAuthCallback<User> callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            getUserData(uid, callback);
        } else {
            callback.onFailure(new Exception("no_logged_in_user_found"));
        }
    }
}
