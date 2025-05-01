package beadando.isports_app.fragments.event;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.R;
import beadando.isports_app.data.repositories.EventRepository;
import beadando.isports_app.data.repositories.UserRepository;
import beadando.isports_app.domains.Event;
import beadando.isports_app.domains.User;
import beadando.isports_app.utils.SessionManager;
import beadando.isports_app.utils.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.utils.mappers.ErrorMapper;
import beadando.isports_app.utils.mappers.SuccessMapper;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public final LiveData<Integer> errorMessage = _errorMessage;
    private final MutableLiveData<Integer> _successMessage = new MutableLiveData<>();
    public final LiveData<Integer> successMessage = _successMessage;

    private final MutableLiveData<User> _organizer = new MutableLiveData<>();
    public final LiveData<User> organizer = _organizer;
    private final MutableLiveData<List<User>> _participants = new MutableLiveData<>();
    public final LiveData<List<User>> participants = _participants;

    @Inject
    public EventViewModel(
            UserRepository userRepository,
            EventRepository eventRepository,
            SessionManager sessionManager
    ) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.sessionManager = sessionManager;
    }

    public void getEventOrganizerUser(String organizerId){
        userRepository.getUserById(organizerId, new FirebaseResultCallbacks<>() {
            @Override
            public void onSuccess(User result, @Nullable Void extra) {
                _organizer.setValue(result);
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));
            }
        });
    }

    public void getEventParticipantsByEventId(String eventId) {
        eventRepository.getParticipantsByEventId(eventId, new FirebaseResultCallbacks<>() {
            @Override
            public void onSuccess(List<User> result, @Nullable Void extra) {
                _participants.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));
            }
        });
    }

    public void applyForEvent(Event event) {
        if(event.getParticipantsList().contains(sessionManager.getUser().getUid())){
            _successMessage.postValue(R.string.error_already_applied);
            return;
        }
        if(event.getCreatedBy().equals(sessionManager.getUser().getUid())){
            _errorMessage.postValue(R.string.error_event_creator_apply);
            return;
        }

        eventRepository.applyForEvent(event.getId(), new FirebaseResultCallbacks<>() {
            @Override
            public void onSuccess(String result, @Nullable Void extra) {
                _successMessage.postValue(SuccessMapper.mapSuccessCodeToMessage(result));
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));
            }
        });
    }
}
