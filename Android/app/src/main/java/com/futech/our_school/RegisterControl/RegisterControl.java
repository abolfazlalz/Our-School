package com.futech.our_school.RegisterControl;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.StringDef;

import com.futech.our_school.request.accounts.AccountData;
import com.futech.our_school.request.register.RegisterHelper;
import com.futech.our_school.utils.request.listener.SelectListener;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class RegisterControl {

    private final Context context;
    private RegisterHelper helper;

    private static final String PREF_NAME = "register";
    private static final String REGISTER_KEY = "register-key";

    public static final String REGISTER_NAME = "name";
    public static final String REGISTER_LAST_NAME = "last-name";
    public static final String REGISTER_PHOTO = "photo";
    public static final String REGISTER_USERNAME = "username";
    public static final String REGISTER_TOTAL_STUDY = "total-study";
    public static final String REGISTER_MONTH_STUDY = "month-study";


    @Retention(SOURCE)
    @StringDef(
            {REGISTER_LAST_NAME, REGISTER_NAME, REGISTER_PHOTO, REGISTER_KEY, REGISTER_USERNAME, REGISTER_MONTH_STUDY, REGISTER_TOTAL_STUDY})
    @interface RegisterKeys {}

    public RegisterControl(Context context) {
        this.context = context;
        helper = new RegisterHelper(getContext());
    }

    public boolean isLogin() {
        return getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).contains(
                REGISTER_KEY);
    }

    public static void deletePrefKey(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().remove(REGISTER_KEY).apply();
    }

    public static String getRegisterKey(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(REGISTER_KEY,
                "");
    }

    public void setLoginKey(String key) {
        getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString(
                REGISTER_KEY, key).apply();
    }

    public void updateRegisterData(ProgressComplete progressComplete) {
        helper.getRegisterInformation(new SelectListener<AccountData>() {
            @Override
            public void onSelect(AccountData data, boolean isOnline) {
                progressComplete.onRegisterUpdate(data);
                SharedPreferences.Editor register = getContext().getSharedPreferences(PREF_NAME,
                        Context.MODE_PRIVATE).edit();
                register.putString(REGISTER_NAME, data.getName());
                register.putString(REGISTER_LAST_NAME, data.getLastName());
                register.putString(REGISTER_PHOTO, data.getPhoto());
                register.putString(REGISTER_USERNAME, data.getUsername());
                register.apply();


            }

            @Override
            public void onError(String msg) {
                progressComplete.onRegisterError(msg);
            }
        });
    }

    public static String getSavedRegisterData(Context context, @RegisterKeys String dataKey) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(dataKey, "");
    }

    public void cancel() {
        helper.cancel();
    }

    public Context getContext() {
        return context;
    }


    public interface ProgressComplete {
        void onRegisterUpdate(AccountData accountData);

        void onRegisterError(String message);
    }
}
