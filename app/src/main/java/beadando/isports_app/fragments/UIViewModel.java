package beadando.isports_app.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UIViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _loadingOverlay = new MutableLiveData<>(false);
    public final LiveData<Boolean> loadingOverlay = _loadingOverlay;

    public void showLoadingOverlay() { _loadingOverlay.setValue(true); }
    public void hideLoadingOverlay() { _loadingOverlay.setValue(false); }
}