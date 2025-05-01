package beadando.isports_app.fragments.editprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentEditProfileBinding;
import beadando.isports_app.util.SessionManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProfileFragment extends Fragment {
    @Inject
    SessionManager sessionManager;

    private FragmentEditProfileBinding binding;
    private EditProfileViewModel editProfileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        binding.nameEditText.setText(sessionManager.getUser().getFullName());
        binding.ageEditText.setText(String.valueOf(sessionManager.getUser().getAge()));
        binding.descriptionEditText.setText(sessionManager.getUser().getDescription());

        editProfileViewModel.errorMessage.observe(getViewLifecycleOwner(), this::showMessage);
        editProfileViewModel.successMessage.observe(getViewLifecycleOwner(), this::handleSuccess);

        return binding.getRoot();
    }

    private void handleSuccess(Integer integer) {
        showMessage(integer);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_editProfileFragment_to_profileFragment);
    }

    private void showMessage(Integer resId) {
        Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.saveButton.setOnClickListener(this::handleSaveButton);
    }

    private void handleSaveButton(View view) {
        String name = binding.nameEditText.getText().toString();
        String age = binding.ageEditText.getText().toString();
        String description = binding.descriptionEditText.getText().toString();
        UpdateProfile(name, age, description);
    }

    private void UpdateProfile(String name, String age, String description) {
        editProfileViewModel.updateProfile(name, age, description);
    }
}