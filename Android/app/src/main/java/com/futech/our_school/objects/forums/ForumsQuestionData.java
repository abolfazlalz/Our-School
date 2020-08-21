/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.objects.forums;

import com.futech.our_school.objects.LessonBookData;
import com.futech.our_school.request.accounts.AccountData;
import com.futech.our_school.request.media.MediaCollectionData;

import java.util.Date;

public class ForumsQuestionData {

    private int id;
    private String title;
    private String body;
    private Date dateCreated;
    private Date dateModified;
    private AccountData writer;
    private LessonBookData lesson;
    private ForumsAnswerData[] answers;
    //todo: its not parsing in json
    private MediaCollectionData mediaList;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public AccountData getWriter() {
        return writer;
    }

    public void setWriter(AccountData writer) {
        this.writer = writer;
    }

    public LessonBookData getLesson() {
        return lesson;
    }

    public void setLesson(LessonBookData lesson) {
        this.lesson = lesson;
    }

    public ForumsAnswerData[] getAnswers() {
        return answers;
    }

    public void setAnswers(ForumsAnswerData[] answers) {
        this.answers = answers;
    }

    public MediaCollectionData getMediaList() {
        return mediaList;
    }
}
