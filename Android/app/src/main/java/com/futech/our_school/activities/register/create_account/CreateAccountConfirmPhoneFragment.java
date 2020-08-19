package com.futech.our_school.activities.register.create_account;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.futech.our_school.R;
import com.futech.our_school.activities.register.RegisterAccountFragmentHelper;
import com.futech.our_school.dialog.ProgressDialog;
import com.futech.our_school.utils.ConfirmCodeHelper;
import com.futech.our_school.utils.SchoolToast;
import com.futech.our_school.utils.VolleyErrorTranslator;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CreateAccountConfirmPhoneFragment extends RegisterAccountFragmentHelper {

    private String phoneNumber;
    private EditText confirmCodeText;
    private Timer resendCodeTimer;
    private TextView timerTextView;
    private Button resendButton;
    private ResendCodeTimerTask resendTimerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cs_confirm_phone, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null || !getArguments().containsKey(PHONE_NUMBER_KEY)) goToChangePhone();

        phoneNumber = getArguments().getString(PHONE_NUMBER_KEY);

        confirmCodeText = view.findViewById(R.id.confirmPhone);
        timerTextView = view.findViewById(R.id.resendCodeTimer);
        resendButton = view.findViewById(R.id.resendCodeButton);
        Button changePhone = view.findViewById(R.id.changePhoneNumber);
        Button nextBtn = view.findViewById(R.id.next_btn);

        changePhone.setOnClickListener(v -> navigateFragment(R.id.action_ConfirmPhone_to_EditPhone));

        nextBtn.setOnClickListener(v -> checkAndNextSession());

        resendTimerTask = new ResendCodeTimerTask();
        enableResendCodeTimer();

        resendButton.setOnClickListener(v -> onResendCodeClick());

        confirmCodeText.setOnEditorActionListener((v, actionId, event) -> {
            checkAndNextSession();
            return true;
        });
    }

    private void goToChangePhone() {
        Fragment fragment = getParentFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert fragment != null;
        NavHostFragment.findNavController(fragment).navigate(R.id.action_ConfirmPhone_to_EditPhone);
    }

    private void checkAndNextSession() {
        String code = confirmCodeText.getText().toString().trim();
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        ConfirmCodeHelper helper = new ConfirmCodeHelper(getContext());
        helper.checkConfirmCode(phoneNumber, code, response -> {
            if (response == ConfirmCodeHelper.VALID) {
                navigateFragment(R.id.action_ConfirmPhone_to_SetPassword);
            }else if (response == ConfirmCodeHelper.INVALID_CODE) {
                confirmCodeText.setError(getString(R.string.incorrect_code));
                SchoolToast.makeFail(getContext(), R.string.incorrect_code, SchoolToast.LENGTH_SHORT).show();
            }else if (response == ConfirmCodeHelper.OUT_OF_DATE_CODE) {
                confirmCodeText.setError(getString(R.string.out_of_date_code));
                SchoolToast.makeFail(getContext(), R.string.out_of_date_code, SchoolToast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        }, error -> {
            dialog.cancel();
        });
    }

    private void enableResendCodeTimer() {
        resendTimerTask = new ResendCodeTimerTask();
        resendCodeTimer = new Timer();
        resendCodeTimer.schedule(resendTimerTask, 0, 500);
    }

    private void canResendCode() {
        changeResendItemVisible(false);
    }

    private void setResendTextViewText(int seconds) {
        changeResendItemVisible(true);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int integerColor = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, requireActivity().getTheme());
        String hexColor = String.format("#%06X", (0xFFFFFF & integerColor));
        String html = getString(R.string.resend_code);
        html += String.format(Locale.getDefault(), " <font color='%s'>%d:%02d</font>", hexColor, minutes, seconds);

        timerTextView.setText(Html.fromHtml(html));
    }

    private void changeResendItemVisible(boolean isTimerOn) {
        timerTextView.setVisibility(isTimerOn ? View.VISIBLE : View.GONE);
        resendButton.setVisibility(isTimerOn ? View.GONE : View.VISIBLE);
    }

    private void onResendCodeClick() {
        ConfirmCodeHelper codeHelper = new ConfirmCodeHelper(this.getContext());
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        codeHelper.sendConfirmationCode(phoneNumber, response -> {
            dialog.cancel();
            if (!response.getData().isStatus()) {
                SchoolToast.makeFail(getContext(), getString(R.string.error_resend_code), SchoolToast.LENGTH_LONG).show();
            }
            enableResendCodeTimer();
        }, error -> {
            VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
            SchoolToast.makeFail(getContext(), translator.getMessage(), SchoolToast.LENGTH_LONG).show();
            dialog.cancel();
            enableResendCodeTimer();
        });
    }

    @Override
    public int getTitle() {
        return R.string.confirm_phone_header;
    }

    @Override
    public void onPause() {
        super.onPause();
        resendCodeTimer.cancel();
        resendCodeTimer.purge();
    }

    @Override
    public int getInformation() {
        return R.string.confirm_information_text;
    }

    /**
     * timer for resend code
     */
    private class ResendCodeTimerTask extends TimerTask {

        private long startTimerTime;
        private int targetTime;

        ResendCodeTimerTask() {
            startTimerTime = System.currentTimeMillis();
            targetTime = 90;
        }

        @Override
        public void run() {
            //after 1 minute and 30 seconds
            long now = System.currentTimeMillis();
            final int seconds = targetTime - (int) ((now - startTimerTime) / 1000);
            if (seconds <= 0) {
                CreateAccountConfirmPhoneFragment.this.resendCodeTimer.cancel();
                CreateAccountConfirmPhoneFragment.this.requireActivity().runOnUiThread(
                        CreateAccountConfirmPhoneFragment.this::canResendCode);
            }else {
                CreateAccountConfirmPhoneFragment.this.requireActivity().runOnUiThread(() -> setResendTextViewText(seconds));
            }
        }
    }
}
