package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class MainActivity : AppCompatActivity() {

    private lateinit var btnStartStudyingTab: Button
    private lateinit var btnAnalyticsTab: Button
    private lateinit var btnHistoryTab: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.yoga_list)

        //btnStartStudyingTab = findViewById(R.id.TabStartStudyingButton)
        //btnAnalyticsTab = findViewById(R.id.TabAnalyticsButton)
        //btnHistoryTab = findViewById(R.id.TabHistoryButton)
        val webView: WebView = findViewById(R.id.webView)

        // Enable JavaScript in the WebView
        webView.settings.javaScriptEnabled = true

        // Load the URL with the iframe content
        val iframeContent = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/SvPKFsCiMsw?si=aX39cuiOMQhc6voH\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
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
    }
}