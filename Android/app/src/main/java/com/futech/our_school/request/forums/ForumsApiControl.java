/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.request.forums;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.futech.our_school.R;
import com.futech.our_school.RegisterControl.RegisterControl;
import com.futech.our_school.objects.forums.ForumsQuestionData;
import com.futech.our_school.utils.SchoolToast;
import com.futech.our_school.utils.VolleyErrorTranslator;
import com.futech.our_school.utils.request.ApiControl;
import com.futech.our_school.utils.request.ApiHelper;
import com.futech.our_school.utils.request.listener.DataChangeListener;
import com.futech.our_school.utils.request.listener.SelectListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForumsApiControl extends ApiHelper<ForumsApiData> {


    public ForumsApiControl(Context context) {
        super(context, context.getString(R.string.api_address) + "forums/", ForumsApiData.class);
        setTag("forums_api");
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    public void getQuestionsList(SelectListener<List<ForumsQuestionData>> listener) {

        ApiControl<ForumsApiData> api = getApiControl("select-question");
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.response(new ApiControl.ApiListener<ForumsApiData>() {
            @Override
            public void onResponse(ForumsApiData api) {
                if (api.getData().isSuccess())
                    listener.onSelect(api.getData().getQuestions(), true);
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });
    }

    public void addQuestion(String title, String information, int bookId, int uploadId, DataChangeListener listener) {

        ApiControl<ForumsApiData> api = getApiControl("new-question");
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.addParameter(Request.Method.POST, "title", title);
        api.addParameter(Request.Method.POST, "text", information);
        api.addParameter(Request.Method.POST, "lesson-id", String.valueOf(bookId));
        api.addParameter(Request.Method.POST, "upload-id", String.valueOf(uploadId));

        api.response(new ApiControl.ApiListener<ForumsApiData>() {
            @Override
            public void onResponse(ForumsApiData api) {
                if (api.getData().isSuccess()) listener.onChange();
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });

    }

    public void getQuestion(int id, @NotNull SelectListener<List<ForumsQuestionData>> listener)
    {

        ApiControl<ForumsApiData> api = getApiControl("select-question");
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));
        api.addParameter(Request.Method.GET, "id", String.valueOf(id));
        api.response(new ApiControl.ApiListener<ForumsApiData>() {
            @Override
            public void onResponse(ForumsApiData api) {
                if (api.getData().isSuccess())
                    listener.onSelect(api.getData().getQuestions(), true);
                else listener.onError(api.getData().getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });
    }

    public void addAnswer(int questionId, @NotNull String answerText, DataChangeListener listener) {
        ApiControl<ForumsApiData> api = getApiControl("new-answer");
        api.addParameter(Request.Method.POST, "question-id", String.valueOf(questionId));
        api.addParameter(Request.Method.POST, "answer", answerText);
        api.addParameter(Request.Method.GET, "register-key",
                RegisterControl.getRegisterKey(getContext()));

        api.response(new ApiControl.ApiListener<ForumsApiData>() {
            @Override
            public void onResponse(ForumsApiData api) {
                if (!api.getData().isSuccess()) {
                    listener.onError(api.getData().getMessage());
                    return;
                }
                listener.onChange();
            }

            @Override
            public void onError(VolleyError error) {
                listener.onError(new VolleyErrorTranslator(error, getContext()).getMessage());
            }
        });
    }
}