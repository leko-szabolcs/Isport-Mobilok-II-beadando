package beadando.isports_app.fragments.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import beadando.isports_app.R;
import beadando.isports_app.data.repositories.AuthRepository;
import beadando.isports_app.domains.User;
import beadando.isports_app.utils.SessionManager;
import beadando.isports_app.utils.callbacks.FirebaseAuthCallback;
import beadando.isports_app.utils.validation.InputValidator;
import beadando.isports_app.utils.validation.ValidationException;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    @Inject
    SessionManager sessionManager;

    AuthRepository authRepository;

    private final MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public final LiveData<Integer> errorMessage = _errorMessage;
    private final MutableLiveData<Integer> _successMessage = new MutableLiveData<>();
    public final LiveData<Integer> successMessage = _successMessage;

    @Inject
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void login(String email, String password) {
        try{
            InputValidator.isLoginValid(email, password);
        }catch (ValidationException e){
            _errorMessage.postValue(Integer.parseInt(e.getMessage()));
            return;
        }

        authRepository.login(email, password, new FirebaseAuthCallback<>() {
            @Override
            public void onSuccess(@Nullable User user) {
                sessionManager.saveUser(user);
                _successMessage.postValue(R.string.login_success);
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(R.string.error_auth_credential);
            }
        });
    }
}
