package com.futech.our_school.request.lesson;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.RegisterControl.RegisterControl;
import com.futech.our_school.local_database.lessons_database.LibraryLocalDatabase;
import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.listener.SelectListener;

import java.util.Arrays;
import java.util.List;

public class LessonBookControl extends ApiHelper<LessonBookApi> {
    public LessonBookControl(Context context) {
        super(context, context.getString(R.string.api_address) + "users/students.php",
                LessonBookApi.class);
        setTag("lesson_book_control");
    }

    public void getBooks(SelectListener<List<LessonBookData>> listener) {
        ApiControl<LessonBookApi> api = getApiControl("get-books");
        LibraryLocalDatabase db = new LibraryLocalDatabase(getContext());
        List<LessonBookData> data = db.selectAll();
        listener.onSelect(data, false);
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.response(new ApiControl.ApiListener<LessonBookApi>() {
            @Override
            public void onResponse(LessonBookApi api) {
                if (api.getData().isSuccess()) {
                    if (db.valuesAreDifferent(api.getData().getBooks(), null, null)) {
                        listener.onSelect(api.getData().getBooks(), true);
                        db.replaceAllData(api.getData().getBooks().toArray(new LessonBookData[0]));
                    }
                }else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });
    }

}
