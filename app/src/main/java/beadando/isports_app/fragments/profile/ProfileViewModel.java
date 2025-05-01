package beadando.isports_app.fragments.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import beadando.isports_app.data.repositories.AuthRepository;
import beadando.isports_app.utils.SessionManager;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    @Inject
    SessionManager sessionManager;
    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> _onLogout = new MutableLiveData<>();
    public final LiveData<Boolean> onLogout = _onLogout;

    @Inject
    ProfileViewModel (AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void logout() {
        authRepository.logout(new AuthRepository.LogoutCallback() {
            @Override
            public void onLogout() {
                sessionManager.clearSession();
                _onLogout.postValue(true);
            }
        });
    }
}
