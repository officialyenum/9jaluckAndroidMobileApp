package com.lordwingames.a9jaluck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mTextView;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    private WebView myWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        mTextView = (TextView) findViewById(R.id.loadText);

        loginWebAction();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 50){
                    mProgressStatus++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);

                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);
                        myWebView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loginWebAction() {

        myWebView = (WebView) findViewById(R.id.loginwebView);
        // javascript Bridge
        final WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //improve webView performance
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        myWebView.setWebViewClient(new webViewClient());
        myWebView.loadUrl("https://9jaluck.com/account/login");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(event.getAction() == android.view.KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case android.view.KeyEvent.KEYCODE_BACK:
                    if(myWebView.canGoBack()){
                        myWebView.goBack();
                    }
                    else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onPause() {
        myWebView.onPause();
        myWebView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        myWebView.resumeTimers();
        myWebView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        myWebView.destroy();
        myWebView = null;
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Intent i = new Intent(LoginActivity.this, Splash2Activity.class);
                    startActivity(i);
                    break;
                case R.id.nav_games:
                    Intent j = new Intent(LoginActivity.this, GameActivity.class);
                    startActivity(j);
                    break;
                case R.id.nav_promo:
                    Intent k = new Intent(LoginActivity.this, promoActivity.class);
                    startActivity(k);
                    break;
            }
            return false;
        }
    };

    private class webViewClient extends WebViewClient
    {
        public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed() ;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Intent i = new Intent(LoginActivity.this, ErrorActivity.class);
            startActivity(i);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

}
