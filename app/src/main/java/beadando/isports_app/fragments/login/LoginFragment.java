package beadando.isports_app.fragments.login;

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
import beadando.isports_app.databinding.FragmentLoginBinding;
import beadando.isports_app.fragments.UIViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private UIViewModel uiViewModel;
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        uiViewModel = new ViewModelProvider(requireActivity()).get(UIViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.tvRegister.setOnClickListener(this::navigateRegisterFragment);
        binding.btnLogin.setOnClickListener(this::handleLoginButtonClick);
        loginViewModel.errorMessage.observe(getViewLifecycleOwner(),this::handleErrorLogin);
        loginViewModel.successMessage.observe(getViewLifecycleOwner(), this::handleSuccessLogin);

        return binding.getRoot();
    }

    private void navigateRegisterFragment(View view) {
        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment);

    }

    private void handleLoginButtonClick(View view) {
        uiViewModel.showLoadingOverlay();
        loginViewModel.login(
                binding.etUsername.getText().toString(),
                binding.etPassword.getText().toString()
        );
    }

    private void handleSuccessLogin(int resId) {
        uiViewModel.hideLoadingOverlay();
        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_mainFragment);
        showMessage(resId);
    }

    private void handleErrorLogin(int resId) {
        uiViewModel.hideLoadingOverlay();
        showMessage(resId);
    }

    private void showMessage(int resId) {

        Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show();
    }
}