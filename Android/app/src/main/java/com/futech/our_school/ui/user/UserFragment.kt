package com.futech.our_school.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.futech.our_school.R
import com.futech.our_school.RegisterControl.RegisterControl
import com.futech.our_school.activities.start_activity.StartingForm
import com.futech.our_school.dialog.ProgressDialog
import com.futech.our_school.request.accounts.AccountData
import com.futech.our_school.request.register.RegisterHelper
import com.futech.our_school.utils.InternetUtils
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.request.listener.DataChangeListener
import com.futech.our_school.utils.request.listener.SelectListener
import java.util.*
import java.util.concurrent.TimeUnit

class UserFragment : Fragment(), SelectListener<AccountData>, DataChangeListener {

    lateinit var usernameText: TextView
    lateinit var totalStudyTimeText: TextView
    lateinit var thisMonthStudyTimeText: TextView
    lateinit var signOutBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loadingInformation()
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameText = view.findViewById(R.id.username_text)
        totalStudyTimeText = view.findViewById(R.id.total_study_time)
        thisMonthStudyTimeText = view.findViewById(R.id.total_study_last_month)
        signOutBtn = view.findViewById(R.id.signOutBtn)

        usernameText.text = RegisterControl.getSavedRegisterData(context, RegisterControl.REGISTER_USERNAME)

        signOutBtn.setOnClickListener {
            val register = RegisterHelper(context)
            register.signOut(this)
        }

        loadingInformation()
    }

    private fun loadingInformation() {
        if (InternetUtils.isConnected(context)) {
            val register = RegisterHelper(context)
            register.getRegisterInformation(this)
        } else {
            SchoolToast.makeFail(context, context?.getString(R.string.internet_not_connected), SchoolToast.LENGTH_SHORT).show()
        }
    }

    private fun convertToText(time: AccountData.Study.TimeData): String {
        return if (time.hour > 0 && time.minute > 0) {
            String.format("%d ساعت و %d دقیقه", time.hour, time.minute)
        } else if (time.hour > 0) {
            String.format("%d ساعت", time.hour)
        } else if (time.minute > 0) {
            String.format("%d دقیقه", time.minute)
        } else {
            String.format("کمتر از یک دقیقه");
        };
    }

    override fun onChange() {
        requireActivity().finish()
        startActivity(Intent(activity, StartingForm::class.java))
    }

    override fun onError(msg: String?) {
        SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show()
    }

    override fun onSelect(data: AccountData?, isOnline: Boolean) {
        if (data == null) {
            loadingInformation()
            return
        }
        usernameText.text = data.username
        thisMonthStudyTimeText.text = convertToText(data.study.thisMonth)
        totalStudyTimeText.text = convertToText(data.study.total)
    }

}