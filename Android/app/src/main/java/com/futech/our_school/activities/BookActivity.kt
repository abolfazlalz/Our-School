/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.futech.our_school.ActivityHelper
import com.futech.our_school.R
import com.futech.our_school.RegisterControl.RegisterControl
import com.futech.our_school.dialog.StudyDialog
import com.futech.our_school.objects.LessonBookData
import com.futech.our_school.utils.request.HttpHelper
import java.util.*

class BookActivity : ActivityHelper() {

    private lateinit var lessonBookData: LessonBookData

    private lateinit var noStudy: TextView
    private lateinit var bookTitle: TextView
    private lateinit var bookImage: ImageView
    private lateinit var studyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BOOK_ID = "book"
        setContentView(R.layout.activity_book)
        noStudy = findViewById(R.id.no_study_msg)
        studyButton = findViewById(R.id.study_button)
        noStudy.text = String.format(Locale.getDefault(), noStudy.text.toString(), RegisterControl.getSavedRegisterData(this, RegisterControl.REGISTER_NAME))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bookImage = findViewById(R.id.book_image)
        bookTitle = findViewById(R.id.book_title)


        if (intent.extras?.get(BOOK_ID) !is LessonBookData) return
        lessonBookData = intent.extras!!.get(BOOK_ID) as LessonBookData

        HttpHelper.loadImage(this, lessonBookData.photo, bookImage, null, 250, 250, lessonBookData.title)
        bookTitle.text = lessonBookData.title
        title = lessonBookData.title

        studyButton.setOnClickListener {

            val study = StudyDialog(this)
            study.setBook(lessonBookData.id)
            study.show()
        }
    }

    companion object {
        lateinit var BOOK_ID: String
    }


}