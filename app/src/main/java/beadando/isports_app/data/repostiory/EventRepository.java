package beadando.isports_app.data.repostiory;



import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import beadando.isports_app.domain.Event;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;

public class EventRepository {
    private final FirebaseFirestore firestore;

    public EventRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void saveEvent(Event event, FirebaseResultCallbacks<String, Void> callback) {
        DocumentReference docRef = firestore.collection("events").document();
        event.setId(docRef.getId());

        docRef.set(event)
                .addOnSuccessListener(unused -> callback.onSuccess("Success", null))
                .addOnFailureListener(callback::onFailure);
    }

    public void getEventById(String id, FirebaseResultCallbacks<Event, Void> callback) {
        firestore.collection("events").document().get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        if (event != null) {
                            event.setId(documentSnapshot.getId());
                            callback.onSuccess(event, null);
                        }
                    } else {
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
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    List<Event> events = new ArrayList<>();

                    for (DocumentSnapshot doc : docs) {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            event.setId(doc.getId());
                            events.add(event);
                        }
                    }

                    DocumentSnapshot last = docs.isEmpty() ? null : docs.get(docs.size() - 1);
                    callback.onSuccess(events, last);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getEventTypes(FirebaseResultCallbacks<List<String>, Void> callback) {
        firestore.collection("sports").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> types = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String name = doc.getString("name"); // vagy: Object name = doc.get("name");
                        if (name != null) {
                            types.add(name);
                        }
                    }
                    callback.onSuccess(types, null);
                })
                .addOnFailureListener(callback::onFailure);

    }
}
