package beadando.isports_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import beadando.isports_app.R;
import beadando.isports_app.data.repostiory.AuthRepository;

import beadando.isports_app.MainActivity;

public class RegisterFragment extends Fragment {

    private AuthRepository auth;
    private RegisterAdapter formAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        auth = new AuthRepository();

        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etPasswordAgain = view.findViewById(R.id.etPasswordAgain);

        formAdapter = new RegisterAdapter(etUsername, etPassword, etPasswordAgain);

        view.findViewById(R.id.btnRegister).setOnClickListener(v ->{
            if (!formAdapter.isValidRegistration(getContext())) return;
            ((MainActivity) requireActivity()).showLoading(true);

            auth.register(formAdapter.getEmail(), formAdapter.getPassword(), new AuthRepository.RegisterCallback() {
                @Override
                public void onSuccess() {
                    ((MainActivity) requireActivity()).showLoading(false);
                    Toast.makeText(getContext(), "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view)
                            .navigate(R.id.action_registerFragment_to_loginFragment);
                }

                @Override
                public void onFailure(Exception e) {
                    ((MainActivity) requireActivity()).showLoading(false);
                    Toast.makeText(getContext(), "Hiba: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        return view;
    }
}