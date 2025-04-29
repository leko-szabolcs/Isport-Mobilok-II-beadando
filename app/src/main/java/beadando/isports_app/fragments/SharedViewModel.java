package beadando.isports_app.fragments;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beadando.isports_app.data.repostiory.EventRepository;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SharedViewModel extends ViewModel {
    private final EventRepository repository;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<List<String>> _sportTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<String>> sportTypes = _sportTypes;

    @Inject
    public SharedViewModel(EventRepository repository) {
        this.repository = repository;
    }

    public void loadSportTypes(){
        if (sportTypes.getValue() != null && !sportTypes.getValue().isEmpty()) {
            return;
        }

        repository.getEventTypes(new FirebaseResultCallbacks<>() {
            @Override
            public void onSuccess(List<String> result, @Nullable Void extra) {
                _sportTypes.setValue(result);
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.setValue(e.getMessage());
            }
        });
    }
}
