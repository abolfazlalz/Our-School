package com.futech.our_school.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.futech.our_school.R;
import com.futech.our_school.objects.ImageData;
import com.futech.our_school.utils.request.HttpHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ImageLocalDatabase extends DatabaseHelper<ImageData> {

    private static final String DB_NAME = "image_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "images";
    private static String TAG = "ImageLocalDatabase";


    private ImageData getImageByUrlAddress(String urlAddress) {
        List<ImageData> selectImage = select(ImageData.SERVER_URL_KEY + "=?", new String[]{urlAddress});
        if (selectImage.size() == 1) {
            ImageData data = selectImage.get(0);
            imageSelected(data);
            return data;
        }else return null;
    }

    @Override
    public void delete(String whereCause, String[] args) {
        List<ImageData> select = select(whereCause, args);
        for (ImageData imageData : select) {
            final File sd = getContext().getFilesDir();
            final File file = new File(sd, imageData.getFilePath());
            file.deleteOnExit();
        }
        super.delete(whereCause, args);
    }

    @Override
    public void insert(ImageData data) {
        String[] values = new String[3];
        values[0] = data.getServerUrl();
        values[1] = data.getFilePath();
        values[2] = data.getImageName();
        String sb = ImageData.SERVER_URL_KEY + "=?" + " and " + ImageData.FILE_PATH_KEY + "=?" + " and " + ImageData.IMAGE_NAME_KEY + "=?";
        delete(sb, values);
        super.insert(data);
    }

    public void loadImage(RequestQueue requestQueue, String url, final ImageView imageView, Bitmap defaultImage, int width, int height, final String name, final HttpHelper.HttpImageDownloaderListener listener) {
        try {
            if (url == null || url.equals("")) return;
            url = url.replace("localhost", "10.0.2.2");

            final String filename = name + ".png";
            final File sd = getContext().getFilesDir();
            final File dest = new File(sd, filename);

            if (defaultImage != null) imageView.setImageBitmap(defaultImage);

            ImageData imageByUrlAddress = getImageByUrlAddress(url);
            if (imageByUrlAddress != null) {
                File file = new File(getContext().getFilesDir(), imageByUrlAddress.getFilePath());
                if (file.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                    if (listener != null) listener.onDownload(false);
                }
            }

            String finalUrl = url;

            ImageRequest request = new ImageRequest(url, response -> {
                imageView.setImageBitmap(response);

                try {
                    boolean delete = true;
                    if (dest.exists()) delete = dest.delete();
                    if (delete) {
                        FileOutputStream out = new FileOutputStream(dest, true);
                        try {
                            response.compress(Bitmap.CompressFormat.PNG, 90, out);
                            Date current = new Date();
                            ImageData imageData = new ImageData(filename, finalUrl,
                                    current, current, name);
                            insert(imageData);
                        } catch (OutOfMemoryError error) {
                            Log.i(TAG, "LoadImage: onResponse: " + error.getMessage());
                        } finally {
                            out.close();
                            out.flush();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (listener != null) listener.onDownload(true);

            }, width, height, ImageView.ScaleType.FIT_START, Bitmap.Config.ARGB_8888, error -> {
                if (listener != null) {
                    listener.onError(getContext().getString(R.string.unable_to_open_picture));
                }
            });

            requestQueue.add(request);
            requestQueue.start();
        } catch (OutOfMemoryError memoryError) {
            Log.i(TAG, "loadImage: " + memoryError.getMessage());
        }
    }

    public void deleteUnusedImages(int hoursToDelete) {
        for (ImageData image : selectAll()) {
            Calendar imageCreatedCalendar = GregorianCalendar.getInstance();
            Calendar currentCalendar = GregorianCalendar.getInstance();
            imageCreatedCalendar.setTime(image.getLastUse());
            currentCalendar.setTime(new Date());
            float hours = currentCalendar.get(Calendar.HOUR) - imageCreatedCalendar.get(Calendar.HOUR);
            if (hours >= hoursToDelete) {
                delete(ImageData.ID_KEY + "=?", new String[]{String.valueOf(image.getId())});
            }
        }
    }

    private void imageSelected(ImageData imageData) {
        imageData.setLastUseNow();
        update(imageData, ImageData.ID_KEY + "=?", new String[]{String.valueOf(imageData.getId())});
    }

    public ImageLocalDatabase(@Nullable Context context) {
        super(context, DB_NAME, DB_VERSION, TABLE_NAME);
    }

    @Override
    protected List<ImageData> convertCursorToList(Cursor cursor) {
        return ImageData.cursorToImageData(cursor);
    }

    @Override
    protected ContentValues convertToContentValues(ImageData value) {
        return value.convertToContentValues();
    }

    @Override
    protected String createTableQuery() {
        Map<String, String> col = new HashMap<>();
        col.put(ImageData.ID_KEY, QueryCreator.INTEGER_KEY);
        col.put(ImageData.IMAGE_NAME_KEY, QueryCreator.STRING_KEY);
        col.put(ImageData.CREATE_TIME_KEY, QueryCreator.STRING_KEY);
        col.put(ImageData.FILE_PATH_KEY, QueryCreator.STRING_KEY);
        col.put(ImageData.LAST_USE_KEY, QueryCreator.STRING_KEY);
        col.put(ImageData.SERVER_URL_KEY, QueryCreator.STRING_KEY);
        return QueryCreator.getCreateDatabaseQuery(tableName, col, ImageData.ID_KEY);
    }
}
