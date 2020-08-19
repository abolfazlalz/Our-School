/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.objects.forums;

import com.futech.our_school.request.accounts.AccountData;

import java.util.Date;

public class ForumsAnswerData {

    private int id;
    private String answer;
    private Date dateCreate;
    private Date dateModified;
    private int questionId;
    private AccountData writer;

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public int getQuestionId() {
        return questionId;
    }

    public AccountData getWriter() {
        return writer;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public Date getDateModified() {
        return dateModified;
    }
}
