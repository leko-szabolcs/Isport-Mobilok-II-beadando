package beadando.isports_app.fragments.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentEventBinding;
import beadando.isports_app.domain.Event;
import beadando.isports_app.domain.User;
import beadando.isports_app.util.DateUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventFragment extends Fragment {
    private FragmentEventBinding binding;
    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        eventAdapter = new EventAdapter(new ArrayList<>());
        binding.rvParticipants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvParticipants.setAdapter(eventAdapter);

        Event event = null;
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
        }

        if(event != null) {
            eventViewModel.getEventOrganizerUser(event.getCreatedBy());
            eventViewModel.getEventParticipants(event.getParticipantsList());

            binding.tvTypeValue.setText(event.getType());
            binding.tvOrganizerValue.setText("n/a");
            binding.tvLocationValue.setText(event.getLocation());
            binding.tvParticipantsValue.setText(event.getParticipantsList().size() + "/" + event.getParticipants());
            binding.tvFeeValue.setText(event.getFee() ? R.string.event_fee : R.string.event_free );
            binding.tvDateValue.setText(DateUtils.formatFirebaseTimestampForDisplay(event.getDate()));
            binding.tvDescriptionValue.setText(event.getDescription());

            final Event finalEvent = event;
            binding.btnJoin.setOnClickListener(v -> applyForEvent(finalEvent));
        }
        eventViewModel.organizer.observe(getViewLifecycleOwner(), this::loadEventOwner);
        eventViewModel.participants.observe(getViewLifecycleOwner(), this::loadEventParticipants);
        eventViewModel.errorMessage.observe(getViewLifecycleOwner(), this::showMessage);
        eventViewModel.successMessage.observe(getViewLifecycleOwner(), this::showMessage);

        return binding.getRoot();
    }

    private void showMessage(int resId) {
        Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show();
    }

    private void applyForEvent(Event event) {
        if (event != null && event.getId() != null) {
            eventViewModel.applyForEvent(event.getId());
        }
    }

    private void loadEventParticipants(List<User> users) {
        if (users != null){
            eventAdapter.addParticipants(users);
        }
    }

    private void loadEventOwner(User user) {
        if (user != null){
            binding.tvOrganizerValue.setText(user.getUsername());
        }else{
            binding.tvOrganizerValue.setText("ismeretlen");
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}