package com.futech.our_school

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.futech.our_school.activities.AboutUs
import com.futech.our_school.dialog.StudyDialog
import com.futech.our_school.local_database.backup.Backup
import com.futech.our_school.utils.LocaleHelper
import com.futech.our_school.utils.NetworkStateReceiver
import java.util.*


open class ActivityHelper : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener {

    private lateinit var studyDialog: MenuItem

    private var networkStateReceiver: NetworkStateReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.setLocale(this, "fa")
        setAppLocale(LocaleHelper.getLanguage(this))
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun setAppLocale(localeCode: String) {
        val resources = resources
        val dm = resources.displayMetrics
        val config = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(Locale(localeCode.toLowerCase(Locale.ROOT)))
        } else {
            config.locale = Locale(localeCode.toLowerCase(Locale.ROOT))
        }
        resources.updateConfiguration(config, dm)
    }

    fun showStudyMenu(visible: Boolean) {
        if (this::studyDialog.isInitialized)
            studyDialog.isVisible = visible
    }

    private fun checkStudyMenu() {
        if (StudyDialog.isStudyTime(this))
            showStudyMenu(true)
    }

    override fun onResume() {
        super.onResume()
        checkStudyMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        studyDialog = menu.add(R.string.already_study)
        studyDialog.setOnMenuItemClickListener {
            val dlg = StudyDialog(this)
            dlg.show()
            true
        }
        if (!StudyDialog.isStudyTime(this)) {
            studyDialog.isVisible = false
        }
        studyDialog.setShowAsAction(android.app.ActionBar.DISPLAY_SHOW_HOME)
        val aboutUs = menu.add("درباره ما")
        aboutUs.setOnMenuItemClickListener {
            openAboutUs()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun openAboutUs() {
        startActivity(Intent(this@ActivityHelper, AboutUs::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateReceiver?.removeListener(this)
        unregisterReceiver(networkStateReceiver)
    }

    override fun networkAvailable() {
        Backup.syncBackup(this)
    }

    override fun networkUnavailable() {
    }
}