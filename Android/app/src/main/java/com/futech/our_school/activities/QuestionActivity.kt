/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.activities

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.futech.our_school.R
import com.futech.our_school.adapter.AnswerAdapter
import com.futech.our_school.dialog.ProgressDialog
import com.futech.our_school.objects.forums.ForumsQuestionData
import com.futech.our_school.request.forums.ForumsApiControl
import com.futech.our_school.utils.TourHelper
import com.futech.our_school.utils.TourHelper.CreateTour.uncompleted
import com.futech.our_school.utils.request.HttpHelper
import com.futech.our_school.utils.request.listener.SelectListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tourguide.tourguide.TourGuide

class QuestionActivity : AppCompatActivity() {

    lateinit var questionTitle: TextView
    lateinit var questionText: TextView
    lateinit var questionStudent: TextView
    lateinit var questionsAnswer: RecyclerView
    lateinit var addAnswer: FloatingActionButton
    lateinit var progressDialog: ProgressDialog
    lateinit var refresh: SwipeRefreshLayout
    lateinit var questionImage: ImageView
    private var id = -1

    private lateinit var addAnswerTour: TourHelper.CreateTour
    private lateinit var noAnswer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        questionStudent = findViewById(R.id.student_name)
        questionText = findViewById(R.id.question_text)
        questionTitle = findViewById(R.id.question_title)
        questionsAnswer = findViewById(R.id.answers)
        addAnswer = findViewById(R.id.add_answer)
        progressDialog = ProgressDialog(this)
        refresh = findViewById(R.id.refresh)
        noAnswer = findViewById(R.id.no_answers)
        questionImage = findViewById(R.id.question_img)
        val addAnswerBtn = findViewById<Button>(R.id.add_answer_btn)
        addAnswerBtn.isEnabled = true
        addAnswerBtn.setOnClickListener {
            addNewAnswer()
        }
        addAnswerTour = uncompleted(this, addAnswer,
                "افزودن پاسخ", "برای پاسخ به این سوال، بر روی این دکمه کلیک نمایید", TourHelper.ForumsTour.ADD_ANSWER)

        if (!addAnswerTour.isCompleted)
            addAnswerTour.showTour()

        addAnswer.isEnabled = false
        progressDialog.show()

        id = intent.extras?.getInt("id")!!
        loadQuestions()


        addAnswer.setOnClickListener {
            if (addAnswerTour.isTourOpen) {
                addAnswerTour.closeTour()
                return@setOnClickListener
            }
            addNewAnswer()
        }

        refresh.setOnRefreshListener {
            addAnswer.isEnabled = false
            loadQuestions()
        }
    }

    private fun addNewAnswer() {
        val intent = Intent(applicationContext, AddAnswerActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun loadQuestions() {
        val control = ForumsApiControl(this)
        control.getQuestion(id, object : SelectListener<List<ForumsQuestionData>> {
            override fun onSelect(data: List<ForumsQuestionData>?, isOnline: Boolean) {
                if (data == null || data.isEmpty()) {
                    finish()
                    return
                }
                progressDialog.dismiss()
                val question = data[0]
                questionTitle.text = question.title
                title = question.title
                questionText.text = question.body
                questionStudent.text = question.writer.fullName
                questionsAnswer.visibility = View.GONE
                noAnswer.visibility = View.VISIBLE
                if (question.answers != null) {
                    if (question.answers.isNotEmpty()) {
                        questionsAnswer.visibility = View.VISIBLE
                        noAnswer.visibility = View.GONE
                    }
                    AnswerAdapter.setAdapter(questionsAnswer, question.answers.toMutableList())
                }

                if (question.mediaList != null && question.mediaList.files.isNotEmpty()) {
                    questionImage.visibility = View.VISIBLE
                    HttpHelper.loadImage(this@QuestionActivity, question.mediaList.files[0].address, questionImage, null, 150, 150, "question-image-" + question.mediaList.files[0].id)
                }

                addAnswer.isEnabled = true
                refresh.isRefreshing = false

            }

            override fun onError(msg: String?) {
                finish()
                refresh.isRefreshing = false
            }
        })
    }
}