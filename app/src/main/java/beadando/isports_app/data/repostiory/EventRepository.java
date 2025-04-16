package beadando.isports_app.data.repostiory;

import com.google.firebase.firestore.FirebaseFirestore;

import beadando.isports_app.domain.Event;

public class EventRepository {
    private FirebaseFirestore firestore;

    public interface SaveCallback{
        void onSuccess();
        void onFailure(Exception e);
    }

    public EventRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void saveEvent(Event event, SaveCallback callback) {
        firestore.collection("events").document().set(event)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(callback::onFailure);
    }
}
