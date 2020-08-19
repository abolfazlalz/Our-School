package com.futech.our_school.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.futech.our_school.MainActivity;
import com.futech.our_school.R;
import com.futech.our_school.request.accounts.AccountData;
import com.futech.our_school.request.register.RegisterHelper;
import com.futech.our_school.utils.SchoolToast;
import com.futech.our_school.utils.request.listener.SelectListener;

public class RegisterAccountComplete extends RegisterAccountFragmentHelper {

    public static String LOGIN_REQUEST = "login";
    public static String SIGNUP_REQUEST = "signup";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_register_loading, container, false);
    }

    @Override
    public int getTitle() {
        return R.string.please_wait;
    }

    @Override
    public int getInformation() {
        return R.string.please_wait_information;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof RegisterActivity) {
            ((RegisterActivity) getActivity()).hideHeaderImage();
        }
        if (getArguments() != null && getArguments().containsKey("request")) {
            String request = getArguments().getString("request");
            assert request != null;
            runRequest(request);
        }else {
            programError();
        }
    }

    private void runRequest(String request) {
        RegisterHelper registerControl = new RegisterHelper(getContext());
        if (request.equals(LOGIN_REQUEST)) {
            if (getArguments() == null || !getArguments().containsKey(
                    "username") || !getArguments().containsKey("password")) {
                programError();
                return;
            }
            String username = getArguments().getString("username");
            String password = getArguments().getString("password");
            registerControl.login(username, password, new SelectListener<AccountData>() {
                @Override
                public void onSelect(AccountData data, boolean isOnline) {
                    successAndClosing(data);
                }

                @Override
                public void onError(String msg) {
                    SchoolToast.makeFail(getContext(), msg, SchoolToast.LENGTH_SHORT).show();
                    navigateFragment(R.id.action_loading_to_Login);
                }
            });

        }else if (request.equals(SIGNUP_REQUEST)) {
            Bundle args = getArguments();
            if (args == null) {
                programError();
                return;
            }
            String[] requiredFields = {BIRTHDAY_KEY, PHONE_NUMBER_KEY, USERNAME_KEY, PASSWORD_KEY, FIRST_NAME_KEY, CLASS_ID_KEY, BIRTHDAY_KEY};
            for (String required : requiredFields) {
                if (!args.containsKey(required)) {
                    programError();
                    return;
                }
            }
            String username = args.getString(USERNAME_KEY);
            String password = args.getString(PASSWORD_KEY);
            String phoneNumber = args.getString(PHONE_NUMBER_KEY);
            String birthday = args.getString(BIRTHDAY_KEY);
            String firstName = args.getString(FIRST_NAME_KEY);
            String gender = args.getString(GENDER_KEY);
            String lastName = args.getString(LAST_NAME_KEY);
            int classId = args.getInt(CLASS_ID_KEY);
            registerControl.createAccount(phoneNumber, username, password, firstName, lastName, classId, birthday, gender, new SelectListener<AccountData>() {
                @Override
                public void onSelect(AccountData data, boolean isOnline) {
                    successAndClosing(data);
                }

                @Override
                public void onError(String msg) {
                    SchoolToast.makeFail(getContext(), msg, SchoolToast.LENGTH_SHORT).show();
                    navigateFragment(R.id.action_loading_to_setInformation);
                }
            });
        }
    }

    private void successAndClosing(AccountData data) {
        startActivity(new Intent(getContext(), MainActivity.class));
        SchoolToast.makeSuccess(getContext(), "خوش آمدی " + data.getName(),
                SchoolToast.LENGTH_SHORT).show();
        if (getActivity() != null) getActivity().finish();
    }

    private void programError() {
        navigateFragment(R.id.action_loading_to_setInformation);
        SchoolToast.makeFail(getContext(),
                "خطایی در اجرای عملیات اتفاق افتاده است\nلطفا دوباره امتحان کنید",
                SchoolToast.LENGTH_SHORT).show();
    }
}
