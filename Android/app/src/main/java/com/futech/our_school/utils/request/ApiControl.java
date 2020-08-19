package com.futech.our_school.utils.request;

import android.content.Context;
import android.util.Log;

import androidx.annotation.IntDef;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.annotation.Retention;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ApiControl<T> {


    @Retention(SOURCE)
    @IntDef({Request.Method.POST, Request.Method.GET})
    @interface RequestMethod {}

    private final Map<String, String> postParams;
    private final Map<String, String> getParams;
    private final HttpHelper httpHelper;
    private Type type;
    private final String TAG = "ApiControl";
    private Context context;

    public ApiControl(Context context, String address, Type type, String tag) {
        this.type = type;
        httpHelper = new HttpHelper(address, tag);
        this.context = context;
        postParams = new HashMap<>();
        getParams = new HashMap<>();
        addParameter(Request.Method.GET, "token", context.getString(R.string.api_token));
    }

    public ApiControl<T> addParameter(@RequestMethod int method, String key, String value) {
        if (method == Request.Method.GET) this.getParams.put(key, value);
        else this.postParams.put(key, value);
        return this;
    }

    public ApiControl<T> setRequest(String requestName) {
        this.getParams.put("request", requestName);
        return this;
    }

    public void uploadFile(Map<String, Object> imageFile, final ApiListener<T> listener) {
        httpHelper.uploadFile(imageFile, this.getParams, this.getParams, responseHandler(listener));
    }

    public void response(final ApiListener<T> listener) {
        httpHelper.response(Request.Method.POST, this.postParams, this.getParams, responseHandler(listener));
    }

    private HttpHelper.HttpListener responseHandler(ApiListener<T> listener) {
        return new HttpHelper.HttpListener() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new GsonBuilder().setDateFormat(
                            context.getString(R.string.datetime_db_format)).create();
                    T t = gson.fromJson(response, type);
                    listener.onResponse(t);
                } catch (JsonSyntaxException ex) {
                    Log.d(TAG, "onResponse: " + response);
                    Log.d(TAG, "onResponse: JsonIOException: " + ex.getMessage());
                    Log.d(TAG,
                            "onResponse: can't parsing json, because syntax of json response is incorrect");
                    listener.onError(new ParseError());
                } catch (JsonParseException ex) {
                    Log.d(TAG, "onResponse: " + response);
                    Log.d(TAG, "onResponse: onError: " + ex.getMessage());
                    listener.onError(new ParseError());
                }
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(error);
            }
        };
    }

    public interface ApiListener<T> {
        void onResponse(T api);

        void onError(VolleyError error);
    }

}
