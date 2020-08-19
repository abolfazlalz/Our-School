package com.futech.our_school.dialog;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;

import androidx.annotation.StringRes;

public class DialogHelper extends Dialog {

    private final Activity activity;

    public DialogHelper(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public static DialogHelper showDialog(Activity activity, String title) {
        DialogHelper dialog = new DialogHelper(activity);
        dialog.setTitle(title);
        return dialog;
    }

    public static DialogHelper showDialog(Activity activity, @StringRes int titleId) {
        DialogHelper dialog = new DialogHelper(activity);
        dialog.setTitle(titleId);
        return dialog;
    }

    public DialogHelper setDialogTitle(String title) {
        setTitle(title);
        return this;
    }

    public DialogHelper setDialogTitle(@StringRes int titleId) {
        setTitle(titleId);
        return this;
    }

    public Activity getActivity() {
        return activity;
    }
}
