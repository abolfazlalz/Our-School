/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.futech.our_school.objects.DatabaseObjectHelper;

import java.util.List;

public abstract class DatabaseHelper<T extends DatabaseObjectHelper> extends SQLiteOpenHelper {

    final String tableName;
    private final Context context;

    public DatabaseHelper(
            @Nullable Context context, @Nullable String db_name, int db_version, String tableName) {
        super(context, db_name, null, db_version);
        this.tableName = tableName;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(deleteTableQuery());
        onCreate(db);
    }

    public void insert(T data) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, convertToContentValues(data));
        db.close();
    }

    public void insertItems(T[] data) {
        for (T t : data) {
            insert(t);
        }
    }

    public void update(T data, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(tableName, convertToContentValues(data), whereClause, whereArgs);
        db.close();
    }

    public List<T> selectAll() {
        return select(null, null);
    }

    public List<T> select(String whereCause, String[] args) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(tableName, null, whereCause, args, null, null, null);
        List<T> list = convertCursorToList(cursor);
        db.close();
        return list;
    }

    public void delete(String whereCause, String[] args) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, whereCause, args);
    }

    public void deleteAll() {
        delete(null, null);
    }

    public void replaceAllData(T[] data) {
        deleteAll();
        insertItems(data);
    }

    public boolean valuesAreDifferent(List<T> data, String whereClause, String[] whereArgs) {
        List<T> select = select(whereClause, whereArgs);
        for (T t : data) {
            for (T t1 : select) {
                if (t1.getPrimaryId() == t.getPrimaryId()) {
                    if (!t1.equals(t))
                        return false;
                }
            }
        }
        return true;
    }

    protected abstract List<T> convertCursorToList(Cursor cursor);

    protected abstract ContentValues convertToContentValues(T value);

    protected abstract String createTableQuery();

    protected String deleteTableQuery() {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    public Context getContext() {
        return context;
    }
}
