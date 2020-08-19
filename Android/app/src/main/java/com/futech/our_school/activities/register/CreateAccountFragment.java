package com.futech.our_school.activities.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.futech.our_school.R;

public class CreateAccountFragment extends Fragment {

    private static String TAG = "CreateAccountFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment fragment = getParentFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert fragment != null;
        NavController controller = NavHostFragment.findNavController(fragment);
        controller.addOnDestinationChangedListener((controller1, destination, arguments) -> {
            if (fragment instanceof RegisterAccountFragmentHelper) {
                String text = requireContext().getString(((RegisterAccountFragmentHelper) fragment).getTitle());
                Log.i(TAG, "onDestinationChanged: " + text);
            }
        });
    }
}
