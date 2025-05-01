package beadando.isports_app;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import beadando.isports_app.data.repostiory.AuthRepository;
import beadando.isports_app.util.SessionManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    SessionManager sessionManager;

    private MainAppViewModel mainAppViewModel;
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
        mainAppViewModel = new ViewModelProvider(this).get(MainAppViewModel.class);

        mainAppViewModel.checkLoginStatus();
        mainAppViewModel.isLoggedIn.observe(this, this::showFirstFragmentBasedOnLoginStatus);
        mainAppViewModel.loading.observe(this, this::showLoading);

        Toolbar mainToolbar = findViewById(R.id.navToolbar);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        globalProgressBar = findViewById(R.id.globalProgressBar);

        profileImageView = mainToolbar.findViewById(R.id.profileImageView);
        toolbarTitle = mainToolbar.findViewById(R.id.toolbarTitle);
        addEventButton = mainToolbar.findViewById(R.id.addEventButton);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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

    private void showFirstFragmentBasedOnLoginStatus(Boolean isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(R.id.mainFragment);
        } else {
            navController.navigate(R.id.loginFragment);
        }
    }

    public void showLoading(boolean isVisible) {
        if (isVisible) {
            loadingOverlay.setVisibility(View.VISIBLE);
            globalProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingOverlay.setVisibility(View.GONE);
            globalProgressBar.setVisibility(View.GONE);
        }
    }

    public void showLoadingOverlay(boolean show) {
        FrameLayout loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}