package beadando.isports_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import beadando.isports_app.data.repostiory.AuthRepository;
import beadando.isports_app.util.SessionManager;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private FrameLayout loadingOverlay;
    private ProgressBar globalProgressBar;
    private ImageView profileImageView;
    private TextView toolbarTitle;
    private ImageButton addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.navToolbar);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        globalProgressBar = findViewById(R.id.globalProgressBar);

        profileImageView = mainToolbar.findViewById(R.id.profileImageView);
        toolbarTitle = mainToolbar.findViewById(R.id.toolbarTitle);
        addEventButton = mainToolbar.findViewById(R.id.addEventButton);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            NavHostFragment navHostFragment =
                    (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            navController = navHostFragment.getNavController();
            navController.navigate(R.id.mainFragment);
        }

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainFragment
        ).build();

        if (profileImageView != null) {
            profileImageView.setOnClickListener(v -> {
                navController.navigate(R.id.profileFragment);
            });
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                mainToolbar.setVisibility(View.GONE);
            } else {
                mainToolbar.setVisibility(View.VISIBLE);

                if (toolbarTitle != null) {
                    if (destination.getId() == R.id.mainFragment) {
                        toolbarTitle.setText("Események");
                    } else if (destination.getId() == R.id.profileFragment) {
                        toolbarTitle.setText("Profil");
                    } else if (destination.getId() == R.id.addEventFragment) {
                        toolbarTitle.setText("Új esemény");
                    } else if (destination.getId() == R.id.editProfileFragment) {
                        toolbarTitle.setText("Profil szerkesztés");
                    } else {
                        toolbarTitle.setText(destination.getLabel());
                    }
                }

                if (profileImageView != null) {
                    if (destination.getId() == R.id.profileFragment || destination.getId() == R.id.addEventFragment || destination.getId() == R.id.editProfileFragment) {
                        profileImageView.setVisibility(View.INVISIBLE);
                    } else {
                        profileImageView.setVisibility(View.VISIBLE);
                    }
                }

                if (addEventButton != null) {
                    if (destination.getId() == R.id.profileFragment || destination.getId() == R.id.addEventFragment || destination.getId() == R.id.editProfileFragment) {
                        addEventButton.setImageResource(R.drawable.ic_back);
                        addEventButton.setOnClickListener(v -> {
                            navController.navigate(R.id.mainFragment);
                        });
                    } else {
                        addEventButton.setImageResource(R.drawable.ic_plus);
                        addEventButton.setOnClickListener(v -> {
                            navController.navigate(R.id.addEventFragment);
                        });
                    }
                }
            }
        });
    }

    public void showLoading(boolean show) {
        if (show) {
            loadingOverlay.setVisibility(View.VISIBLE);
            globalProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingOverlay.setVisibility(View.GONE);
            globalProgressBar.setVisibility(View.GONE);
        }
    }

    public void logout() {
        new AuthRepository().logout(() -> {
            new SessionManager(this).clearSession();
            Toast.makeText(this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();
            navController.popBackStack(R.id.loginFragment, false);
            navController.navigate(R.id.loginFragment);
        });
    }
}