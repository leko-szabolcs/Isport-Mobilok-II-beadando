package beadando.isports_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private FrameLayout loadingOverlay;
    private ProgressBar globalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = findViewById(R.id.navToolbar);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        globalProgressBar = findViewById(R.id.globalProgressBar);
        setSupportActionBar(mainToolbar);

        setTitle("Navigation app");

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainFragment
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // HIDE MENU ON LOGIN & REGISTER
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                mainToolbar.setVisibility(View.GONE);
            } else {
                mainToolbar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_logout) {
            new AuthRepository().logout(() -> {
                Toast.makeText(this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();
                navController.popBackStack(R.id.loginFragment, false);
                navController.navigate(R.id.loginFragment);
            });
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
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


}