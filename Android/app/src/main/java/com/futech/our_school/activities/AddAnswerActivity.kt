/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import com.futech.our_school.R
import com.futech.our_school.dialog.ProgressDialog
import com.futech.our_school.objects.forums.ForumsQuestionData
import com.futech.our_school.request.forums.ForumsApiControl
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.request.listener.DataChangeListener
import com.futech.our_school.utils.request.listener.SelectListener

class AddAnswerActivity : AppCompatActivity(), DataChangeListener {

    var questionId: Int = -1
    lateinit var answerEditText: EditText
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_answer)

        val button = findViewById<Button>(R.id.add_button)
        answerEditText = findViewById(R.id.answer_text)
        progressDialog = ProgressDialog(this)

        val id = intent.extras!!.getInt("id")
        val a = ForumsApiControl(this)
        a.getQuestion(id, object : SelectListener<List<ForumsQuestionData>> {
            override fun onSelect(data: List<ForumsQuestionData>?, isOnline: Boolean) {
                if (data == null || data.isEmpty()) {
                    finish()
                    return
                }
                val question = data[0]

                title = "پاسخ به سوال - " + question.title
                questionId = question.id
            }

            override fun onError(msg: String?) {
                finish()
            }
        })

        button.setOnClickListener { addAnswer() }
    }

    private fun addAnswer() {
        val answerText = answerEditText.text.toString().trim()
        if (answerText == "") {
            SchoolToast.makeFail(this, "لطفا پاسخ خود را بنویسید", SchoolToast.LENGTH_LONG).show()
            return
        }
        val forum = ForumsApiControl(this)
        forum.addAnswer(questionId, answerText, this)
        progressDialog.show()
    }

    override fun onChange() {
        SchoolToast.makeSuccess(this, "تشکر از پاسخگویی شما", SchoolToast.LENGTH_SHORT).show()
        progressDialog.cancel()
        finish()
    }

    override fun onError(msg: String?) {
        SchoolToast.makeFail(this, msg, SchoolToast.LENGTH_LONG).show()
        progressDialog.cancel()
    }
}