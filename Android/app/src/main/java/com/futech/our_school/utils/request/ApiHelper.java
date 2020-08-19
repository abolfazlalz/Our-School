package com.futech.our_school.utils.request;

import android.content.Context;

import com.futech.our_school.Application;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApiHelper<T> {

    private Context context;
    private String apiAddress;
    private final Type type;
    private String tag;

    public ApiHelper(Context context, String apiAddress, Type type) {
        this.context = context;
        this.apiAddress = apiAddress;
        this.type = type;
        this.tag = "ApiHelper";
    }

    protected Context getContext() {
        return this.context;
    }

    private Type getType() {
        return type;
    }

    public ApiControl<T> getApiControl(String requestName) {
        return new ApiControl<T>(getContext(), apiAddress, getType(),
                tag).setRequest(requestName);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void cancel() {
        Application.getRequestQueue().cancelAll(tag);
    }

}
