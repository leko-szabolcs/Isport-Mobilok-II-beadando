package beadando.isports_app.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import beadando.isports_app.domains.User;
import beadando.isports_app.utils.callbacks.FirebaseResultCallbacks;

public class UserRepository {
    private final FirebaseFirestore firestore;

    @Inject
    public UserRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void getUserById(String id, FirebaseResultCallbacks<User, Void> callback) {
        firestore.collection("users").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        user.setUid(documentSnapshot.getId());
                        callback.onSuccess(user, null);
                    } else {
                        callback.onFailure(new Exception("error_user_not_found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
}
