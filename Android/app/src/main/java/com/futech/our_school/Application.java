package com.futech.our_school;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.futech.our_school.local_database.ImageLocalDatabase;

public class Application extends android.app.Application {

    public static final String TAG = "ApplicationLog";

    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Application has started");
        requestQueue = Volley.newRequestQueue(this);
        ImageLocalDatabase imageDb = new ImageLocalDatabase(this);
        // Remove all image from catch after 12 hours
        imageDb.deleteUnusedImages(12);
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

}
