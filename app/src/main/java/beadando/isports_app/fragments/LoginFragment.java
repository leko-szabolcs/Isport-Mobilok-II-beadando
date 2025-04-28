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

import com.google.firebase.firestore.FirebaseFirestore;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.data.repostiory.AuthRepository;
import beadando.isports_app.domain.User;
import beadando.isports_app.util.SessionManager;

public class LoginFragment extends Fragment {
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);

        AuthRepository auth = new AuthRepository(FirebaseFirestore.getInstance());
        LoginAdapter loginAdapter =  new LoginAdapter(etUsername, etPassword);
        sessionManager = new SessionManager(requireActivity().getApplicationContext());

        view.findViewById(R.id.tvRegister).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        );

        view.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            if (!loginAdapter.isValidLogin(getContext())) return;
            ((MainActivity) requireActivity()).showLoading(true);
            auth.login(loginAdapter.getEmail(), loginAdapter.getPassword(), new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    ((MainActivity) requireActivity()).showLoading(false);
                    sessionManager.saveUser(user);
                    sessionManager.setLoggedIn(true);
                    Toast.makeText(getContext(), "Sikeres bejelentkezés", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
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