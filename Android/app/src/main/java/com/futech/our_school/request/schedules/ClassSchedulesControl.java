package com.futech.our_school.request.schedules;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.RegisterControl.RegisterControl;
import com.futech.our_school.objects.ClassData;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.listener.SelectListener;

import java.util.List;

public class ClassSchedulesControl extends ApiHelper<ClassApiData> {


    public ClassSchedulesControl(Context context) {
        super(context, context.getString(R.string.api_address) + "users/students.php",
                ClassApiData.class);
        setTag("class-schedule");
    }

    public void getTodayClasses(SelectListener<List<ClassData>> listener) {
        ApiControl<ClassApiData> api = getApiControl("get-class");
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.response(new ApiControl.ApiListener<ClassApiData>() {
            @Override
            public void onResponse(ClassApiData api) {
                listener.onSelect(api.getData().getItems(), true);
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
                listener.onError(translator.getMessage());
            }
        });
    }

}
