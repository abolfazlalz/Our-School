package com.futech.our_school.request.upload;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.FileDataPart;
import com.futech.our_school.utils.request.listener.SelectListener;

import java.util.HashMap;
import java.util.Map;

public class UploadRequestControl extends ApiHelper<UploadRequestData> {

    public UploadRequestControl(Context context) {
        super(context, context.getString(R.string.api_address) + "media.php",
                UploadRequestData.class);
    }

    public void uploadImageFile(FileDataPart file, SelectListener<UploadRequestData> listener) {
        Map<String, Object> files = new HashMap<>();
        files.put("file", file);
        ApiControl<UploadRequestData> apiControl = getApiControl("upload-image");
        apiControl.uploadFile(files, new ApiControl.ApiListener<UploadRequestData>() {
            @Override
            public void onResponse(UploadRequestData api) {
                if (api.getData().isSuccess()) listener.onSelect(api, true);
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator errorTranslator = new VolleyErrorTranslator(error,
                        getContext());
                listener.onError(errorTranslator.getMessage());
            }
        });
    }

    /**
     * create new media collection and upload a file
     * @param title    title of new media collection
     * @param file     target file for uploading
     * @param listener listener
     */
    public void uploadImageInNewCollection(String title, FileDataPart file, SelectListener<UploadRequestData> listener) {
        Map<String, Object> files = new HashMap<>();
        files.put("file", file);
        ApiControl<UploadRequestData> apiControl = getApiControl("upload-new-list");
        apiControl.uploadFile(files, new ApiControl.ApiListener<UploadRequestData>() {
            @Override
            public void onResponse(UploadRequestData api) {
                if (api.getData().isSuccess()) listener.onSelect(api, true);
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator volleyError = new VolleyErrorTranslator(error, getContext());
                listener.onError(volleyError.getMessage());
            }
        });
    }

    public void uploadImageInNewCollection(String title, @Nullable
            byte[] file, SelectListener<UploadRequestData> listener)
    {
        if (file == null) {
            listener.onError("is null");
            return;
        }
        FileDataPart fileDataPart = new FileDataPart("image", file, "jpeg");
        uploadImageInNewCollection(title, fileDataPart, listener);
    }

    public void uploadImageFile(@Nullable byte[] file, SelectListener<UploadRequestData> listener) {
        if (file == null) {
            listener.onError("is null");
            return;
        }
        FileDataPart fileDataPart = new FileDataPart("image", file, "jpeg");
        uploadImageFile(fileDataPart, listener);
    }

}
