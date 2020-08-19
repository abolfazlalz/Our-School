package com.futech.our_school.request.school;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.listener.SelectListener;

public class SchoolHelper extends ApiHelper<SchoolApiData> {

    private ApiHelper<SchoolClassApiData> schoolClassApi;

    public SchoolHelper(Context context) {
        super(context, context.getString(R.string.api_address) + "school/index.php",
                SchoolApiData.class);
        schoolClassApi = new ApiHelper<>(context,
                context.getString(R.string.api_address) + "school/index.php",
                SchoolClassApiData.class);
        setTag("school");
    }

    public void getSchoolInCity(String cityName, SelectListener<SchoolData[]> listener) {
        ApiControl<SchoolApiData> api = getApiControl("get-schools");
        api.addParameter(Request.Method.GET, "city", cityName);
        api.response(new ApiControl.ApiListener<SchoolApiData>() {
            @Override
            public void onResponse(SchoolApiData api) {
                if (api.getData().isStatus()) listener.onSelect(api.getData().getSchools(), true);
                else listener.onError(api.getData().getMsg());
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
                listener.onError(translator.getMessage());
            }
        });
    }

    public void getSchoolClasses(String school, SelectListener<SchoolClassData[]> listener) {
        ApiControl<SchoolClassApiData> api = schoolClassApi.getApiControl("get-school-class");
        api.addParameter(Request.Method.GET, "name", school);
        api.response(new ApiControl.ApiListener<SchoolClassApiData>() {
            @Override
            public void onResponse(SchoolClassApiData api) {
                if (api.getData().isSuccess()) {
                    listener.onSelect(api.getData().getSchoolClass(), true);
                } else {
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

}
