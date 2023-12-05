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

class BreakActivity: AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var countdownTimer: CountdownTimerHelper
    private lateinit var exitButton: Button
   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challenege_list)


        progressBar = findViewById(R.id.studyoffTimer)
        timerText = findViewById(R.id.timerTextView)
        exitButton = findViewById(R.id)



        exitButton.setOnClickListener {
            // Navigate back to the home fragment or activity
            val intent= Intent(this@BreakActivity,TimerActivity::class.java)
            intent.putExtra("restartTimer", true)
            startActivity(intent)

        }
    } */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.yoga_list)


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

        exitButton.setOnClickListener {
            // Navigate back to the home fragment or activity
            val returnStudy= Intent(this@BreakActivity,TimerActivity::class.java)
            returnStudy.putExtra("restartTimer", true)
            startActivity(returnStudy)

        }
    }



}