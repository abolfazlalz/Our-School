package com.futech.our_school.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LessonBookData extends DatabaseObjectHelper implements Serializable {

    public static final String ID_KEY = "id";
    public static final String ID_DB_KEY = "id_db";
    public static final String TITLE_KEY = "title";
    public static final String FILE_PATH_KEY = "filePath";
    public static final String PHOTO_KEY = "photo";

    private int id;
    private String title;
    private String filePath;
    private LessonData lesson;
    private String photo;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public LessonData getLesson() {
        return lesson;
    }

    public String getPhoto() {
        return photo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setLesson(LessonData lesson) {
        this.lesson = lesson;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID_KEY, getId());
        values.put(TITLE_KEY, getTitle());
        values.put(FILE_PATH_KEY, getFilePath());
        values.put(PHOTO_KEY, getPhoto());
        return values;
    }

    @Override
    public void setDataByCursor(Cursor cursor) {
        setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_KEY)));
        setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE_KEY)));
        setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(FILE_PATH_KEY)));
        setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(PHOTO_KEY)));
    }

    @Override
    public Map.Entry<String, Integer> getPrimaryId() {
        return new AbstractMap.SimpleEntry<>(ID_KEY, getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LessonBookData that = (LessonBookData) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return id == that.id && Objects.equals(title, that.title) && Objects.equals(filePath,
                    that.filePath) && Objects.equals(lesson, that.lesson) && Objects.equals(photo,
                    that.photo);
        }else {
            return id == that.id && title.equals(that.title) && filePath.equals(
                    that.filePath) && lesson.getId() == that.lesson.getId() && photo.equals(
                    that.photo);
        }
    }
}
