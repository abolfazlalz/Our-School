package com.futech.our_school.local_database.lessons_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.futech.our_school.local_database.DatabaseHelper;
import com.futech.our_school.local_database.QueryCreator;
import com.futech.our_school.objects.LessonBookData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.futech.our_school.local_database.QueryCreator.*;
import static com.futech.our_school.objects.LessonBookData.*;

public class LibraryLocalDatabase extends DatabaseHelper<LessonBookData> {

    private static final String TABLE_NAME = "library";

    public LibraryLocalDatabase(@Nullable Context context)
    {
        super(context, LessonsDatabase.LESSONS_DB_NAME, LessonsDatabase.LESSONS_DB_VERSION,
                TABLE_NAME);
    }

    @Override
    protected List<LessonBookData> convertCursorToList(Cursor cursor) {
        List<LessonBookData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            LessonBookData data = new LessonBookData();
            data.setDataByCursor(cursor);
            list.add(data);
        }
        return list;
    }

    @Override
    protected ContentValues convertToContentValues(LessonBookData value) {
        return value.convertToContentValues();
    }

    @Override
    protected String createTableQuery() {
        return QueryCreator.getCreateDatabaseQuery(TABLE_NAME, needleColumns(), ID_DB_KEY);
    }

    static Map<String, String> needleColumns() {
        Map<String, String> columns = new HashMap<>();
        columns.put(ID_KEY, INTEGER_KEY);
        columns.put(ID_DB_KEY, INTEGER_KEY);
        columns.put(TITLE_KEY, STRING_KEY);
        columns.put(FILE_PATH_KEY, STRING_KEY);
        columns.put(PHOTO_KEY, STRING_KEY);
        return columns;
    }
}
