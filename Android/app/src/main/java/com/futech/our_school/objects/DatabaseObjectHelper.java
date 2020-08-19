package com.futech.our_school.objects;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import java.util.HashMap;

public abstract class DatabaseObjectHelper {

    public abstract ContentValues convertToContentValues();
    public abstract void setDataByCursor(Cursor cursor);
    public abstract HashMap.Entry<String, Integer> getPrimaryId();

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
