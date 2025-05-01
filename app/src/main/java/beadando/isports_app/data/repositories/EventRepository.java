package beadando.isports_app.data.repositories;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.domains.Event;
import beadando.isports_app.domains.User;
import beadando.isports_app.utils.SessionManager;
import beadando.isports_app.utils.callbacks.FirebaseResultCallbacks;

public class EventRepository {
    private final FirebaseFirestore firestore;
    private final SessionManager sessionManager;

    @Inject
    public EventRepository(FirebaseFirestore firestore, SessionManager sessionManager) {
        this.firestore = firestore;
        this.sessionManager = sessionManager;
    }

    public void insertEvent(Event event, FirebaseResultCallbacks<String, Void> callback) {
        DocumentReference docRef = firestore.collection("events").document();
        event.setId(docRef.getId());

        docRef.set(event)
                .addOnSuccessListener(unused -> callback.onSuccess("create_event_success", null))
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

    public void getLatestEvents(int limit,
                                @Nullable DocumentSnapshot from,
                                @Nullable String sportType,
                                FirebaseResultCallbacks<List<Event>, DocumentSnapshot>  callback){
        Query query =  firestore.collection("events")
                .orderBy("date", Query.Direction.ASCENDING)
                .limit(limit);

        if (from == null) {
            Timestamp now = Timestamp.now();
            query = query.whereGreaterThanOrEqualTo("date", now);
        } else {
            query = query.startAfter(from);
        }


        if (sportType != null && !sportType.isEmpty())
            query = query.whereEqualTo("type", sportType);


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
                        String name = doc.getString("name");
                        if (name != null) {
                            types.add(name);
                        }
                    }
                    callback.onSuccess(types, null);
                })
                .addOnFailureListener(callback::onFailure);

    }

    public void applyForEvent(String eventId, FirebaseResultCallbacks<String, Void> callback) {
        checkEventApplyRequirements(eventId, new FirebaseResultCallbacks<Boolean, Void>() {
            @Override
            public void onSuccess(Boolean result, Void unused) {
                firestore.collection("events").document(eventId)
                        .update("participantsList", FieldValue.arrayUnion(sessionManager.getUser().getUid()))
                        .addOnSuccessListener(unused2 -> callback.onSuccess("successful_apply", null))
                        .addOnFailureListener(callback::onFailure);
            }
            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void checkEventApplyRequirements(String eventId, FirebaseResultCallbacks<Boolean, Void> callback) {
        Query query = firestore.collection("events").whereEqualTo("id", eventId);
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        callback.onFailure(new Exception());
                        return;
                    }
                    DocumentSnapshot docs = queryDocumentSnapshots.getDocuments().get(0);
                    Event event = docs.toObject(Event.class);
                    if (event != null) {
                        if (event.getParticipantsList().contains(sessionManager.getUser().getUid())) {
                            callback.onFailure(new Exception("error_already_applied"));
                            return;
                        }
                        if (event.getParticipantsList().size() >= event.getParticipants()) {
                            callback.onFailure(new Exception("error_event_full"));
                            return;
                        }
                        callback.onSuccess(true, null);
                    } else {
                        callback.onFailure(new Exception("error_event_not_found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getParticipantsByEventId(String eventId, FirebaseResultCallbacks<List<User>, Void> callback) {
        firestore.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(eventSnapshot -> {
                    if (eventSnapshot.exists()) {
                        List<String> participantIds = (List<String>) eventSnapshot.get("participantsList");
                        if (participantIds == null || participantIds.isEmpty()) {
                            callback.onSuccess(new ArrayList<>(), null);
                            return;
                        }

                        getEventParticipantsByParticipantsList(participantIds, callback);
                    } else {
                        callback.onFailure(new Exception("error_event_not_found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    private void getEventParticipantsByParticipantsList(List<String> participantIds, FirebaseResultCallbacks<List<User>, Void> callback) {
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
