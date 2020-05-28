package com.example.buildawntask

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val currentTime = LocalTime.now()
        time_text.text = currentTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)).toString()

        val currentDate = LocalDate.now()
        date_text.text = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE)

        object : Thread() {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                try {
                    while (!isInterrupted) {
                        run {
                            sleep(1000)
                            this@MainActivity.runOnUiThread(Runnable {
                                val date = System.currentTimeMillis()
                                val sdf = SimpleDateFormat("MMM dd yy\nhh-mm-ss a")
                                val  dateString = sdf.format(date)
                                date_text.text = dateString
                            })
                        }
                    }
                } catch (e: InterruptedException) {
                    Log.d("MainActivity", e.localizedMessage)
                }
            }
        }
    }

}
