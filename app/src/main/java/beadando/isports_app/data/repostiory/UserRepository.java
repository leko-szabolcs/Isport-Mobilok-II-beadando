package beadando.isports_app.data.repostiory;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.domain.Event;
import beadando.isports_app.domain.User;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;

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

    public void getEventParticipantsByParticipantsList(List<String> participantIds, FirebaseResultCallbacks<List<User>, Void> callback) {
        List<User> participants = new ArrayList<>();
        List<List<String>> batches = new ArrayList<>();

        for (int i = 0; i < participantIds.size(); i += 10) {
            batches.add(participantIds.subList(i, Math.min(i + 10, participantIds.size())));
        }

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (List<String> batch : batches) {
            Task<QuerySnapshot> task = firestore.collection("users")
                    .whereIn("uid", batch)
                    .get();
            tasks.add(task);
        }

        Tasks.whenAllSuccess(tasks)
                .addOnSuccessListener(results -> {
                    for (Object result : results) {
                        QuerySnapshot querySnapshot = (QuerySnapshot) result;
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            User user = doc.toObject(User.class);
                            if (user != null) {
                                participants.add(user);
                            }
                        }
                    }
                    callback.onSuccess(participants, null);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
