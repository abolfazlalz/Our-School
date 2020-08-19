package com.futech.our_school.request.register;

import android.content.Context;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.RegisterControl.RegisterControl;
import com.futech.our_school.request.accounts.AccountApiData;
import com.futech.our_school.request.accounts.AccountData;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.listener.DataChangeListener;
import com.futech.our_school.utils.request.listener.SelectListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterHelper extends ApiHelper<AccountApiData> {

    public RegisterHelper(Context context) {
        super(context, context.getString(R.string.api_address) + "users/accounts.php",
                AccountApiData.class);
        setTag("register-api");
    }

    public void login(String username, String password, SelectListener<AccountData> listener) {
        String deviceModel = android.os.Build.MODEL;
        String id = Build.ID;

        ApiControl<AccountApiData> api = getApiControl("login");

        api.addParameter(Request.Method.POST, "username", username);
        api.addParameter(Request.Method.POST, "password", password);
        api.addParameter(Request.Method.POST, "ip", id);
        api.addParameter(Request.Method.POST, "device_name", deviceModel);

        api.response(new ApiControl.ApiListener<AccountApiData>() {
            @Override
            public void onResponse(AccountApiData api) {
                if (api.getData().isSuccess()) {
                    listener.onSelect(api.getData().getInformation(), true);
                    RegisterControl registerControl = new RegisterControl(getContext());
                    registerControl.setLoginKey(api.getData().getInformation().getKey());
                }else {
                    listener.onError(api.getData().getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
                listener.onError(translator.getMessage());
            }
        });
    }

    public void signOut(DataChangeListener listener) {
        ApiControl<AccountApiData> api = getApiControl("sign-out");
        api.addParameter(Request.Method.POST, "key", RegisterControl.getRegisterKey(getContext()));
        api.response(new ApiControl.ApiListener<AccountApiData>() {
            @Override
            public void onResponse(AccountApiData api) {
                if (api.getData().isSuccess()) {
                    RegisterControl.deletePrefKey(getContext());
                    listener.onChange();
                }else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });
    }

    public void createAccount(String phoneNumber, String username, String password, String name, String lastName, int classId, String birthday, String gender, SelectListener<AccountData> listener) {
        String deviceModel = android.os.Build.MODEL;
        String id = Build.ID;

        String apiAddress = getContext().getString(R.string.api_address) + "users/students.php";
        ApiHelper<AccountApiData> apiHelper = new ApiHelper<>(getContext(), apiAddress,
                AccountApiData.class);
        ApiControl<AccountApiData> api = apiHelper.getApiControl("create-account");

        api.addParameter(Request.Method.POST, "phone_number", phoneNumber);
        api.addParameter(Request.Method.POST, "class_id", String.valueOf(classId));
        api.addParameter(Request.Method.POST, "username", username);
        api.addParameter(Request.Method.POST, "password", password);
        api.addParameter(Request.Method.POST, "name", name);
        api.addParameter(Request.Method.POST, "last_name", lastName);
        api.addParameter(Request.Method.POST, "birthday", birthday);
        api.addParameter(Request.Method.POST, "gender", gender);
        api.addParameter(Request.Method.POST, "ip", id);
        api.addParameter(Request.Method.POST, "device_name", deviceModel);

        api.response(new ApiControl.ApiListener<AccountApiData>() {
            @Override
            public void onResponse(AccountApiData api) {
                if (api.getData().isSuccess()) {
                    listener.onSelect(api.getData().getInformation(), true);
                    RegisterControl registerControl = new RegisterControl(getContext());
                    registerControl.setLoginKey(api.getData().getInformation().getKey());
                }else {
                    listener.onError(api.getData().getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
                listener.onError(translator.getMessage());
            }
        });
    }

    public void getRegisterInformation(SelectListener<AccountData> listener) {
        ApiControl<AccountApiData> api = getApiControl("get-information");
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.response(new ApiControl.ApiListener<AccountApiData>() {
            @Override
            public void onResponse(AccountApiData api) {
                if (api.getData().isSuccess())
                    listener.onSelect(api.getData().getInformation(), true);
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
                listener.onError(translator.getMessage());
            }
        });
    }

}
