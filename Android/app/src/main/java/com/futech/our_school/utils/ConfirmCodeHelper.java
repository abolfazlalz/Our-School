package com.futech.our_school.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.values.ConfirmCodeApi;

public class ConfirmCodeHelper extends ApiHelper<ConfirmCodeApi> {

    public static final int OUT_OF_DATE_CODE = 0;
    public static final int INVALID_CODE = 1;
    public static final int VALID = 2;

    public ConfirmCodeHelper(Context context) {
        super(context, context.getString(R.string.api_address) + "users/accounts.php", ConfirmCodeApi.class);
    }

    public void sendConfirmationCode(String phoneNumber, Response.Listener<ConfirmCodeApi> listener, Response.ErrorListener errorListener) {
        ApiControl<ConfirmCodeApi> apiControl = getApiControl("check_phone_number");
        apiControl.addParameter(Request.Method.POST, "phone_number", phoneNumber);
        apiControl.response(new ApiControl.ApiListener<ConfirmCodeApi>() {
            @Override
            public void onResponse(ConfirmCodeApi api) {
                listener.onResponse(api);
            }

            @Override
            public void onError(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });
    }

    public void checkConfirmCode(String phoneNumber, String confirmCode, Response.Listener<Integer> listener, Response.ErrorListener errorListener) {
        ApiControl<ConfirmCodeApi> apiControl = getApiControl("confirm_phone_number");
        apiControl.addParameter(Request.Method.POST, "phone_number", phoneNumber);
        apiControl.addParameter(Request.Method.POST, "code", confirmCode);
        apiControl.response(new ApiControl.ApiListener<ConfirmCodeApi>() {
            @Override
            public void onResponse(ConfirmCodeApi api) {
                listener.onResponse(api.getData().getCode());
            }

            @Override
            public void onError(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        });
    }

}
