/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.local_database.backup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.futech.our_school.local_database.DatabaseHelper;
import com.futech.our_school.local_database.QueryCreator;
import com.futech.our_school.objects.StudyDurationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.futech.our_school.objects.StudyDurationData.*;

public class StudyHistoryBackup extends DatabaseHelper<StudyDurationData> {

    private static String TABLE_NAME = "studyDuration";

    public StudyHistoryBackup(@Nullable Context context)
    {
        super(context, Backup.DB_NAME, Backup.DB_VERSION, TABLE_NAME);
    }

    @Override
    protected List<StudyDurationData> convertCursorToList(Cursor cursor) {
        List<StudyDurationData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            StudyDurationData data = new StudyDurationData();
            data.setDataByCursor(cursor);
            list.add(data);
        }
        return list;
    }

    @Override
    protected ContentValues convertToContentValues(StudyDurationData value) {
        return value.convertToContentValues();
    }

    @Override
    protected String createTableQuery() {
        Map<String, String> table = new HashMap<>();
        table.put(ID_DB_KEY, QueryCreator.INTEGER_KEY);
        table.put(ID_KEY, QueryCreator.INTEGER_KEY);
        table.put(LESSON_ID_KEY, QueryCreator.INTEGER_KEY);
        table.put(START_TIME_KEY, QueryCreator.STRING_KEY);
        table.put(END_TIME_KEY, QueryCreator.STRING_KEY);
        return QueryCreator.getCreateDatabaseQuery(TABLE_NAME, table, ID_DB_KEY);
    }
}
