package com.futech.our_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.futech.our_school.R;
import com.futech.our_school.objects.ClassData;

import java.util.List;

public class ClassMiniAdapter extends Adapter<ClassMiniAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<ClassData> classes;

    private ClassMiniAdapter(Context context, List<ClassData> classes) {
        inflater = LayoutInflater.from(context);
        this.classes = classes;
    }

    public static void setAdapter(RecyclerView recyclerView, List<ClassData> classes) {
        Context context = recyclerView.getContext();
        recyclerView.setAdapter(new ClassMiniAdapter(context, classes));
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layout);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_class_small_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(classes.get(position));
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView classTitleText;
        private TextView classTimeText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            classTimeText = itemView.findViewById(R.id.lesson_time);
            classTitleText = itemView.findViewById(R.id.lesson_title);
        }

        void fillData(ClassData classData) {
            classTitleText.setText(classData.getDescription());
            String startTime = classData.getStartTime().format("h:m");
            String endTime = classData.getEndTime().format("h:m");
            String times = String.format(itemView.getContext().getString(R.string.duration_class), startTime, endTime);
            classTimeText.setText(times);
        }
    }

}
