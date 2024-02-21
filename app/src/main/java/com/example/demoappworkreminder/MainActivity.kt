package com.example.demoappworkreminder

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.Touch
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.example.demoappworkreminder.Adapter.WorkAdapter
import com.example.demoappworkreminder.Model.Work
import com.example.demoappworkreminder.WorkManager.WorkReminder
import com.example.demoappworkreminder.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val URL = "content://com.example.demo.content.provider/work"
    val URI = Uri.parse(URL)

    lateinit var binding: ActivityMainBinding

    lateinit var workList: ArrayList<Work>
    lateinit var adapter: WorkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workList = ArrayList()
        adapter = WorkAdapter(applicationContext, R.layout.work_layout, workList)
        binding.listView.adapter = adapter
        grantUriPermission(
            "com.example.democontentprovider",
            URI,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        displayWorkList()

        binding.btnTest.setOnClickListener {
            testReminder()
        }
        startRemindSchedule()
    }

    private fun testReminder() {
        val smf = SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.getDefault())
        var tagetCalendar = Calendar.getInstance()
        if(!(tagetCalendar.get(Calendar.HOUR) < 6
                    && tagetCalendar.get(Calendar.AM_PM) == Calendar.AM))
        {
            tagetCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        var workCalendar = Calendar.getInstance()
        var strPrefixWorkDate = "06:00 AM "

        for (item in workList) {
            val workDate = smf.parse(strPrefixWorkDate + item.date)
            workCalendar.time = workDate
            Log.d("reminder_log", "day: ${workCalendar.time}")
            var compare = workCalendar.compareTo(tagetCalendar)
            if(compare >= 0) {
                remind(item, 10)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun startRemindSchedule() {
        val smf = SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.getDefault())
        var tagetCalendar = Calendar.getInstance()
        if(!(tagetCalendar.get(Calendar.HOUR) < 6
           && tagetCalendar.get(Calendar.AM_PM) == Calendar.AM))
        {
            tagetCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        var workCalendar = Calendar.getInstance()
        var strPrefixWorkDate = "06:00 AM "

        for (item in workList) {
            val workDate = smf.parse(strPrefixWorkDate + item.date)
            workCalendar.time = workDate
            Log.d("reminder_log", "day: ${workCalendar.time}")
            var compare = workCalendar.compareTo(tagetCalendar)
            if(compare >= 0) {
                var delayTime = workCalendar.timeInMillis - tagetCalendar.timeInMillis
                remind(item, delayTime)
            }
        }
    }

    private fun remind(work: Work, timeToDelay: Long) {
        val data = Data.Builder()
            .putString("workContent", work.content.toString())
            .putInt("notificationId", work.id)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = OneTimeWorkRequestBuilder<WorkReminder>()
            .setConstraints(
                Constraints.Builder().build()
            ).setInputData(data)
            .setInitialDelay(timeToDelay, TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniqueWork(work.id.toString(), ExistingWorkPolicy.REPLACE , workRequest)
    }

    private fun displayWorkList() {
        workList.clear()
        val cursor = contentResolver.query(Uri.parse("content://com.example.demo.content.provider/work"),
            null, null, null, null)
        cursor?.let {
            if(it.moveToFirst()) {
                do {
                    val id = it.getInt(0)
                    val content = it.getString(1)
                    val date = it.getString(2)
                    val work = Work(id, content, date)
                    workList.add(work)
                } while (it.moveToNext())
            }
        }
        adapter.notifyDataSetChanged()
    }
}