package com.futech.our_school.objects;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class StudyDurationData extends DatabaseObjectHelper {

    public static final String ID_KEY = "id";
    public static final String ID_DB_KEY = "id_db";
    public static final String START_TIME_KEY = "startTime";
    public static final String END_TIME_KEY = "endTime";
    public static final String LESSON_ID_KEY = "lessonKey";

    String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";
//    public static final String

    private int id;
    private Date startTime;
    private Date endTime;
    private LessonBookData book;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public LessonBookData getBook() {
        return book;
    }

    public void setBook(LessonBookData book) {
        this.book = book;
    }

    @Override
    public ContentValues convertToContentValues() {
        ContentValues val = new ContentValues();
        SimpleDateFormat format = new SimpleDateFormat(dateFormatPattern, Locale.ENGLISH);
        val.put(ID_KEY, getId());
        val.put(START_TIME_KEY, format.format(getStartTime()));
        val.put(END_TIME_KEY, format.format(getEndTime()));
        return val;
    }

    @Override
    public void setDataByCursor(Cursor cursor) {
        LessonBookData lesson = new LessonBookData();
        lesson.setId(cursor.getInt(cursor.getColumnIndexOrThrow(LESSON_ID_KEY)));
        setId(cursor.getInt(cursor.getColumnIndexOrThrow(LESSON_ID_KEY)));
        SimpleDateFormat format = new SimpleDateFormat(dateFormatPattern, Locale.ENGLISH);
        try {
            setStartTime(format.parse(cursor.getString(cursor.getColumnIndexOrThrow(START_TIME_KEY))));
        } catch (ParseException ignore) {
        }
        try {
            setEndTime(format.parse(cursor.getString(cursor.getColumnIndexOrThrow(END_TIME_KEY))));
        } catch (ParseException ignore) {
        }
    }

    @Override
    public Map.Entry<String, Integer> getPrimaryId() {
        return new AbstractMap.SimpleEntry<>(ID_KEY, getId());
    }
}
