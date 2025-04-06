package beadando.isports_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import beadando.isports_app.MainActivity;
import beadando.isports_app.R;
import beadando.isports_app.domain.User;
import beadando.isports_app.util.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private TextView emailTextView;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Get user data from SessionManager
        SessionManager sessionManager = new SessionManager(requireContext());
        User currentUser = sessionManager.getUser();

        // Display user information
        if (currentUser != null) {
            usernameTextView.setText(currentUser.getUsername());
            emailTextView.setText(currentUser.getEmail());
        }

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            }
        });
    }
}