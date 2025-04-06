package beadando.isports_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private ImageView profileImageView;
    private TextView toolbarTitle;
    private ImageButton addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up custom toolbar elements
        Toolbar mainToolbar = findViewById(R.id.navToolbar);
        profileImageView = mainToolbar.findViewById(R.id.profileImageView);
        toolbarTitle = mainToolbar.findViewById(R.id.toolbarTitle);
        addEventButton = mainToolbar.findViewById(R.id.addEventButton);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide default title

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainFragment
        ).build();

        // Set up click listeners for profile and add event buttons
        if (profileImageView != null) {
            profileImageView.setOnClickListener(v -> {
                // Navigate to profile fragment
                navController.navigate(R.id.profileFragment);
            });
        }

        // Hide toolbar on login & register screens and update title and buttons for other destinations
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                mainToolbar.setVisibility(View.GONE);
            } else {
                mainToolbar.setVisibility(View.VISIBLE);

                // Update toolbar title based on destination
                if (toolbarTitle != null) {
                    if (destination.getId() == R.id.mainFragment) {
                        toolbarTitle.setText("Események");
                    } else if (destination.getId() == R.id.profileFragment) {
                        toolbarTitle.setText("Profil");
                    } else if (destination.getId() == R.id.addEventFragment) {
                        toolbarTitle.setText("Új esemény");
                    } else {
                        // Default title or get from destination label
                        toolbarTitle.setText(destination.getLabel());
                    }
                }

                // Show/hide profile image based on destination
                if (profileImageView != null) {
                    if (destination.getId() == R.id.profileFragment || destination.getId() == R.id.addEventFragment) {
                        // Hide profile image on Profile and AddEvent fragments
                        profileImageView.setVisibility(View.INVISIBLE);
                    } else {
                        // Show profile image on other fragments
                        profileImageView.setVisibility(View.VISIBLE);
                    }
                }

                // Change addEventButton icon and behavior based on destination
                if (addEventButton != null) {
                    if (destination.getId() == R.id.profileFragment || destination.getId() == R.id.addEventFragment) {
                        // Change to back arrow for Profile and AddEvent fragments
                        addEventButton.setImageResource(R.drawable.ic_back);
                        addEventButton.setOnClickListener(v -> {
                            // Navigate back to main fragment
                            navController.navigate(R.id.mainFragment);
                        });
                    } else {
                        // Use plus icon for main fragment
                        addEventButton.setImageResource(R.drawable.ic_plus);
                        addEventButton.setOnClickListener(v -> {
                            // Navigate to new event fragment
                            navController.navigate(R.id.addEventFragment);
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}