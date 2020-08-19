/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futech.our_school.R;
import com.futech.our_school.activities.QuestionActivity;
import com.futech.our_school.objects.forums.ForumsQuestionData;
import com.futech.our_school.utils.ConvertingUtils;

import java.util.List;

public class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private final List<ForumsQuestionData> data;
    private boolean isFullWidth;

    private ForumsAdapter(Context context, List<ForumsQuestionData> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public static void setAdapter(RecyclerView recyclerView, List<ForumsQuestionData> data) {
        setAdapter(recyclerView, data, RecyclerView.HORIZONTAL);
    }

    public static void setAdapterLinear(RecyclerView recyclerView, List<ForumsQuestionData> data) {
        setAdapter(recyclerView, data, RecyclerView.VERTICAL).isFullWidth = true;
    }

    public static ForumsAdapter setAdapter(RecyclerView recyclerView, List<ForumsQuestionData> data, int orientation) {
        Context context = recyclerView.getContext();
        ForumsAdapter adapter = new ForumsAdapter(context, data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(orientation);
        recyclerView.setLayoutManager(layoutManager);
        return adapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_forums_mini_item, parent, false);
        if (isFullWidth) {
            int height = (int) view.getContext().getResources().getDimension(R.dimen.forums_height);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(position);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView questionTitle;
        private TextView questionText;
        private TextView questionUsername;
        private TextView questionLesson;
        private int id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTitle = itemView.findViewById(R.id.question_title);
            questionText = itemView.findViewById(R.id.question_text);
            questionUsername = itemView.findViewById(R.id.question_username);
            questionLesson = itemView.findViewById(R.id.question_lesson);
            itemView.setOnClickListener(this);
        }

        void fillData(int position) {
            ForumsQuestionData data = ForumsAdapter.this.data.get(position);
            this.id = data.getId();
            questionTitle.setText(data.getTitle());
            questionText.setText(data.getBody());
            questionUsername.setText(data.getWriter().getFullName());
            questionLesson.setText(data.getLesson().getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), QuestionActivity.class);
            intent.putExtra("id", id);
            v.getContext().startActivity(intent);
        }
    }

}
