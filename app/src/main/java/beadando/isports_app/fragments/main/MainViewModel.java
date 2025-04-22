package beadando.isports_app.fragments.main;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.data.repostiory.EventRepository;
import beadando.isports_app.domain.Event;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final EventRepository repository;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    private DocumentSnapshot lastVisibleSnapshot = null;
    private boolean isLastPage = false;

    private final MutableLiveData<List<String>> _sportTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<String>> sportTypes = _sportTypes;

    MainViewModel(EventRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Event>> getEvents(){
        return eventsLiveData;
    }

    public void loadEvents() {
        if (isLastPage || Boolean.TRUE.equals(_isLoading.getValue())) return;
        _isLoading.setValue(true);
        repository.getLatesEvents(20, lastVisibleSnapshot, new FirebaseResultCallbacks<>()
        {
            @Override
            public void onSuccess(List<Event> result, @Nullable DocumentSnapshot lastVisible) {
                if (result.size() < 20) isLastPage = true;

                List<Event> currentEvents = eventsLiveData.getValue();
                if (currentEvents != null) {
                    currentEvents.addAll(result);
                    eventsLiveData.setValue(currentEvents);
                }

                lastVisibleSnapshot = lastVisible;
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Exception e) {
                _errorMessage.setValue(e.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    public void refreshEvents() {
        lastVisibleSnapshot = null;
        isLastPage = false;
        eventsLiveData.setValue(new ArrayList<>());
        loadEvents();
    }
}
