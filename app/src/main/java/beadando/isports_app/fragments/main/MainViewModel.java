package beadando.isports_app.fragments.main;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.utils.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.data.repositories.EventRepository;
import beadando.isports_app.domains.Event;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final EventRepository eventRepository;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    private DocumentSnapshot lastVisibleSnapshot = null;
    private String selectedSportType = null;
    private boolean isLastPage = false;

    @Inject
    MainViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public LiveData<List<Event>> getEvents(){
        return eventsLiveData;
    }

    public void loadEvents() {
        if (isLastPage || Boolean.TRUE.equals(_isLoading.getValue())) return;
        _isLoading.setValue(true);
        eventRepository.getLatestEvents(20,
                lastVisibleSnapshot,
                selectedSportType,
                new FirebaseResultCallbacks<>()
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

    public void setSportType(String sportType) {
        this.selectedSportType = sportType;
        refreshEvents();
    }

    public void refreshEvents() {
        lastVisibleSnapshot = null;
        isLastPage = false;
        eventsLiveData.setValue(new ArrayList<>());
        loadEvents();
    }
}
