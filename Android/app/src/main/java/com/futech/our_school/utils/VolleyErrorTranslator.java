package com.futech.our_school.utils;

import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.futech.our_school.R;

public class VolleyErrorTranslator {

    private final VolleyError error;
    private final Context context;

    public VolleyErrorTranslator(VolleyError error, Context context) {
        this.error = error;
        this.context = context;
    }

    public String getMessage() {
        String msg = "";
        if (error instanceof AuthFailureError) {
            msg = context.getString(R.string.volley_auth_error);
        }else if (error instanceof ClientError) {
            msg = context.getString(R.string.volley_client_error);
        } else if (error instanceof NoConnectionError) {
            msg = context.getString(R.string.volley_no_connection_error);
        }else if (error instanceof NetworkError) {
            msg = context.getString(R.string.volley_network_error);
        }else if (error instanceof ParseError) {
            msg = context.getString(R.string.volley_parse_error);
        }else if (error instanceof ServerError) {
            msg =context.getString(R.string.volley_server_error);
        }else if (error instanceof TimeoutError) {
            msg =context.getString(R.string.volley_timeout_error);
        }
        return msg;
    }

}
