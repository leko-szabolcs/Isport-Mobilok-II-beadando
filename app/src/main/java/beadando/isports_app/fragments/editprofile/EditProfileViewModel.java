package beadando.isports_app.fragments.editprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import beadando.isports_app.R;
import beadando.isports_app.data.repostiory.ProfileRepository;
import beadando.isports_app.util.callbacks.FirebaseResultCallbacks;
import beadando.isports_app.util.mappers.ErrorMapper;
import beadando.isports_app.util.validation.ProfileValidator;
import beadando.isports_app.util.validation.ValidationException;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditProfileViewModel extends ViewModel {
    private final ProfileRepository profileRepository;

    private final MutableLiveData<Integer> _successMessage = new MutableLiveData<>();
    public final LiveData<Integer> successMessage = _successMessage;
    private final MutableLiveData<Integer> _errorMessage = new MutableLiveData<>();
    public final LiveData<Integer> errorMessage = _errorMessage;

    @Inject
    public EditProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void updateProfile(String fullName, String age, String description ) {
        try {
            ProfileValidator.isValidProfile(fullName, age, description);
            _successMessage.postValue(R.string.success_save);
        } catch (ValidationException e) {
            _errorMessage.postValue(Integer.parseInt(e.getMessage()));
            return;
        }
        updateFullName(fullName);
        updateAge(age);
        updateDescription(description);
    }

    public void updateFullName(String fullName) {
        profileRepository.updateFullName(fullName, new FirebaseResultCallbacks<>() {

            @Override
            public void onSuccess(String result, Void unused) {
                _successMessage.postValue(R.string.success_save);
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));
            }
        });
    }

    public void updateDescription(String description) {
        profileRepository.updateDescription(description, new FirebaseResultCallbacks<>() {

            @Override
            public void onSuccess(String result, Void unused) {
                _successMessage.postValue(R.string.success_save);
            }

            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));
            }
        });
    }

    public void updateAge(String age) {
        int ageInt = Integer.parseInt(age);
        profileRepository.updateAge(ageInt, new FirebaseResultCallbacks<>() {
            @Override
            public void onSuccess(String result, Void unused) {
                _successMessage.postValue(R.string.success_save);
            }
            @Override
            public void onFailure(Exception e) {
                _errorMessage.postValue(ErrorMapper.mapExceptionToMessage(e));
            }
        });
    }
}
