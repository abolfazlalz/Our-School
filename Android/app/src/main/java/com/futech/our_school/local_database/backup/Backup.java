/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.local_database.backup;

import android.content.Context;

import com.futech.our_school.objects.StudyDurationData;
import com.futech.our_school.request.schedules.StudyHistoryControl;
import com.futech.our_school.utils.request.listener.DataChangeListener;

import java.util.List;

public class Backup {

    public static String DB_NAME = "backup";
    public static int DB_VERSION = 2;

    public static void syncBackup(Context context) {
        StudyHistoryBackup studyBackup = new StudyHistoryBackup(context);
        List<StudyDurationData> studyDurationData = studyBackup.selectAll();
        studyBackup.deleteAll();
        StudyHistoryControl studyControl = new StudyHistoryControl(context);
        for (StudyDurationData data : studyDurationData) {
            studyControl.addStudyTime(data.getBook().getId(), data.getStartTime(), data.getEndTime(), new DataChangeListener() {
                @Override
                public void onChange() {

                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

}
