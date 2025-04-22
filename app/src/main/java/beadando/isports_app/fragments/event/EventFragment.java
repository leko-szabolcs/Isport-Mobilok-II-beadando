package beadando.isports_app.fragments.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentEventBinding;
import beadando.isports_app.domain.Event;
import beadando.isports_app.util.DateUtils;

public class EventFragment extends Fragment {
    private FragmentEventBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);

        Event event = null;
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
        }

        if(event != null) {
            //binding..setText(event.getTitle());

            binding.tvTypeValue.setText(event.getType());
            binding.tvOrganizerValue.setText("n/a");
            binding.tvLocationValue.setText(event.getLocation());

            binding.tvParticipantsValue.setText(event.getParticipantsList().size() + "/" + event.getParticipants());
            binding.tvFeeValue.setText(event.getFee() ? "fizetős" : "ingyenes" );
            binding.tvDateValue.setText(DateUtils.formatFirebaseTimestampForDisplay(event.getDate()));

            binding.tvDescriptionValue.setText(event.getDescription());
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnJoin.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_eventFragment_to_mainFragment);
        });
    }
}