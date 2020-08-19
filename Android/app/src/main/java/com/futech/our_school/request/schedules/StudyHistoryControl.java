package com.futech.our_school.request.schedules;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.RegisterControl.RegisterControl;
import com.futech.our_school.local_database.backup.StudyHistoryBackup;
import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.objects.StudyDurationData;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.listener.DataChangeListener;
import com.futech.our_school.utils.request.listener.SelectListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudyHistoryControl extends ApiHelper<StudyHistoryApiData> {

    public StudyHistoryControl(Context context) {
        super(context, context.getString(R.string.api_address) + "users/students.php",
                StudyHistoryApiData.class);
        setTag("study-history");
    }

    public void getHistory(int page, int count, SelectListener<List<StudyDurationData>> listener) {
        ApiControl<StudyHistoryApiData> api = getApiControl("select-study-history");
        api.addParameter(Request.Method.GET, "page", String.valueOf(page)).
                addParameter(Request.Method.GET, "count", String.valueOf(count)).
                addParameter(Request.Method.GET, "register-key",
                        RegisterControl.getRegisterKey(getContext()));

        api.response(new ApiControl.ApiListener<StudyHistoryApiData>() {
            @Override
            public void onResponse(StudyHistoryApiData api) {
                if (api.getData().isSuccess()) listener.onSelect(api.getData().getItems(), true);
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                VolleyErrorTranslator translator = new VolleyErrorTranslator(error, getContext());
                listener.onError(translator.getMessage());
            }
        });
    }

    public void deleteStudyTime(int id, DataChangeListener listener) {
        ApiControl<StudyHistoryApiData> api = getApiControl("delete-study-time");
        api.addParameter(Request.Method.GET, "id", String.valueOf(id));
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.response(new ApiControl.ApiListener<StudyHistoryApiData>() {
            @Override
            public void onResponse(StudyHistoryApiData api) {
                if (api.getData().isSuccess()) listener.onChange();
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });
    }

    public void addStudyTime(int lessonId, Date startDate, Date endDate, DataChangeListener listener) {
        ApiControl<StudyHistoryApiData> api = getApiControl("add-study-time");

        String pattenFormat = getContext().getString(R.string.datetime_db_format);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattenFormat, Locale.ENGLISH);

        api.addParameter(Request.Method.POST, "book-id", String.valueOf(lessonId)).
                addParameter(Request.Method.POST, "start-time", dateFormat.format(startDate)).
                addParameter(Request.Method.POST, "end-time", dateFormat.format(endDate)).
                addParameter(Request.Method.GET, "register-key",
                        RegisterControl.getRegisterKey(getContext()));

        api.response(new ApiControl.ApiListener<StudyHistoryApiData>() {
            @Override
            public void onResponse(StudyHistoryApiData api) {
                if (api.getData().isSuccess()) listener.onChange();
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
                StudyHistoryBackup backup = new StudyHistoryBackup(getContext());
                StudyDurationData data = new StudyDurationData();
                data.setEndTime(endDate);
                data.setStartTime(startDate);
                LessonBookData lesson = new LessonBookData();
                lesson.setId(lessonId);
                data.setBook(lesson);
                backup.insert(data);
            }
        });
    }

}
