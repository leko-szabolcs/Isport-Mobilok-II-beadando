package beadando.isports_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DecimalFormat;

import javax.inject.Inject;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentProfileBinding;
import beadando.isports_app.domain.User;
import beadando.isports_app.util.SessionManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    @Inject
    SessionManager sessionManager;

    private FragmentProfileBinding binding;
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        currentUser = sessionManager.getUser();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fullNameTextView.setText(currentUser.getFullName());
        binding.usernameTextView.setText(currentUser.getUsername());
        binding.emailTextView.setText(currentUser.getEmail());
        binding.ageTextView.setText(formatAgeToDisplay(currentUser.getAge()));
        binding.descriptionTextView.setText(currentUser.getDescription());

        binding.logoutButton.setOnClickListener(this::handleLogoutButton);
        binding.editButton.setOnClickListener(this::handleEditButton);
    }

    private void handleLogoutButton(View view) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).logout();
        }
    }

    private void handleEditButton(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_profileFragment_to_editProfileFragment);
    }

    private String formatAgeToDisplay(int age){
        DecimalFormat DFormat = new DecimalFormat("###");
        return DFormat.format(age);
    }
}