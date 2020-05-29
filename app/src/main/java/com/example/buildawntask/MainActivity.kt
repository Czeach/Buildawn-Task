package com.example.buildawntask

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // Implement date and time and update
        val timeDate = object : Thread() {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                try {
                    while (!isInterrupted) {
                        run {
                            sleep(1000)
                            this@MainActivity.runOnUiThread(Runnable {
                                val current = System.currentTimeMillis()
                                val sdf = SimpleDateFormat("M/d/y")
                                val  dateString = sdf.format(current)
                                val stf = SimpleDateFormat("H:m")
                                val timeString = stf.format(current)
                                date_text.text = dateString
                                time_text.text = timeString
                            })
                        }
                    }
                } catch (e: InterruptedException) {
                    Log.d("MainActivity", e.localizedMessage)
                }
            }
        }
        timeDate.start()
    }

}
