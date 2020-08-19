package com.futech.our_school.dialog;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.futech.our_school.R;
import com.futech.our_school.utils.ConvertingUtils;

import java.util.Objects;

public class ProgressDialog extends DialogHelper {

    private TextView titleText;

    public ProgressDialog(Activity activity) {
        super(activity);
        LoadingDialogView view = new LoadingDialogView(getContext());
        setContentView(view);

        titleText = findViewById(R.id.dialog_title);

        /*    set dialog size    */

        DisplayMetrics displayMetrics = new DisplayMetrics();
        int height = (int) ConvertingUtils.dipToPixel(getContext(), 250);

        activity.getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels - 100;
        view.setLayoutParams(new FrameLayout.LayoutParams(width, height));

        /*    end dialog size    */

        setCancelable(false);

        setBusAnimation();

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void setBusAnimation() {
        ImageView busImage = findViewById(R.id.bus_loading);
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.bus_motion);
        set.setTarget(busImage);
        set.start();
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);
        titleText.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        titleText.setText(titleId);
    }

    private static class LoadingDialogView extends FrameLayout {
        public LoadingDialogView(Context context) {
            super(context);
            setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            inflate(context, R.layout.dialog_loading, this);
        }
    }

}
