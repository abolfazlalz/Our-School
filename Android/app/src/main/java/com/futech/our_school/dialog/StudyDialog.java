/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.futech.our_school.ActivityHelper;
import com.futech.our_school.MainActivity;
import com.futech.our_school.R;
import com.futech.our_school.adapter.BooksSpinnerAdapter;
import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.request.lesson.LessonBookControl;
import com.futech.our_school.request.schedules.StudyHistoryControl;
import com.futech.our_school.utils.SchoolToast;
import com.futech.our_school.utils.request.listener.DataChangeListener;
import com.futech.our_school.utils.request.listener.SelectListener;

import org.joda.time.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class StudyDialog extends DialogHelper implements View.OnClickListener, DataChangeListener {

    private ProgressBar progressBar;
    private TextView timeLeftTextView;
    private Spinner lessonSelector;

    private CounterTimeTimer timerTask;
    private Timer timer;

    private Button startButton;
    private Button endButton;

    public static final String START_TIMER_TAG = "start-timer";
    public static final String LESSON_TIMER_TAG = "lesson-timer";
    public static final String PREF_NAME = "study-timer";
    private int lessonId;

    public StudyDialog(ActivityHelper activity) {
        super(activity);
        StudyViewItem view = StudyViewItem.newInstance(activity);
        setContentView(view);

        progressBar = findViewById(R.id.progress_circular);
        timeLeftTextView = findViewById(R.id.time_left);

        lessonSelector = view.findViewById(R.id.lesson_select);
        startButton = view.findViewById(R.id.start_study_button);
        endButton = view.findViewById(R.id.end_study_button);

        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        if (!preferences.getString(START_TIMER_TAG, "").equals("")) studyStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.requestLayout();

        LessonBookControl ctrl = new LessonBookControl(getContext());
        ctrl.getBooks(new SelectListener<List<LessonBookData>>() {
            @Override
            public void onSelect(List<LessonBookData> data, boolean isOnline) {
                BooksSpinnerAdapter booksSpinnerAdapter = new BooksSpinnerAdapter(getContext(),
                        data);
                lessonSelector.setAdapter(booksSpinnerAdapter);
                int id = preferences.getInt(LESSON_TIMER_TAG, StudyDialog.this.lessonId);
                if (id > -1) {
                    lessonSelector.setSelection(booksSpinnerAdapter.getPosition(id));
                }
            }

            @Override
            public void onError(String msg) {

            }
        });

        startButton.setOnClickListener(this);
        endButton.setOnClickListener(this);

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setBook(int bookId) {
        this.lessonId = bookId;
        for (int i = 0; i < lessonSelector.getCount(); i++) {
            LessonBookData itemAtPosition = (LessonBookData) lessonSelector.getItemAtPosition(i);
            if (itemAtPosition.getId() == bookId) {
                lessonSelector.setSelection(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_study_button) {
            studyStart();
        }else if (v.getId() == R.id.end_study_button) {
            studyEnd();
        }
    }

    @Override
    public void onChange() {
        SchoolToast.makeSuccess(getContext(), R.string.study_save_data_message,
                SchoolToast.LENGTH_SHORT);
    }

    @Override
    public void onError(String msg) {
        SchoolToast.makeFail(getContext(), msg, SchoolToast.LENGTH_LONG).show();
    }

    public static boolean isStudyTime(Context context) {
        return !context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                START_TIMER_TAG, "").equals("");
    }

    private void studyStart() {
        String format = getActivity().getString(R.string.datetime_db_format);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date();

        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        if (!isStudyTime(getContext())) {
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.putString(START_TIMER_TAG, dateFormat.format(date));
            Object selectedItem = lessonSelector.getSelectedItem();
            LessonBookData lesson = (LessonBookData) selectedItem;
            prefEditor.putInt(LESSON_TIMER_TAG, lesson.getId());
            prefEditor.apply();
        }

        timerTask = new CounterTimeTimer();
        timer = new Timer();
        timer.schedule(timerTask, 0, 500);
        lessonSelector.setEnabled(false);
        endButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).showStudyMenu(true);
    }

    private void studyEnd() {
        String format = getActivity().getString(R.string.datetime_db_format);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        lessonSelector.setEnabled(true);
        timerTask.cancel();
        timer.cancel();
        timer.purge();
        endButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).showStudyMenu(false);

        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        String dateStr = preferences.getString(START_TIMER_TAG, "");
        Date date;
        if (!isStudyTime(getContext())) date = new Date();
        else {
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                date = new Date();
            }
        }
        int lessonId = preferences.getInt(LESSON_TIMER_TAG, -1);
        StudyHistoryControl ctrl = new StudyHistoryControl(getContext());
        ctrl.addStudyTime(lessonId, date, new Date(), this);

        preferences.edit().clear().apply();
    }

    private static class StudyViewItem extends FrameLayout {
        public StudyViewItem(Context context) {
            super(context);
            setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            inflate(context, R.layout.dialog_study, this);
        }

        public static StudyViewItem newInstance(Context context) {
            return new StudyViewItem(context);
        }
    }

    private class CounterTimeTimer extends TimerTask {

        private Date startDate;

        CounterTimeTimer() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    getActivity().getString(R.string.datetime_db_format), Locale.getDefault());
            try {
                startDate = dateFormat.parse(getActivity().getSharedPreferences(PREF_NAME,
                        Context.MODE_PRIVATE).getString(START_TIMER_TAG, ""));
            } catch (ParseException e) {
                startDate = new Date();
            }
        }

        @Override
        public void run() {
            getActivity().runOnUiThread(() -> {
                Interval interval = new Interval(startDate.getTime(), new Date().getTime());
                int seconds = interval.toPeriod().getSeconds();
                int minutes = interval.toPeriod().getMinutes();
                int hours = interval.toPeriod().getHours();
                progressBar.setProgress(seconds);
                timeLeftTextView.setText(String.format(Locale.getDefault(), "%02d", hours));
                timeLeftTextView.append(":");
                timeLeftTextView.append(String.format(Locale.getDefault(), "%02d", minutes));
                timeLeftTextView.append(":");
                timeLeftTextView.append(String.format(Locale.getDefault(), "%02d", seconds));
            });
        }
    }
}
