package beadando.isports_app.fragments.addevent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.data.repostiory.EventRepository;
import beadando.isports_app.domain.Event;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.util.mappers.ErrorMapper;
import beadando.isports_app.util.mappers.SuccessMapper;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddEventViewModel extends ViewModel {
    private final EventRepository repository;

    private final MutableLiveData<Integer> _saveSuccess = new MutableLiveData<>();
    public final LiveData<Integer> saveSuccess = _saveSuccess;
    private final MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public final LiveData<Integer> errorMessage = _errorMessage;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    @Inject
    public AddEventViewModel(EventRepository repository) {
        this.repository = repository;
    }

    public void saveEvent(Event event) {
        _isLoading.setValue(true);
        repository.createEvent(event, new FirebaseResultCallbacks<>() {

            @Override
            public void onSuccess(String result, @Nullable Void extra) {
                _isLoading.setValue(false);
                _saveSuccess.postValue(SuccessMapper.mapSuccessCodeToMessage(result));
            }

            @Override
            public void onFailure(Exception e) {
                _isLoading.setValue(false);
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));;
            }

        });
    }
}