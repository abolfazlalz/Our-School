/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.futech.our_school.ActivityHelper
import com.futech.our_school.R
import com.futech.our_school.adapter.StudyHistoryAdapter
import com.futech.our_school.dialog.StudyDialog
import com.futech.our_school.objects.StudyDurationData
import com.futech.our_school.request.schedules.StudyHistoryControl
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.request.listener.SelectListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudyHistoryActivity : ActivityHelper(), SelectListener<MutableList<StudyDurationData>> {

    private lateinit var historyList: RecyclerView
    private lateinit var refresh: SwipeRefreshLayout

    private lateinit var items: List<StudyDurationData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_history)

        historyList = findViewById(R.id.study_history)
        refresh = findViewById(R.id.refresh)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "تاریخچه مطالعه"
        val study = StudyHistoryControl(this)
        study.getHistory(1, 20, this)
        refresh.isRefreshing = true

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val dlg = StudyDialog(this)
            dlg.show()
        }
        refresh.setOnRefreshListener { StudyHistoryControl(this).getHistory(1, 20, this)}
    }

    override fun onSelect(data: MutableList<StudyDurationData>?, isOnline: Boolean) {
        if (data != null) {
            this.items = data
            StudyHistoryAdapter.setGridAdapter(historyList, data)
        }
        refresh.isRefreshing = false
    }

    override fun onError(msg: String?) {
        SchoolToast.makeFail(this, msg, SchoolToast.LENGTH_SHORT).show()
        finish()
        refresh.isRefreshing = false
    }
}