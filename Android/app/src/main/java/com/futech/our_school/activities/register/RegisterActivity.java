package com.futech.our_school.activities.register;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.futech.our_school.ActivityHelper;
import com.futech.our_school.Application;
import com.futech.our_school.R;
import com.futech.our_school.utils.SchoolToast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RegisterActivity extends ActivityHelper {

    public static final String EXTRA_KEY = "fragment";
    public static final int LOGIN_KEY = 0;
    public static final int SIGNUP_KEY = 1;

    private Fragment fragment;
    private ImageView headerImage;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        headerImage = findViewById(R.id.headerImage);

        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(EXTRA_KEY))
            return;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert fragment != null;
        if (getIntent().getExtras().getInt(EXTRA_KEY) == SIGNUP_KEY) {
            NavHostFragment.findNavController(fragment).navigate(
                    R.id.action_LoginFragment_to_CreateAccount);
        }
    }

    public void changeView(int key) {
        changeView(key, null);
    }

    public void changeView(int key, Bundle args) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert fragment != null;
        switch (key) {
            case LOGIN_KEY:
                NavHostFragment.findNavController(fragment).navigate(R.id.action_CreateAccountFragment_to_LoginActivity, args);
                break;
            case SIGNUP_KEY:
                NavHostFragment.findNavController(fragment).navigate(R.id.action_LoginFragment_to_CreateAccount, args);
                break;
        }
    }

    public void hideHeaderImage() {
        headerImage.setVisibility(GONE);
    }
    public void showHeaderImage() {
        headerImage.setVisibility(VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        SchoolToast.makeInformation(this, R.string.double_click_to_exit, SchoolToast.LENGTH_SHORT);

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
