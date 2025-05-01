package beadando.isports_app.controllers;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;

import beadando.isports_app.R;

public class ToolbarController {
    private final TextView toolbarTitle;
    private final ImageView profileImageView;
    private final ImageButton addEventButton;
    private final NavController navController;

    public ToolbarController(TextView toolbarTitle, ImageView profileImageView, ImageButton addEventButton, NavController navController){
        this.toolbarTitle = toolbarTitle;
        this.profileImageView = profileImageView;
        this.addEventButton = addEventButton;
        this.navController = navController;
    }

    public void updateToolbar(int destinationId, View toolbar){
        if (destinationId == R.id.loginFragment || destinationId == R.id.registerFragment) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
            updateToolbarTitle(destinationId);
            updateToolbarIcon(destinationId);
        }
    }

    private void updateToolbarTitle(int destinationId) {
        String title = "";

        if (destinationId == R.id.mainFragment) {
            toolbarTitle.setText("Események");
        } else if (destinationId == R.id.profileFragment) {
            toolbarTitle.setText("Profil");
        } else if (destinationId == R.id.addEventFragment) {
            toolbarTitle.setText("Új esemény");
        } else if (destinationId == R.id.editProfileFragment) {
            toolbarTitle.setText("Profil szerkesztés");
        } else {
            toolbarTitle.setText(title);
        }
    }

    public void updateToolbarIcon(int destinationId) {
        if (profileImageView != null) {
            if (destinationId == R.id.profileFragment || destinationId == R.id.addEventFragment || destinationId == R.id.editProfileFragment) {
                profileImageView.setVisibility(View.INVISIBLE);
            } else {
                profileImageView.setVisibility(View.VISIBLE);
                profileImageView.setOnClickListener(v -> {
                    navController.navigate(R.id.profileFragment);
                });
            }
        }

        if (addEventButton != null) {
            if (destinationId == R.id.profileFragment || destinationId == R.id.addEventFragment || destinationId == R.id.editProfileFragment) {
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
}
