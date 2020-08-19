package com.futech.our_school.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.futech.our_school.ActivityHelper
import com.futech.our_school.MainActivity
import com.futech.our_school.R
import com.futech.our_school.RegisterControl.RegisterControl
import com.futech.our_school.activities.StudyHistoryActivity
import com.futech.our_school.adapter.ClassMiniAdapter
import com.futech.our_school.adapter.ForumsAdapter
import com.futech.our_school.adapter.StudyHistoryAdapter
import com.futech.our_school.dialog.StudyDialog
import com.futech.our_school.objects.ClassData
import com.futech.our_school.objects.StudyDurationData
import com.futech.our_school.objects.forums.ForumsQuestionData
import com.futech.our_school.request.accounts.AccountData
import com.futech.our_school.request.forums.ForumsApiControl
import com.futech.our_school.request.schedules.ClassSchedulesControl
import com.futech.our_school.request.schedules.StudyHistoryControl
import com.futech.our_school.utils.InternetUtils
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.TourHelper
import com.futech.our_school.utils.request.listener.SelectListener
import java.util.*

class HomeFragment : Fragment(), SelectListener<List<StudyDurationData>> {

    private lateinit var studyHistory: RecyclerView
    private lateinit var classesListView: RecyclerView
    private lateinit var forumsListView: RecyclerView
    private lateinit var noStudyHistoryData: LinearLayout
    private lateinit var refreshLayout: SwipeRefreshLayout

    private lateinit var welcomeText: TextView
    private lateinit var noClassToday: TextView
    private lateinit var noForums: TextView
    private lateinit var noStudyMessage: TextView

    private lateinit var history: StudyHistoryControl
    private lateinit var forumsControl: ForumsApiControl
    private lateinit var registerControl: RegisterControl
    private lateinit var classControl: ClassSchedulesControl

    private lateinit var startNowButton: Button

    private val loadCount = 4
    private var refreshStatus = 0

    private lateinit var studyNow: TourHelper

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val header: LinearLayout = root.findViewById(R.id.header)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            header.elevation = 100f
            (activity as MainActivity).removeShadow()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studyHistory = view.findViewById(R.id.study_history)
        noStudyHistoryData = view.findViewById(R.id.no_study_data)
        classesListView = view.findViewById(R.id.class_list)
        refreshLayout = view.findViewById(R.id.refresh)
        welcomeText = view.findViewById(R.id.welcome_text)
        noForums = view.findViewById(R.id.no_forums)
        noClassToday = view.findViewById(R.id.no_class_today)
        forumsListView = view.findViewById(R.id.forums_list)
        noStudyMessage = view.findViewById(R.id.no_study_msg)
        startNowButton = view.findViewById(R.id.start_now_study)

        val moreStudyHistory: Button = view.findViewById(R.id.more_history_study)
        moreStudyHistory.setOnClickListener {
            startActivity(Intent(context, StudyHistoryActivity::class.java))
        }
        refreshLayout.setOnRefreshListener {
            refresh()
        }

        studyNow = TourHelper.showTour.Uncompleted(activity,
                startNowButton, "افزودن مدت مطالعه", "برای تعیین مدت زمان مطالعه خود، از این دکمه استفاده نمایید",
                TourHelper.HomeFragmentTour.STUDY_NOW);


        startNowButton.setOnClickListener {
            if (studyNow.isTourOpen) {
                startNowTour()
                return@setOnClickListener
            }
            val dlg = StudyDialog(this.requireActivity() as ActivityHelper?)
            dlg.show()
        }


        val name = RegisterControl.getSavedRegisterData(context, RegisterControl.REGISTER_NAME)
        welcomeText.text = ((context?.getString(R.string.welcome_message) + " ") + name)
        noStudyMessage.text = String.format(Locale.getDefault(), noStudyMessage.text.toString(), name)

