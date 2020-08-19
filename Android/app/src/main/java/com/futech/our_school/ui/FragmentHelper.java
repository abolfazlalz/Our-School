/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.ui;

import androidx.fragment.app.Fragment;

import com.futech.our_school.Application;

public class FragmentHelper extends Fragment {

    private String tag;

    public FragmentHelper() {
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void cancel() {
        Application.getRequestQueue().cancelAll(tag);
    }

}
