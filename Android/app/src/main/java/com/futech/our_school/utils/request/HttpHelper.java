package com.futech.our_school.utils.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.futech.our_school.Application;
import com.futech.our_school.local_database.ImageLocalDatabase;
import com.futech.our_school.utils.VolleyErrorTranslator;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

/**
 * make and get http requests
 *
 * @author Abolfazl Alizadeh
 */
public class HttpHelper {

    private final String tag;
    private RequestQueue requestQueue;
    private String address;
    private static final String TAG = "HttpHelperLog";

    public HttpHelper(String address, String tag) {
        this.tag = tag;
        requestQueue = Application.getRequestQueue();
        this.address = address;
    }

    public static void loadImage(Context context, String url, final ImageView imageView, Bitmap defaultImage, int width, int height, String name) {
        ImageLocalDatabase db = new ImageLocalDatabase(context);
        db.loadImage(Application.getRequestQueue(), url, imageView, defaultImage, width, height,
                name, null);
    }

    private static String paramsMapToString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (!sb.toString().equals("")) {
                    sb.append('&');

                }else {
                    sb.append('?');
                }
                sb.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException ignored) {
        }
        return String.valueOf(sb);
    }

    public void uploadFile(Map<String, Object> imageFile, final Map<String, String> postParams, final Map<String, String> getParams, final HttpListener apiListener) {
        address += HttpHelper.paramsMapToString(getParams);
        VolleyFileUploadRequest request = new VolleyFileUploadRequest(Request.Method.POST, address,
                success -> {
                    try {
                        String json = new String(success.data, HttpHeaderParser.parseCharset(success.headers));
                        apiListener.onResponse(json);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }, apiListener::onError)
        {
            @Override
            protected Map<String, String> getParams() {
                return postParams;
            }

            @Nullable
            @Override
            public Map<String, Object> getByteData() {
                return imageFile;
            }
        };

        requestQueue.add(request);
        requestQueue.start();
    }

    void response(int method, final Map<String, String> postParams, final Map<String, String> getParams, final HttpListener apiListener) {
        try {
            address += HttpHelper.paramsMapToString(getParams);
            StringRequest request = new StringRequest(method, address, apiListener::onResponse,
                    error -> {
                        if (error instanceof NoConnectionError)
                            response(method, postParams, getParams, apiListener);
                        else apiListener.onError(error);
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    return postParams;
                }
            };
            request.setTag(tag);
            requestQueue.add(request);
            requestQueue.start();
        } catch (OutOfMemoryError memoryError) {
            Log.d(TAG,
                    "response: no Enough memory ram, fix it, it's not very good:\nError Message: " + memoryError.getMessage() + "\nLanguage Error" + memoryError.getLocalizedMessage());
        }
    }

    public interface HttpImageDownloaderListener {
        void onDownload(boolean isOnline);

        void onError(String errorMessage);
    }

    public interface HttpListener {
        void onResponse(String response);

        void onError(VolleyError error);
    }
}
