/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class TourHelper {

    private static final String PREF_NAME = "tour_pref";

    public static class HomeFragmentTour {
        public final static String STUDY_NOW = "home_1";
        public final static String FORUMS = "home_2";
    }

    public static class StudyDialogTour {
        public static final String CHOICE_BOOK = "study_1";
        public static final String START_STUDY = "study_2";
    }

    public static class ForumsTour {
        public static final String ADD_QUESTION = "forums_1";
        public static final String ADD_ANSWER = "forums_2";

        public static String[] getToursStringList() {
            return new String[]{ADD_QUESTION, ADD_ANSWER};
        }
    }


    @Retention(SOURCE)
    @StringDef(
            {StudyDialogTour.CHOICE_BOOK, StudyDialogTour.START_STUDY, HomeFragmentTour.STUDY_NOW, HomeFragmentTour.FORUMS,
                    ForumsTour.ADD_QUESTION, ForumsTour.ADD_ANSWER})
    @interface ToursList {}

    private Activity activity;
    private TourGuide mTourGuideHandler;
    private String request;
    private boolean isOpen;

    private TourHelper(Activity activity, @ToursList String request) {
        this.activity = activity;
        this.request = request;
    }

    private Activity getActivity() {
        return this.activity;
    }

    private String getRequest() {
        return request;
    }

    public boolean isCompleted() {
        return getActivity().getSharedPreferences(TourHelper.PREF_NAME,
                Context.MODE_PRIVATE).getBoolean(getRequest(), false);
    }

    public TourHelper showTour(View view, String title, String description) {
        mTourGuideHandler = TourGuide.init(activity).with(TourGuide.Technique.Click).setPointer(
                new Pointer()).setToolTip(
                new ToolTip().setTitle(title).setDescription(description)).setOverlay(
                new Overlay()).playOn(view);
        this.isOpen = true;
        return this;
    }

    public boolean isTourOpen() {
        return this.isOpen;
    }

    public void closeTour() {
        if (mTourGuideHandler != null) mTourGuideHandler.cleanUp();
        getActivity().getSharedPreferences(TourHelper.PREF_NAME,
                Context.MODE_PRIVATE).edit().putBoolean(request, true).apply();
    }

    public static class showTour {
        public static TourHelper Uncompleted(Activity activity, View view, String title, String description,
                                             @ToursList String request)
        {
            TourHelper tour = new TourHelper(activity, request);
            if (!tour.isCompleted()) {
                return tour.showTour(view, title, description);
            }
            return tour;
        }
    }

    public static class CreateTour extends TourHelper {
        private View view;
        private String title;
        private String description;

        private CreateTour(Activity activity, View view, String title, String description, String request) {
            super(activity, request);
            this.view = view;
            this.title = title;
            this.description = description;
        }

        public static CreateTour uncompleted(Activity activity, View view, String title, String description,
                                             @ToursList String request)
        {
            return new CreateTour(activity, view, title, description, request);
        }

        public CreateTour showTour() {
            showTour(view, title, description);
            return this;
        }
    }

}
