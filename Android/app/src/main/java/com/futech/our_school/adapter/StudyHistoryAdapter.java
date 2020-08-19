package com.futech.our_school.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futech.our_school.R;
import com.futech.our_school.objects.StudyDurationData;
import com.futech.our_school.request.schedules.StudyHistoryControl;
import com.futech.our_school.utils.SchoolToast;
import com.futech.our_school.utils.request.listener.DataChangeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class StudyHistoryAdapter extends RecyclerView.Adapter<StudyHistoryAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<StudyDurationData> studyHistory;
    private int width = -1;


    private StudyHistoryAdapter(Context context, List<StudyDurationData> studyHistory) {
        this.studyHistory = studyHistory;
        inflater = LayoutInflater.from(context);
    }

    public static void setAdapter(RecyclerView recyclerView, List<StudyDurationData> data) {
        StudyHistoryAdapter adapter = new StudyHistoryAdapter(recyclerView.getContext(), data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(recyclerView.getContext());
        layout.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layout);
    }

    public static void setGridAdapter(RecyclerView recyclerView, List<StudyDurationData> data) {
        StudyHistoryAdapter adapter = new StudyHistoryAdapter(recyclerView.getContext(), data);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), 3);
        adapter.width = recyclerView.getWidth();
        recyclerView.setLayoutManager(layoutManager);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_study_item, parent, false);
        if (width > 0) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width / 3;
            view.setLayoutParams(layoutParams);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(studyHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return studyHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener, DataChangeListener {

        private TextView lessonTitle;
        private TextView studyDate;
        private TextView studyDuration;

        private StudyDurationData data;

        ViewHolder(@NonNull View view) {
            super(view);
            lessonTitle = view.findViewById(R.id.lesson_name);
            studyDate = view.findViewById(R.id.study_date);
            studyDuration = view.findViewById(R.id.study_duration);
            view.setOnCreateContextMenuListener(this);
        }

        void fillData(StudyDurationData data) {
            this.data = data;

            lessonTitle.setText(data.getBook().getTitle());
            if (dateIsToday(data.getStartTime())) {
                studyDate.setText(R.string.today_text);
            }else {
                Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
                calendar.setTime(data.getStartTime());
                if (todayCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(
                        Calendar.DAY_OF_MONTH) < 4) {
                    int days = calendar.get(Calendar.DAY_OF_MONTH);
                    int today = todayCalendar.get(Calendar.DAY_OF_MONTH);
                    studyDate.setText(String.format(Locale.getDefault(),
                            itemView.getContext().getString(R.string.days_ago), today - days));
                }else {
                    PersianCalendar perCalendar = new PersianCalendar(
                            data.getStartTime().getTime());
                    String text = perCalendar.getPersianDay() + perCalendar.getPersianMonthName();
                    studyDate.setText(text);
                }
            }

            long d = data.getEndTime().getTime() - data.getStartTime().getTime();
            long minutes = TimeUnit.MINUTES.convert(d, TimeUnit.MILLISECONDS);
            if (minutes < 1) {
                studyDuration.setText(R.string.less_than_minute);
            }else if (minutes < 60) {
                studyDuration.setText(
                        String.format(itemView.getContext().getString(R.string.duration_minutes),
                                minutes));
            }else if (minutes % 60 == 0) {
                studyDuration.setText(
                        String.format(itemView.getContext().getString(R.string.duration_hours),
                                minutes / 60));
            }else {
                long hours = minutes / 60;
                minutes = minutes % 60;
                studyDuration.setText(String.format(
                        itemView.getContext().getString(R.string.duration_hours_minutes), hours,
                        minutes));
            }
        }

        private boolean dateIsToday(Date date) {
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return format.format(date).equals(format.format(today));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater f = new MenuInflater(v.getContext());
            f.inflate(R.menu.study_item_menu, menu);
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.delete_item) {
                StudyHistoryAdapter.this.studyHistory.remove(data);
                StudyHistoryAdapter.this.notifyItemRemoved(
                        StudyHistoryAdapter.this.studyHistory.indexOf(data));
                StudyHistoryControl ctrl = new StudyHistoryControl(itemView.getContext());
                ctrl.deleteStudyTime(data.getId(), this);
                return true;
            }
            return false;
        }

        @Override
        public void onChange() {

        }

        @Override
        public void onError(String msg) {
            SchoolToast.makeFail(itemView.getContext(), msg, SchoolToast.LENGTH_LONG);
        }
    }

}
