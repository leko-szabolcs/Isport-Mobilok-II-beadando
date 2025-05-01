package beadando.isports_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import beadando.isports_app.data.repositories.AuthRepository;
import beadando.isports_app.domains.User;
import beadando.isports_app.utils.SessionManager;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainAppViewModel extends ViewModel {
    @Inject
    SessionManager sessionManager;

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> _loading  = new MutableLiveData<>();
    public final LiveData<Boolean> loading = _loading;

    private final MutableLiveData<Boolean> _isLoggedIn = new MutableLiveData<>();
    public final LiveData<Boolean> isLoggedIn = _isLoggedIn;

    @Inject
    public MainAppViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void checkLoginStatus() {
        _loading.postValue(true);
        authRepository.isLoggedIn(new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                sessionManager.saveUser(user);
                sessionManager.setLoggedIn(true);
                _isLoggedIn.postValue(true);
                _loading.postValue(false);
            }

            @Override
            public void onFailure(Exception e) {
                sessionManager.setLoggedIn(false);
                _isLoggedIn.postValue(false);
                _loading.postValue(false);
            }
        });
    }
}
