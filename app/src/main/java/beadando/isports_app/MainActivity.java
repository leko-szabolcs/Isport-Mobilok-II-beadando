package beadando.isports_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import javax.inject.Inject;

import beadando.isports_app.controllers.ToolbarController;
import beadando.isports_app.databinding.ActivityMainBinding;
import beadando.isports_app.fragments.UIViewModel;
import beadando.isports_app.utils.SessionManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    SessionManager sessionManager;

    private MainAppViewModel mainAppViewModel;
    private UIViewModel uiViewModel;
    private ActivityMainBinding binding;
    private NavController navController;
    private ImageView profileImageView;
    private TextView toolbarTitle;
    private ImageButton addEventButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainAppViewModel = new ViewModelProvider(this).get(MainAppViewModel.class);
        uiViewModel = new ViewModelProvider(this).get(UIViewModel.class);

        mainAppViewModel.checkLoginStatus();
        mainAppViewModel.isLoggedIn.observe(this, this::showFragmentBasedOnLoginStatus);
        mainAppViewModel.loading.observe(this, this::setLoadingOverlay);
        uiViewModel.loadingOverlay.observe(this, this::showLoadingOverlay);

        // Initialize toolbar
        Toolbar mainToolbar = findViewById(R.id.navToolbar);

        profileImageView = mainToolbar.findViewById(R.id.profileImageView);
        toolbarTitle = mainToolbar.findViewById(R.id.toolbarTitle);
        addEventButton = mainToolbar.findViewById(R.id.addEventButton);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


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

    private void showFragmentBasedOnLoginStatus(Boolean isLoggedIn) {
        if (navController.getCurrentDestination() == null) return;
        int currentId = navController.getCurrentDestination().getId();
        if (isLoggedIn  && currentId != R.id.mainFragment) {
            navController.navigate(R.id.mainFragment);
        } else if (!isLoggedIn && currentId != R.id.loginFragment){
            navController.navigate(R.id.loginFragment);
        }
    }

    private void setLoadingOverlay(boolean isVisible) {
        if (isVisible) {
            uiViewModel.showLoadingOverlay();
        }else{
            uiViewModel.hideLoadingOverlay();
        }
    }

    public void showLoadingOverlay(boolean isVisible) {
        binding.loadingOverlay.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}