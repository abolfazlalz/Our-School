/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futech.our_school.R;
import com.futech.our_school.objects.forums.ForumsAnswerData;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.hamsaa.persiandatepicker.util.PersianDateParser;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<ForumsAnswerData> answers;

    private AnswerAdapter(Context context, List<ForumsAnswerData> answers) {
        this.answers = answers;
        this.inflater = LayoutInflater.from(context);
    }

    public static void setAdapter(RecyclerView recyclerView, List<ForumsAnswerData> answers) {
        AnswerAdapter adapter = new AnswerAdapter(recyclerView.getContext(), answers);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_answer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(answers.get(position));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView writerText;
        private TextView answerText;
        private TextView createTimeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            writerText = itemView.findViewById(R.id.writer);
            answerText = itemView.findViewById(R.id.answer_text);
            createTimeText = itemView.findViewById(R.id.create_time);
        }

        private void fillData(ForumsAnswerData data) {
            writerText.setText(data.getWriter().getFullName());
            answerText.setText(data.getAnswer());
            String time = new PersianCalendar(data.getDateCreate().getTime()).getPersianLongDateAndTime();
            createTimeText.setText(time);
        }
    }

}
