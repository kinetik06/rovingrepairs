package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AppointmentActivity extends AppCompatActivity {

    WebView mWebView;
    String bookURL = "https://squareup.com/appointments/book/1DF6K51SMWE5B/roving-repairs";
    String changeURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);


        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(bookURL);
        Log.d("URL: ", mWebView.getUrl());
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                changeURL = mWebView.getUrl();

                if (changeURL.contains("confirmation")){
                    Toast.makeText(AppointmentActivity.this, "Thanks for booking with Us! You should receive a confirmation email soon!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AppointmentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
