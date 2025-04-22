package beadando.isports_app.fragments.event;

import androidx.lifecycle.ViewModel;

import beadando.isports_app.data.repostiory.UserRepository;

public class EventViewModel extends ViewModel {
    private final UserRepository userRepository;

    public EventViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




}
