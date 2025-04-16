package beadando.isports_app.data.repostiory;



import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import beadando.isports_app.domain.Event;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;

public class EventRepository {
    private FirebaseFirestore firestore;

    public EventRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void saveEvent(Event event, FirebaseResultCallbacks<String, Void> callback) {
        firestore.collection("events").document().set(event)
                .addOnSuccessListener(unused -> callback.onSuccess("Success", null))
                .addOnFailureListener(callback::onFailure);
    }

    public void getEventById(String id, FirebaseResultCallbacks<Event, Void> callback) {
        firestore.collection("events").document().get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                    }else {
                        callback.onFailure(new Exception("error_event_not_found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void getLatesEvents(int limit, @Nullable DocumentSnapshot from,
                               FirebaseResultCallbacks<List<Event>, DocumentSnapshot>  callback){
        Query query =  firestore.collection("events")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit);

        if (from != null) query = query.startAfter(from);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Event> events = queryDocumentSnapshots.toObjects(Event.class);
                    DocumentSnapshot last = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    callback.onSuccess(events,last);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
