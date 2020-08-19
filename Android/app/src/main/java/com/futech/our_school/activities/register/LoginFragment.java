package com.futech.our_school.activities.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.futech.our_school.R;

public class LoginFragment extends RegisterAccountFragmentHelper implements View.OnClickListener {

    private EditText editUsername;
    private EditText editPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.signupBtn).setOnClickListener(view1 -> NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_LoginFragment_to_CreateAccount));
        editUsername = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        Button login = view.findViewById(R.id.login_btn);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.login_btn) return;
        Bundle arguments = new Bundle();
        arguments.putString("username", editUsername.getText().toString().trim());
        arguments.putString("password", editPassword.getText().toString());
        arguments.putString("request", "login");
        setArguments(arguments);
        navigateFragment(R.id.action_LoginFragment_to_Loading);
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    public int getInformation() {
        return 0;
    }
}