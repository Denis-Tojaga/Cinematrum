package com.mob3000.cinematrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //hiding the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        if (intent != null) {
            webView = findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            String myYouTubeVideoUrl = intent.getStringExtra("url");
            webView.loadUrl(myYouTubeVideoUrl);
            webView.setWebChromeClient(new WebChromeClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setPluginState(WebSettings.PluginState.ON);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

}