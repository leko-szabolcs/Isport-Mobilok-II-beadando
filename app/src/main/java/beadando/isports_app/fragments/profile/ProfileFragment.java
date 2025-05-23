package beadando.isports_app.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentProfileBinding;
import beadando.isports_app.domains.User;
import beadando.isports_app.utils.SessionManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    @Inject
    SessionManager sessionManager;

    ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
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
        binding.ageTextView.setText(String.valueOf(currentUser.getAge()));
        binding.descriptionTextView.setText(currentUser.getDescription());

        binding.logoutButton.setOnClickListener(this::handleLogoutButton);
        binding.editButton.setOnClickListener(this::handleEditButton);

        profileViewModel.onLogout.observe(getViewLifecycleOwner(), this::logoutNavigation);
    }


    private void handleLogoutButton(View view) {
        profileViewModel.logout();
    }

    private void handleEditButton(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_profileFragment_to_editProfileFragment);
    }

    private void logoutNavigation(Boolean isLoggedOut){
        if (isLoggedOut) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        }
    }
}