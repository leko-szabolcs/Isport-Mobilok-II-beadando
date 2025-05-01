package beadando.isports_app.fragments.register;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import beadando.isports_app.R;
import beadando.isports_app.data.repositories.AuthRepository;
import beadando.isports_app.utils.callbacks.FirebaseAuthCallback;
import beadando.isports_app.utils.validation.InputValidator;
import beadando.isports_app.utils.validation.ValidationException;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {
    private final AuthRepository authRepository;

    private final MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public final LiveData<Integer> errorMessage = _errorMessage;
    private final MutableLiveData<Integer> _successMessage = new MutableLiveData<>();
    public final LiveData<Integer> successMessage = _successMessage;

    @Inject
    public RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void login(String email, String password, String passwordAgain){
        try {
            InputValidator.isRegistrationValid(email, password, passwordAgain);
        } catch (ValidationException e) {
            _errorMessage.postValue(Integer.parseInt(e.getMessage()));
            return;
        }
        authRepository.register(email, password, new FirebaseAuthCallback<>() {
            @Override
            public void onSuccess(@Nullable Void result) {
                _successMessage.postValue(R.string.register_success);
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(R.string.error_auth_registration);
            }
        });
    }
}



