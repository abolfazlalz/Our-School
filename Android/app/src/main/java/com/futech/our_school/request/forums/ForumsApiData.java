/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.request.forums;

import com.futech.our_school.objects.forums.ForumsQuestionData;
import com.futech.our_school.request.ApiTemplate;
import com.futech.our_school.utils.request.ApiHelper;

import java.util.List;

public class ForumsApiData extends ApiTemplate {

    public class Data extends ApiTemplate.Data {
        private List<ForumsQuestionData> questions;
        public List<ForumsQuestionData> getQuestions() {
            return this.questions;
        }
    }

    private Data data;
    public Data getData() {
        return this.data;
    }
}
