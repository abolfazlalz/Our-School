package com.futech.our_school.ui.forums

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.futech.our_school.R
import com.futech.our_school.activities.AddQuestionActivity
import com.futech.our_school.adapter.ForumsAdapter
import com.futech.our_school.objects.forums.ForumsQuestionData
import com.futech.our_school.request.forums.ForumsApiControl
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.TourHelper
import com.futech.our_school.utils.request.listener.SelectListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ForumsFragment : Fragment(), SelectListener<MutableList<ForumsQuestionData>> {

    private lateinit var questionsList: RecyclerView
    private lateinit var api: ForumsApiControl
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var uncompleted: TourHelper.CreateTour

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_forums, container, false)

        questionsList = root.findViewById(R.id.questions_list)
        refreshLayout = root.findViewById(R.id.refresh)
        fab = root.findViewById(R.id.fab)

        api = ForumsApiControl(context)
        api.setTag("fragment_forums")
        loadRefresh()
//        startTour()


        refreshLayout.setOnRefreshListener { loadRefresh() }

        fab.setOnClickListener {
            if (this::uncompleted.isInitialized && uncompleted.isTourOpen) {
                onDoneTour()
                return@setOnClickListener
            }
            context?.startActivity(Intent(context, AddQuestionActivity::class.java))
        }

        return root
    }

    override fun onPause() {
        super.onPause()
        api.cancel()
    }

    private fun loadRefresh() {
        api.getQuestionsList(this)
        refreshLayout.isRefreshing = true
    }

    override fun onError(msg: String?) {
        SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show()
        refreshLayout.isRefreshing = false
    }

    override fun onSelect(data: MutableList<ForumsQuestionData>?, isOnline: Boolean) {
        ForumsAdapter.setAdapterLinear(questionsList, data)
        refreshLayout.isRefreshing = false
    }

    private fun startTour() {
        uncompleted = TourHelper.CreateTour.uncompleted(activity, fab, "افزودن سوال", "می توانید برای افزودن سوال خود از این دکمه استفاده نمایید", TourHelper.ForumsTour.ADD_QUESTION);
        if (!uncompleted.isCompleted)
            uncompleted.showTour()
        onDoneTour()
    }

    private fun onDoneTour() {
        if (this::uncompleted.isInitialized && uncompleted.isTourOpen)
            uncompleted.closeTour()
    }
}