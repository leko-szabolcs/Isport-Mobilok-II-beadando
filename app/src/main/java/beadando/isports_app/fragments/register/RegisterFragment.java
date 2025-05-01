package beadando.isports_app.fragments.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import beadando.isports_app.R;
import beadando.isports_app.databinding.FragmentRegisterBinding;
import beadando.isports_app.fragments.UIViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment {
    private RegisterViewModel registerViewModel;
    private UIViewModel uiViewModel;
    private FragmentRegisterBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        uiViewModel = new ViewModelProvider(requireActivity()).get(UIViewModel.class);

        binding.btnRegister.setOnClickListener(this::handleRegisterButtonClick);

        registerViewModel.errorMessage.observe(getViewLifecycleOwner(),this::handleErrorRegister);
        registerViewModel.successMessage.observe(getViewLifecycleOwner(), this::handleSuccessRegister);

        return binding.getRoot();
    }

    private void handleErrorRegister(int integer) {
        uiViewModel.hideLoadingOverlay();
        showMessage(integer);
    }

    private void handleSuccessRegister(int integer) {
        uiViewModel.hideLoadingOverlay();
        showMessage(integer);
        NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment);
    }

    private void showMessage(int resId) {
        Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show();
    }

    private void handleRegisterButtonClick(View view) {
        uiViewModel.showLoadingOverlay();
        registerViewModel.login(
                binding.etUsername.getText().toString(),
                binding.etPassword.getText().toString(),
                binding.etPasswordAgain.getText().toString());
    }
}