        refresh()
    }

    private fun startNowTour() {
//        if (this::studyNow.isInitialized)
//            studyNow.closeTour()
//        val studentQuestionsLabel = activity?.findViewById<TextView>(R.id.questions_list_text)
//        val forumsTour: TourHelper = TourHelper.showTour.Uncompleted(activity,
//                studentQuestionsLabel, "مشاهده سوالات دانش آموزان", "مشاهده سوالات دانش آموزان",
//                TourHelper.HomeFragmentTour.FORUMS)
//        studentQuestionsLabel?.isClickable = true
//        studentQuestionsLabel?.setOnClickListener { forumsTour.closeTour() }
//        forumsListView.setOnClickListener {
//            if (forumsTour.isTourOpen) {
//                forumsTour.closeTour()
//            }
//        }
    }

    override fun onPause() {
        super.onPause()
        if (this::history.isInitialized) history.cancel()
        if (this::forumsControl.isInitialized) forumsControl.cancel()
        if (this::registerControl.isInitialized) registerControl.cancel()
        if (this::classControl.isInitialized) classControl.cancel()
    }

    private fun refresh() {
        if (InternetUtils.isConnected(context)) {
            refreshStatus = 0
            loadStudySchedule()
            loadTodayClass()
            loadRegisterInformation()
            loadForumsQuestions()
        } else {
            SchoolToast.makeFail(context, context?.getString(R.string.internet_not_connected), SchoolToast.LENGTH_SHORT).show()
            refreshLayout.isRefreshing = false
        }
    }

    private fun refreshComplete() {
        refreshStatus++
        if (refreshStatus == loadCount) {
            refreshLayout.isRefreshing = false
            refreshStatus = 0
        }
    }

    private fun loadStudySchedule() {
        history = StudyHistoryControl(this.context)
        history.getHistory(1, 5, this)
    }

    private fun loadForumsQuestions() {
        forumsControl = ForumsApiControl(this.context)
        val selectListener = object : SelectListener<List<ForumsQuestionData>> {
            override fun onError(msg: String?) {
                refreshComplete()
                SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show()
                setFeedStatus(forumsListView, noForums, false)
            }

            override fun onSelect(data: List<ForumsQuestionData>?, isOnline: Boolean) {
                refreshComplete()
                val isSuccess = data != null && data.isNotEmpty()
                ForumsAdapter.setAdapter(forumsListView, data)
                setFeedStatus(forumsListView, noForums, isSuccess)
            }

        }
        forumsControl.getQuestionsList(selectListener)
    }

    private fun loadTodayClass() {
        val listener = object : SelectListener<List<ClassData>> {
            override fun onSelect(data: List<ClassData>?, isOnline: Boolean) {
                refreshComplete()
                val isSuccess = data != null && data.isNotEmpty()
                if (isSuccess) ClassMiniAdapter.setAdapter(classesListView, data)
                setFeedStatus(classesListView, noClassToday, isSuccess)
            }

            override fun onError(msg: String?) {
                refreshComplete()
                SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show()
                setFeedStatus(classesListView, noClassToday, false)
            }
        }

        classControl = ClassSchedulesControl(context)
        classControl.getTodayClasses(listener)
    }

    private fun loadRegisterInformation() {
        val listener = object : RegisterControl.ProgressComplete {
            override fun onRegisterError(message: String?) {
                SchoolToast.makeFail(context, message, SchoolToast.LENGTH_SHORT).show()
                refreshComplete()
            }

            override fun onRegisterUpdate(accountData: AccountData?) {
                welcomeText.text = ((context?.getString(R.string.welcome_message) + " ") + accountData?.name)
                refreshComplete()
            }
        }

        registerControl = RegisterControl(context)
        registerControl.updateRegisterData(listener)
    }

    override fun onError(msg: String?) {
        refreshComplete()
        noStudyHistoryData.visibility = View.VISIBLE
        SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show()
        setFeedStatus(studyHistory, noStudyHistoryData, false)
    }

    override fun onSelect(data: List<StudyDurationData>?, isOnline: Boolean) {
        refreshComplete()
        val isSuccess = data != null && data.isNotEmpty()
        if (isSuccess) {
            StudyHistoryAdapter.setAdapter(studyHistory, data)
            noStudyHistoryData.visibility = View.GONE
            startNowTour()
        } else
            noStudyHistoryData.visibility = View.VISIBLE
        setFeedStatus(studyHistory, noStudyHistoryData, isSuccess)
    }

    private fun setFeedStatus(list: RecyclerView, text: View, isSuccess: Boolean) {
        list.visibility = if (isSuccess) View.VISIBLE else View.GONE
        text.visibility = if (!isSuccess) View.VISIBLE else View.GONE
    }


}