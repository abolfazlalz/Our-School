package com.futech.our_school.activities.register;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.futech.our_school.R;

public abstract class RegisterAccountFragmentHelper extends Fragment {

    public abstract int getTitle();
    public abstract int getInformation();
    private Bundle arguments;

    public static final String PASSWORD_KEY = "password";
    public static final String USERNAME_KEY ="username";
    public static final String BIRTHDAY_KEY = "birthday";
    public static final String FIRST_NAME_KEY = "first_name";
    public static final String LAST_NAME_KEY = "last_name";
    public static final String PHONE_NUMBER_KEY = "phone_number";
    public static final String GENDER_KEY = "gender";
    public static final String CLASS_ID_KEY = "class_id";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof RegisterActivity) {
            ((RegisterActivity) getActivity()).showHeaderImage();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arguments = getArguments();
    }

    protected void navigateFragment(@IdRes int targetFragment) {
        navigateFragment(getParentFragmentManager(), targetFragment, getArguments());
    }

    static void navigateFragment(FragmentManager manager, @IdRes int targetFragment) {
        navigateFragment(manager, targetFragment, null);
    }

    static void navigateFragment(FragmentManager fragmentManager,
                                 @IdRes int targetFragment, Bundle args) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.nav_host_fragment);
        assert fragment != null;
        NavHostFragment.findNavController(fragment).navigate(targetFragment, args);
    }
}
