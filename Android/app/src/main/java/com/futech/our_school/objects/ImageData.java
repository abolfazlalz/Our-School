package com.futech.our_school.objects;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageData extends DatabaseObjectHelper {

    public static final String ID_KEY = "id";
    public static final String IMAGE_NAME_KEY = "imageName";
    public static final String FILE_PATH_KEY = "filePath";
    public static final String SERVER_URL_KEY = "serverUrl";
    public static final String CREATE_TIME_KEY = "createTime";
    public static final String LAST_USE_KEY = "lastUseKey";
    
    private int id;
    private String imageName;
    private String filePath;
    private String serverUrl;
    private Date createTime;
    private Date lastUse;

    public ImageData(String filePath, String serverUrl, Date createTime, Date lastUse, String imageName) {
        this.imageName = imageName;
        this.filePath = filePath;
        this.serverUrl = serverUrl;
        this.createTime = createTime;
        this.lastUse = lastUse;
    }

    private ImageData() {
        imageName = "";
        filePath = "";
        serverUrl = "";
        createTime = new Date();
        lastUse = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    private void setCreateTimeString(String createTime) {
        try {
            this.createTime = getDateFormat().parse(createTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }

    private void setLastUseString(String date) {
        try {
            lastUse = getDateFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ContentValues convertToContentValues() {
        ContentValues values = new ContentValues();
        values.put(IMAGE_NAME_KEY, getImageName());
        values.put(FILE_PATH_KEY, getFilePath());
        values.put(SERVER_URL_KEY, getServerUrl());
        SimpleDateFormat dateFormat = getDateFormat();
        values.put(CREATE_TIME_KEY, dateFormat.format(getCreateTime()));

        values.put(LAST_USE_KEY, dateFormat.format(getLastUse()));
        return values;
    }

    @Override
    public void setDataByCursor(Cursor cursor) {
        setCreateTimeString(cursor.getString(cursor.getColumnIndexOrThrow(CREATE_TIME_KEY)));
        setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(FILE_PATH_KEY)));
        setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_KEY)));
        setImageName(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_NAME_KEY)));
        setLastUseString(cursor.getString(cursor.getColumnIndexOrThrow(LAST_USE_KEY)));
        setServerUrl(cursor.getString(cursor.getColumnIndexOrThrow(SERVER_URL_KEY)));
    }

    @Override
    public AbstractMap.SimpleEntry<String, Integer> getPrimaryId() {
        return new AbstractMap.SimpleEntry<>(ID_KEY, getId());
    }


    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.getDefault());
    }

    public static List<ImageData> cursorToImageData(Cursor cursor) {
        List<ImageData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ImageData imageData = new ImageData();
            imageData.setDataByCursor(cursor);
            list.add(imageData);
        }
        return list;
    }

    public void setLastUseNow() {
        setLastUse(new Date());
    }
}

