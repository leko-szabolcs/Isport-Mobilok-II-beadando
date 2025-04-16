package beadando.isports_app.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;

import beadando.isports_app.data.repostiory.EventRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(new EventRepository(FirebaseFirestore.getInstance()));
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}