package com.futech.our_school.utils;

import android.content.Context;
import android.graphics.drawable.DrawableWrapper;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class ConvertingUtils {

    public static float dipToPixel(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    private static String integerColorToStringHex(@ColorInt int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static String colorResourceToStringHex(@NonNull Context context, @ColorRes int color) {
        int colorInteger = ResourcesCompat.getColor(context.getResources(), color, context.getTheme());
        return integerColorToStringHex(colorInteger);
    }



}
