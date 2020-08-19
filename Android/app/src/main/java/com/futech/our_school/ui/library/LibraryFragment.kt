package com.futech.our_school.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.futech.our_school.R
import com.futech.our_school.adapter.LibraryAdapter
import com.futech.our_school.objects.LessonBookData
import com.futech.our_school.request.lesson.LessonBookControl
import com.futech.our_school.utils.InternetUtils
import com.futech.our_school.utils.SchoolToast
import com.futech.our_school.utils.request.listener.SelectListener

class LibraryFragment : Fragment(), SelectListener<List<LessonBookData>> {


    private lateinit var libraryList: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var bookControl: LessonBookControl

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryList = view.findViewById(R.id.library_list)
        refreshLayout = view.findViewById(R.id.refresh)
        refreshLayout.setOnRefreshListener { refreshList(); }
        bookControl = LessonBookControl(context)
        refreshList()
    }

    private fun refreshList() {
        refreshLayout.isRefreshing = true
        bookControl.getBooks(this)
        if (!InternetUtils.isConnected(context)) {
            refreshLayout.isRefreshing = false
            SchoolToast.makeFail(context, context?.getString(R.string.internet_not_connected), SchoolToast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        bookControl.cancel()
    }

    override fun onSelect(data: List<LessonBookData>?, isOnline: Boolean) {
        LibraryAdapter.setAdapter(libraryList, data)
        refreshLayout.isRefreshing = false
    }

    override fun onError(msg: String?) {
        SchoolToast.makeFail(context, msg, SchoolToast.LENGTH_SHORT).show()
        refreshLayout.isRefreshing = false
    }
}