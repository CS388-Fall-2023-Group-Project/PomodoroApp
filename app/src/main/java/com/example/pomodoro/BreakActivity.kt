package com.example.pomodoro

import android.content.Intent
import  android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.concurrent.TimeUnit

class BreakActivity: AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var countdownTimer: CountdownTimerHelper
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.yoga_list)



        progressBar = findViewById(R.id.progressBar2)
        timerText = findViewById(R.id.studyOffTimerTV)
        exitButton = findViewById(R.id.return_btn2)


        val restartTimer = intent.getBooleanExtra("restartTimer", false)

        val selectedStudyOff = intent.getStringExtra("selectedStudyOff")

        val studyOffMinutes = selectedStudyOff?.let { extractNumberFromString(it) } ?: 0


        val webView: WebView = findViewById(R.id.webView)
        exitButton = findViewById(R.id.return_btn2)
        // Enable JavaScript in the WebView
        webView.settings.javaScriptEnabled = true

        // Load the URL with the iframe content
        val iframeContent = "<iframe width=\"400\" height=\"315\" src=\"https://www.youtube.com/embed/videoseries?si=wRozd1-JgL4x5jja&amp;list=PLipSZg1JNsC-w8Ivhg5eYF4quTgPfNtrw\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        webView.loadData(iframeContent, "text/html", "utf-8")

        // Set a WebViewClient to handle navigation events within the WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        // Set a WebChromeClient to handle UI-related events
        webView.webChromeClient = object : WebChromeClient() {
            // Handle UI events here if needed
        }

        if (restartTimer) {
            restartTimer2()
        }

        val totalTimeInMillis = studyOffMinutes.toLong() // 1 minute
        val interval = 1000L // 1 second



        countdownTimer = CountdownTimerHelper(
            totalTimeInMillis = totalTimeInMillis,
            interval = interval,
            onTick = { millisUntilFinished ->
                val progress = (millisUntilFinished.toFloat() / totalTimeInMillis * 100).toInt()
                progressBar.progress = progress

                // Update the timer text
                val formattedTime = formatTime(millisUntilFinished)
                timerText.text = formattedTime
            },
            onFinish = {
                // Timer finished, handle it as needed
                // For example, navigate back to the previous activity
                val returnStudy= Intent(this@BreakActivity,TimerActivity::class.java)
                returnStudy.putExtra("restartTimer", true)
                startActivity(returnStudy)

            }
        )

        countdownTimer.start()

        exitButton.setOnClickListener {
            // Navigate back to the home fragment or activity

            val returnStudy= Intent(this@BreakActivity,TimerActivity::class.java)
            returnStudy.putExtra("restartTimer", true)
            startActivity(returnStudy)

        }
    }


    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun restartTimer2() {
        // Your logic to restart the timer goes here
        // For example, create a new instance of CountdownTimerHelper and start it
        countdownTimer.cancel()

        // Start a new timer
        countdownTimer.start()

    }


    private fun extractNumberFromString(timeString: String): Long {
        val regex = """(\d+) (\w+)""".toRegex()
        val matchResult = regex.find(timeString)
        val value = matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val unit = matchResult?.groupValues?.get(2)?.toLowerCase() ?: "minutes"

        return when (unit) {
            "seconds" -> value * 1000L
            "minutes" -> value * 60 * 1000L
            "hours" -> value * 60 * 60 * 1000L
            else -> throw IllegalArgumentException("Unsupported time unit: $unit")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to avoid memory leaks
        countdownTimer.cancel()
    }

    override fun onBackPressed() {

    }


}