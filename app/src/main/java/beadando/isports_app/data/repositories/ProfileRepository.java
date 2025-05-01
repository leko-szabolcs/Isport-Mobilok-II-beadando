package beadando.isports_app.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import beadando.isports_app.utils.SessionManager;
import beadando.isports_app.utils.callbacks.FirebaseResultCallbacks;

public class ProfileRepository {
    private final FirebaseFirestore firestore;
    private final SessionManager sessionManager;

    @Inject
    public ProfileRepository(FirebaseFirestore firestore, SessionManager sessionManager) {
        this.firestore = firestore;
        this.sessionManager = sessionManager;
    }

    public void updateFullName(String name, FirebaseResultCallbacks<String, Void>  callback) {
        firestore.collection("users").document(sessionManager.getUser().getUid())
                .update("fullName", name)
                .addOnSuccessListener(unused -> callback.onSuccess("successful_update_name", null))
                .addOnFailureListener(callback::onFailure);
    }

    public void updateDescription(String description, FirebaseResultCallbacks<String, Void>  callback) {
        firestore.collection("users").document(sessionManager.getUser().getUid())
                .update("description", description)
                .addOnSuccessListener(unused -> callback.onSuccess("successful_update_description", null))
                .addOnFailureListener(callback::onFailure);
    }

    public void updateAge(int age, FirebaseResultCallbacks<String, Void>  callback) {
        firestore.collection("users").document(sessionManager.getUser().getUid())
                .update("age", age)
                .addOnSuccessListener(unused -> callback.onSuccess("successful_update_age", null))
                .addOnFailureListener(callback::onFailure);
    }

}
