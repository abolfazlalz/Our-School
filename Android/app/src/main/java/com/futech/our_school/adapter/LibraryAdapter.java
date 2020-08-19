package com.futech.our_school.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futech.our_school.R;
import com.futech.our_school.activities.BookActivity;
import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.utils.request.HttpHelper;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private final List<LessonBookData> books;
    private int width;

    private LibraryAdapter(Context context, List<LessonBookData> books, int width) {
        inflater = LayoutInflater.from(context);
        this.books = books;
        this.width = width;
    }

    public static void setAdapter(RecyclerView recyclerView, List<LessonBookData> books) {
        Context context = recyclerView.getContext();
        LibraryAdapter adapter = new LibraryAdapter(context, books, recyclerView.getWidth());
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(layoutManager);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_book_item, parent, false);
        view.getLayoutParams().width = (width / 3) - 2;
        view.requestLayout();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        private TextView bookTitle;
        private ImageView bookImage;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookImage = itemView.findViewById(R.id.book_image);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);
            itemView.setHovered(true);
        }

        public void fillData(int position) {
            this.position = position;
            LessonBookData book = books.get(position);
            bookTitle.setText(book.getTitle());
            HttpHelper.loadImage(itemView.getContext(), book.getPhoto(), bookImage, null, 200, 200,
                    book.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), BookActivity.class);
            LessonBookData book = books.get(position);
            intent.putExtra(BookActivity.BOOK_ID, book);
            v.getContext().startActivity(intent);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float scaleClick = 0.9f;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setScaleX(scaleClick);
                v.setScaleY(scaleClick);
                return false;
            }else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                return false;
            }else {
                v.setScaleX(1);
                v.setScaleY(1);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) return v.performClick();
            return false;
        }
    }

}
