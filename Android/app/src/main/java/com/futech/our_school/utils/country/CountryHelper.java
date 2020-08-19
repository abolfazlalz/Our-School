package com.futech.our_school.utils.country;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.listener.SelectListener;

public class CountryHelper {

    private final Context context;
    private ApiControl<CountryApi> api;
    private final static String TAG = "country";

    public CountryHelper(Context context) {
        this.context = context;
        api = new ApiControl<>(context, "https://www.api.abolfazlalz.ir/country/api.php",
                CountryApi.class, TAG);
    }

    public void getStates(SelectListener<StateData[]> states) {
        api.addParameter(Request.Method.GET, "request", "get-states");
        api.addParameter(Request.Method.GET, "count", "all");
        api.response(new ApiControl.ApiListener<CountryApi>() {
            @Override
            public void onResponse(CountryApi api) {
                if (api != null && api.getData().isStatus() && api.getData().getStates() != null)
                    states.onSelect(api.getData().getStates(), true);
            }

            @Override
            public void onError(VolleyError error) {
                states.onError(new VolleyErrorTranslator(error, context).getMessage());
            }
        });
    }

    public void getCities(SelectListener<CityData[]> cities, String state) {
        api.addParameter(Request.Method.GET, "request", "get-cities");
        api.addParameter(Request.Method.GET, "count", "all");
        api.addParameter(Request.Method.GET, "state", state);
        api.response(new ApiControl.ApiListener<CountryApi>() {
            @Override
            public void onResponse(CountryApi api) {
                if (api.getData().isStatus() && api.getData().getCities() != null) {
                    cities.onSelect(api.getData().getCities(), true);
                }
            }

            @Override
            public void onError(VolleyError error) {
                cities.onError(new VolleyErrorTranslator(error, context).getMessage());
            }
        });
    }

}
