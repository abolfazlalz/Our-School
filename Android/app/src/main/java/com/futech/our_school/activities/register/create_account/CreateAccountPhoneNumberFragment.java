package com.futech.our_school.activities.register.create_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.futech.our_school.R;
import com.futech.our_school.activities.register.RegisterAccountFragmentHelper;
import com.futech.our_school.activities.register.RegisterActivity;
import com.futech.our_school.dialog.ProgressDialog;
import com.futech.our_school.utils.ConfirmCodeHelper;
import com.futech.our_school.utils.SchoolToast;

public class CreateAccountPhoneNumberFragment extends RegisterAccountFragmentHelper {

    private EditText phoneNumberText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cs_phone_number, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneNumberText = view.findViewById(R.id.phoneNumber);

        Button btn = view.findViewById(R.id.next_btn);
        btn.setOnClickListener(this::onNextBtnClick);
        final Button login = view.findViewById(R.id.changePasswordBtn);
        login.setOnClickListener(v -> ((RegisterActivity) requireActivity()).changeView(RegisterActivity.LOGIN_KEY));

        phoneNumberText.setOnEditorActionListener((v, actionId, event) -> {
            onNextBtnClick(login);
            return true;
        });
    }

    private void onNextBtnClick(View v) {
        String phoneNumber = phoneNumberText.getText().toString().trim();
        if (phoneNumber.equals("")) {
            phoneNumberText.setError(getString(R.string.phone_number_field_empty));
            SchoolToast.makeFail(getContext(), R.string.phone_number_field_empty, SchoolToast.LENGTH_SHORT).show();
            return;
        }else if (phoneNumber.length() < 11) {
            phoneNumberText.setError(getString(R.string.incorrect_phone_number));
            SchoolToast.makeFail(getContext(), R.string.incorrect_phone_number, SchoolToast.LENGTH_SHORT).show();
            return;
        }

        ConfirmCodeHelper helper = new ConfirmCodeHelper(requireContext());
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        helper.sendConfirmationCode(phoneNumber, response -> {
            dialog.cancel();
            if (response.getData().isStatus()) {
                Bundle args = new Bundle();
                dialog.cancel();
                args.putString(PHONE_NUMBER_KEY, phoneNumber);
                setArguments(args);
                navigateFragment(R.id.action_EditPhone_to_ConfirmPhone);
            }else if (response.getData().getCode() == 0) {
                phoneNumberText.setError(getString(R.string.phone_number_taken));
                SchoolToast.makeFail(getContext(), R.string.phone_number_taken, SchoolToast.LENGTH_SHORT).show();
            }
        }, error -> dialog.cancel());
    }

    @Override
    public int getTitle() {
        return R.string.create_account_header;
    }

    @Override
    public int getInformation() {
        return R.string.create_account_information_text;
    }
}
