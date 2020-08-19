package com.futech.our_school.activities.start_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.futech.our_school.ActivityHelper;
import com.futech.our_school.Application;
import com.futech.our_school.MainActivity;
import com.futech.our_school.R;
import com.futech.our_school.RegisterControl.RegisterControl;
import com.futech.our_school.activities.register.RegisterActivity;
import com.futech.our_school.utils.LocaleHelper;

public class StartingForm extends ActivityHelper implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.onAttach(this);
        super.onCreate(savedInstanceState);

        RegisterControl registerControl = new RegisterControl(this);
        if (registerControl.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_starting_form);
        Button loginBtn, createAccountBtn;
        loginBtn = findViewById(R.id.changePasswordBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);

        loginBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        switch (v.getId()) {
            case R.id.changePasswordBtn:
                intent.putExtra(RegisterActivity.EXTRA_KEY, RegisterActivity.LOGIN_KEY);
                break;
            case R.id.createAccountBtn:
                intent.putExtra(RegisterActivity.EXTRA_KEY, RegisterActivity.SIGNUP_KEY);
                break;
        }
        startActivity(intent);
    }
}
