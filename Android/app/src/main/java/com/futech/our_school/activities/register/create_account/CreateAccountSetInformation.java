package com.futech.our_school.activities.register.create_account;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.futech.our_school.R;
import com.futech.our_school.activities.register.RegisterAccountComplete;
import com.futech.our_school.activities.register.RegisterAccountFragmentHelper;
import com.futech.our_school.activities.register.SelectSchoolHelper;
import com.futech.our_school.utils.ConvertingUtils;
import com.futech.our_school.utils.SchoolToast;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class CreateAccountSetInformation extends RegisterAccountFragmentHelper implements View.OnClickListener, TextWatcher {

    private Date birthday = null;
    private PersianDatePickerDialog picker;
    private Button birthdayButton;
    private SlidingUpPanelLayout sliding;
    private SelectSchoolHelper school;
    private EditText editUsername, editName, editLastName;
    private RadioGroup genderChoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cs_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        birthdayButton = view.findViewById(R.id.birthdayText);
        initializeDatePicker();
        birthdayButton.setOnClickListener(v -> picker.show());
        Button changePassword = view.findViewById(R.id.changePasswordBtn);
        Button choiceSchool = view.findViewById(R.id.choice_school);
        changePassword.setOnClickListener(this);
        choiceSchool.setOnClickListener(v -> showChoiceSchoolPanel());
        sliding = view.findViewById(R.id.sliding_panel);
        school = new SelectSchoolHelper(view);
        genderChoice = view.findViewById(R.id.gender_fields);

        editUsername = view.findViewById(R.id.edit_username);
        editName = view.findViewById(R.id.edit_name);
        editLastName = view.findViewById(R.id.edit_last_name);

        Button nextBtn = view.findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);

        editUsername.addTextChangedListener(this);
        Button doneBtn = view.findViewById(R.id.done_button);
        doneBtn.setOnClickListener(this);
    }

    private void showChoiceSchoolPanel() {
        sliding.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    private void initializeDatePicker() {
        if (getContext() == null) return;
        int colorInteger = ResourcesCompat.getColor(getResources(), R.color.colorPrimary,
                getContext().getTheme());
        picker = new PersianDatePickerDialog(getContext()).
                setPositiveButtonString("باشه").setNegativeButton("بیخیال").setTodayButton(
                "امروز").setTodayButtonVisible(true).setMinYear(1330).setMaxYear(
                PersianDatePickerDialog.THIS_YEAR - 6).setActionTextColor(
                colorInteger).setTitleType(
                PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR).setShowInBottomSheet(
                true).setListener(new Listener() {
            @Override
            public void onDateSelected(PersianCalendar persianCalendar) {
                birthday = persianCalendar.getTime();

                if (getContext() == null) return;
                String birthdayText = String.format(Locale.getDefault(), "%d %s سال %d",
                        persianCalendar.getPersianDay(), persianCalendar.getPersianMonthName(),
                        persianCalendar.getPersianYear());
                String birthday = getContext().getString(R.string.your_birthday);
                birthday += String.format(": <font color='%s'>%s</font>",
                        ConvertingUtils.colorResourceToStringHex(getContext(),
                                R.color.colorPrimary), birthdayText);
                birthdayButton.setText(Html.fromHtml(birthday));
            }

            @Override
            public void onDismissed() {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getTitle() {
        return R.string.cs_information_header;
    }

    @Override
    public int getInformation() {
        return R.string.cs_information_information;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.changePasswordBtn:
                navigateFragment(R.id.action_SetInformation_to_SetPassword);
                break;
            case R.id.done_button:
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case R.id.next_btn:
                String usernameText = editUsername.getText().toString().trim();
                String nameText = editName.getText().toString().trim();
                String lastNameText = editLastName.getText().toString().trim();
                Bundle args = getArguments();
                if (args == null) {
                    navigateFragment(R.id.action_SetInformation_to_PhoneNumber);
                    return;
                }

                if (birthday == null) {
                    SchoolToast.makeFail(getContext(), R.string.fill_birthday,
                            SchoolToast.LENGTH_SHORT).show();
                    return;
                }else if (nameText.equals("")) {
                    SchoolToast.makeFail(getContext(), R.string.empty_first_name_field,
                            SchoolToast.LENGTH_SHORT).show();
                    editName.setError(requireContext().getString(R.string.empty_first_name_field));
                }else if (lastNameText.equals("")) {
                    SchoolToast.makeFail(getContext(), R.string.empty_last_name_field,
                            SchoolToast.LENGTH_SHORT).show();
                    editLastName.setError(
                            requireContext().getString(R.string.empty_last_name_field));
                }else if (usernameText.equals("")) {
                    SchoolToast.makeFail(getContext(), R.string.enter_username,
                            SchoolToast.LENGTH_SHORT).show();
                    editUsername.setError(requireContext().getString(R.string.enter_username));
                }

                SimpleDateFormat format = new SimpleDateFormat(
                        requireContext().getString(R.string.date_db_format), Locale.getDefault());

                args.putString(USERNAME_KEY, usernameText);
                args.putString(FIRST_NAME_KEY, nameText);
                args.putString(LAST_NAME_KEY, lastNameText);
                args.putString(BIRTHDAY_KEY, format.format(birthday));
                args.putInt(CLASS_ID_KEY, school.getClassSelected());
                if (genderChoice.getCheckedRadioButtonId() == R.id.female_choice)
                    args.putString(GENDER_KEY, "female");
                else if (genderChoice.getCheckedRadioButtonId() == R.id.male_choice)
                    args.putString(GENDER_KEY, "male");

                args.putString("request", RegisterAccountComplete.SIGNUP_REQUEST);
                setArguments(args);
                navigateFragment(R.id.action_setInformation_to_loading);
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        int length = text.length();

        String pattern = "^[a-zA-Z0-9._-]{3,}$";

        if (length > 0 && !Pattern.matches(pattern, text)) {
            s.delete(length - 1, length);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
