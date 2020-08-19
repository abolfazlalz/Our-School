/*
 * Copyright (c) 2020. Abl-Developer.
 * Abl-Developer by Abolfazl Managing.
 */

package com.futech.our_school.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.futech.our_school.ActivityHelper
import com.futech.our_school.R
import com.futech.our_school.adapter.BooksSpinnerAdapter
import com.futech.our_school.request.forums.ForumsApiControl
import com.futech.our_school.request.upload.UploadRequestControl
import com.futech.our_school.request.upload.UploadRequestData
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.request.listener.DataChangeListener
import com.futech.our_school.utils.request.listener.SelectListener
import java.io.IOException

class AddQuestionActivity : ActivityHelper() {

    private lateinit var bookSelect: Spinner
    private lateinit var questionTitle: EditText
    private lateinit var questionText: EditText
    private lateinit var addButton: Button
    private lateinit var attachmentFile: ImageView
    private lateinit var attachFileBtn: ImageButton

    private var fileUploadId = -1
    private var fileIsSelected = false

    private var imageData: ByteArray? = null
    private var postURL: String = "https://our-school.abolfazlalz.ir/api/media.php?request=new-image&token="

    private lateinit var api: ForumsApiControl

    companion object {
        private const val IMAGE_PICK_CODE = 999
        private const val TAG = "ADD_QUESTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        bookSelect = findViewById(R.id.lesson_select)
        questionTitle = findViewById(R.id.question_title)
        questionText = findViewById(R.id.question_text)
        addButton = findViewById(R.id.add_button)
        attachmentFile = findViewById(R.id.attachment_image)
        attachFileBtn = findViewById(R.id.add_attachment_btn)

        setTitle(R.string.add_question)

        api = ForumsApiControl(this)

        BooksSpinnerAdapter.setAllBookAdapter(this, bookSelect)

        addButton.setOnClickListener { addQuestion() }
        attachFileBtn.setOnClickListener { launchGallery() }

        postURL += getString(R.string.api_token)


    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                attachmentFile.setImageURI(uri)
                createImageData(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadImage() {
        val uploadApi = UploadRequestControl(this)
        uploadApi.uploadImageInNewCollection(questionTitle.text.toString(), imageData, object : SelectListener<UploadRequestData> {
            override fun onSelect(data: UploadRequestData?, isOnline: Boolean) {
                if (data == null || data.data.uploadId <= 0) {
                    SchoolToast.makeFail(this@AddQuestionActivity, R.string.upload_photo_error, SchoolToast.LENGTH_SHORT).show()
                    addButton.isEnabled = true
                    return
                }
                fileUploadId = data.data.listId
                addQuestion()
            }

            override fun onError(msg: String?) {
                SchoolToast.makeFail(this@AddQuestionActivity, R.string.upload_photo_error, SchoolToast.LENGTH_SHORT).show()
                Log.e(TAG, msg!!)
                addButton.isEnabled = true
            }

        })
    }

    override fun onPause() {
        super.onPause()
        api.cancel()
    }

    private fun addQuestion() {
        addButton.isEnabled = false
        if (fileIsSelected && fileUploadId == -1) {
            uploadImage()
            return
        }
        val title = questionTitle.text.toString().trim()
        val text = questionText.text.toString().trim()
        val bookId = BooksSpinnerAdapter.getBookData(bookSelect).id

        api.addQuestion(title, text, bookId, fileUploadId, object : DataChangeListener {
            override fun onChange() {
                SchoolToast.makeSuccess(applicationContext, getString(R.string.add_question_success), SchoolToast.LENGTH_SHORT).show()
                finish()
            }

            override fun onError(msg: String?) {
                SchoolToast.makeFail(applicationContext, msg, SchoolToast.LENGTH_SHORT).show()
                addButton.isEnabled = true
            }
        })
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        fileUploadId = -1
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
            fileIsSelected = true
        }
    }

}