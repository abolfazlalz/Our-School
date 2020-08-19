package com.futech.our_school.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import com.futech.our_school.R;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SchoolToast extends Toast {

    @Retention(SOURCE)
    @IntDef({SchoolToast.LENGTH_SHORT, SchoolToast.LENGTH_LONG})
    @interface Duration {}

    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    @Retention(SOURCE)
    @IntDef({SchoolToast.FAIL_TOAST_TYPE, SchoolToast.SUCCESS_TOAST_TYPE, SchoolToast.INFORMATION_TOAST_TYPE})
    @interface ToastType {}

    private Context context;
    private TextView toastTextView;

    public static final int FAIL_TOAST_TYPE = 0;
    public static final int SUCCESS_TOAST_TYPE = 1;
    public static final int INFORMATION_TOAST_TYPE = 2;

    private int toastType;
    private String toastText;

    private SchoolToast(Context context) {
        super(context);
        this.context = context;
        setView(getView());
    }


    @Override
    public View getView() {
        return View.inflate(getContext(), R.layout.toast_view, null);
    }

    public void setText(@StringRes int resId) {
        toastTextView.setText(resId);
        toastText = toastTextView.getText().toString();
    }

    public void setText(CharSequence s) {
        if (s == null) return;
        toastTextView.setText(s);
        toastText = s.toString();
    }

    private void prepareView(View view, @DrawableRes int icon, @ColorRes int color) {
        ImageView image = view.findViewById(R.id.toastIcon);
        toastTextView = view.findViewById(R.id.toastText);
        view.setBackgroundResource(R.drawable.toast_background);
        GradientDrawable gd = (GradientDrawable) view.getBackground();
        int colorInt = ResourcesCompat.getColor(getContext().getResources(), color,
                getContext().getTheme());
        gd.setStroke((int) ConvertingUtils.dipToPixel(getContext(), 2), colorInt);
        toastTextView.setText(toastText);
        toastTextView.setTextColor(colorInt);
        image.setImageResource(icon);
    }

    private Context getContext() {
        return context;
    }

    private void setToastType(@SchoolToast.ToastType int toastType) {
        this.toastType = toastType;

        View view = getView();

        if (this.toastType == FAIL_TOAST_TYPE) {
            prepareView(view, R.drawable.ic_alert_fail, R.color.colorFailToast);
        }else if (this.toastType == INFORMATION_TOAST_TYPE) {
            prepareView(view, R.drawable.ic_alert_information, R.color.colorInformationToast);
        }else {
            prepareView(view, R.drawable.ic_alert_success, R.color.colorSuccessToast);
        }
        setView(view);
    }

    private static SchoolToast makeText(Context context, CharSequence text,
                                        @SchoolToast.Duration int duration,
                                        @ToastType int toastType)
    {
        SchoolToast result = new SchoolToast(context);
        result.setToastType(toastType);
        result.setText(text);
        result.setDuration(duration);
        return result;
    }

    public static SchoolToast makeText(Context context, CharSequence text,
                                       @SchoolToast.Duration int duration)
    {
        return makeText(context, text, duration, SUCCESS_TOAST_TYPE);
    }

    private static SchoolToast makeText(Context context,
                                        @StringRes int strId,
                                        @SchoolToast.Duration int duration,
                                        @ToastType int toastType)
    {
        SchoolToast toast = makeText(context, "", duration, toastType);
        toast.setText(strId);
        return toast;
    }

    public static SchoolToast makeFail(Context context, String text, @Duration int duration) {
        return makeText(context, text, duration, FAIL_TOAST_TYPE);
    }

    public static SchoolToast makeFail(Context context,
                                       @StringRes int strId, @Duration int duration)
    {
        return makeText(context, strId, duration, FAIL_TOAST_TYPE);
    }

    public static SchoolToast makeSuccess(Context context, String text, @Duration int duration) {
        return makeText(context, text, duration, SUCCESS_TOAST_TYPE);
    }

    public static SchoolToast makeSuccess(Context context,
                                          @StringRes int strId, @Duration int duration)
    {
        return makeText(context, strId, duration, SUCCESS_TOAST_TYPE);
    }

    public static SchoolToast makeInformation(Context context, String text, @Duration int duration)
    {
        return makeText(context, text, duration, INFORMATION_TOAST_TYPE);
    }

    public static SchoolToast makeInformation(Context context,
                                              @StringRes int strId, @Duration int duration)
    {
        return makeText(context, strId, duration, INFORMATION_TOAST_TYPE);
    }

    public static SchoolToast makeText(Context context,
                                       @StringRes int strId, @SchoolToast.Duration int duration)
    {
        return makeText(context, strId, duration, SUCCESS_TOAST_TYPE);
    }


}
