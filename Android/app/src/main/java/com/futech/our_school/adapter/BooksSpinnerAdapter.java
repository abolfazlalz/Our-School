/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.futech.our_school.R;
import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.request.lesson.LessonBookControl;
import com.futech.our_school.utils.request.HttpHelper;
import com.futech.our_school.utils.request.listener.SelectListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BooksSpinnerAdapter extends ArrayAdapter<LessonBookData> implements SpinnerAdapter {

    private LayoutInflater inflater;
    @NonNull
    private final Context context;
    private final List<LessonBookData> books;

    public BooksSpinnerAdapter(@NonNull Context context, List<LessonBookData> books) {
        super(context, R.layout.layout_books_spinner);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.books = books;
    }

    public static LessonBookData getBookData(Spinner spinner) {
        if (spinner.getAdapter() instanceof BooksSpinnerAdapter) {
            return (LessonBookData) spinner.getSelectedItem();
        }
        return null;
    }

    public static void setAllBookAdapter(Context context, Spinner spinner) {
        LessonBookControl lesson = new LessonBookControl(context);
        lesson.getBooks(new SelectListener<List<LessonBookData>>() {
            @Override
            public void onSelect(List<LessonBookData> data, boolean isOnline) {
                spinner.setAdapter(new BooksSpinnerAdapter(context, data));
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        convertView = new View(context);
        return getView(position, convertView, parent);
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        View view = convertView;
        LessonBookData lessonBook = books.get(position);
        if (lessonBook != null) {
            view = inflater.inflate(R.layout.layout_books_spinner, parent, false);
            ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            holder.setTitle(lessonBook.getTitle());
            holder.setLessonImage(lessonBook.getPhoto(), lessonBook.getTitle());
        }
        return view;
    }

    @Override
    public int getPosition(@Nullable LessonBookData item) {
        return books.indexOf(item);
    }

    public int getPosition(int id) {
        for (LessonBookData data : books) {
            if (data.getId() == id) return getPosition(data);
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return books.get(position).getId();
    }

    @Nullable
    @Override
    public LessonBookData getItem(int position) {
        return books.get(position);
    }

    class ViewHolder {
        TextView lessonTitle;
        ImageView lessonImage;

        public ViewHolder(View view) {
            lessonTitle = view.findViewById(R.id.book_title);
            lessonImage = view.findViewById(R.id.book_image);
        }

        void setTitle(String title) {
            lessonTitle.setText(title);
        }

        void setLessonImage(String image, String title) {
            HttpHelper.loadImage(BooksSpinnerAdapter.this.context, image, lessonImage, null, 20, 20,
                    title);
        }
    }

}
