package beadando.isports_app.fragments.addevent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import beadando.isports_app.data.repositories.EventRepository;
import beadando.isports_app.domains.Event;
import beadando.isports_app.utils.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.utils.mappers.ErrorMapper;
import beadando.isports_app.utils.mappers.SuccessMapper;
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
        repository.insertEvent(event, new FirebaseResultCallbacks<>() {

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