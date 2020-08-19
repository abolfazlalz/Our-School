package com.futech.our_school.activities.register.create_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.futech.our_school.R;
import com.futech.our_school.activities.register.RegisterAccountFragmentHelper;
import com.futech.our_school.utils.SchoolToast;

public class CreateAccountPasswordFragment extends RegisterAccountFragmentHelper {

    private EditText editPassword, confirmEditPassword;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cs_set_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editPassword = view.findViewById(R.id.password_text);
        confirmEditPassword = view.findViewById(R.id.password_confirm_text);
        Button nextBtn = view.findViewById(R.id.next_btn);
        Button editPhoneNumber = view.findViewById(R.id.changePhoneNumber);

        nextBtn.setOnClickListener(v -> checkAndNextSession());
        confirmEditPassword.setOnEditorActionListener((v, actionId, event) -> {
            checkAndNextSession();
            return true;
        });

        editPhoneNumber.setOnClickListener(v -> navigateFragment(R.id.action_SetPassword_to_EditPhone));

    }

    private void checkAndNextSession() {
        String password = editPassword.getText().toString();
        String passwordConfirm = confirmEditPassword.getText().toString();

        if (password.length() < 8) {
            editPassword.setError(getString(R.string.password_length_error));
            SchoolToast.makeFail(getContext(), R.string.password_length_error, SchoolToast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordConfirm.equals(password)) {
            confirmEditPassword.setError(getString(R.string.not_match_password));
            SchoolToast.makeFail(getContext(), R.string.not_match_password, SchoolToast.LENGTH_SHORT).show();
            return;
        }

        Bundle args = getArguments();
        if (args == null) args = new Bundle();
        args.putString(RegisterAccountFragmentHelper.PASSWORD_KEY, editPassword.getText().toString());
        setArguments(args);
        navigateFragment(R.id.action_SetPassword_to_SetInformation);
    }

    @Override
    public int getTitle() {
        return R.string.set_password_header;
    }

    @Override
    public int getInformation() {
        return R.string.set_password_information;
    }
}
