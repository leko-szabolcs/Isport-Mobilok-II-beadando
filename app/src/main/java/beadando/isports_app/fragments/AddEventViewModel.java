package beadando.isports_app.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import beadando.isports_app.data.repostiory.EventRepository;
import beadando.isports_app.domain.Event;

public class AddEventViewModel extends ViewModel {
    private final EventRepository repository;

    private final MutableLiveData<Boolean> _saveSuccess = new MutableLiveData<>();
    public final LiveData<Boolean> saveSuccess = _saveSuccess;
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    public AddEventViewModel() {
        repository = new EventRepository(FirebaseFirestore.getInstance());
    }

    public void saveEvent(Event event) {
        _isLoading.setValue(true);
        repository.saveEvent(event, new EventRepository.SaveCallback() {
            @Override
            public void onSuccess() {
                _isLoading.setValue(false);
                _saveSuccess.setValue(true);
            }
            @Override
            public void onFailure(Exception e) {
                _isLoading.setValue(false);
                _errorMessage.setValue(e.getMessage());
            }
        });
    }

    public void resetState() {
        _saveSuccess.setValue(null);
        _errorMessage.setValue(null);
    }
}