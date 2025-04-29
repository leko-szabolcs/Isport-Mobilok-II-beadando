package beadando.isports_app.fragments.event;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.data.repostiory.EventRepository;
import beadando.isports_app.data.repostiory.UserRepository;
import beadando.isports_app.domain.User;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.util.mappers.ErrorMapper;
import beadando.isports_app.util.mappers.SuccessMapper;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public final LiveData<Integer> errorMessage = _errorMessage;
    private final MutableLiveData<Integer> _successMessage = new MutableLiveData<>();
    public final LiveData<Integer> successMessage = _successMessage;

    private final MutableLiveData<User> _organizer = new MutableLiveData<>();
    public final LiveData<User> organizer = _organizer;
    private final MutableLiveData<List<User>> _participants = new MutableLiveData<>();
    public final LiveData<List<User>> participants = _participants;

    @Inject
    public EventViewModel(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
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

    public void applyForEvent(String eventId) {
        eventRepository.applyForEvent(eventId, new FirebaseResultCallbacks<>() {
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
