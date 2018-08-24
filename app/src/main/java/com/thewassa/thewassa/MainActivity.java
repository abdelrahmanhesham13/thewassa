package com.thewassa.thewassa;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    ProgressBar mProgressBar;
    public static final String BASE_URL = "http://www.3eesh.com.sa";
    public static final String CALL_US_BASE_URL = "http://www.3eesh.com.sa/contact.php";
    public static final String SETTING_BASE_URL = "http://www.3eesh.com.sa/mybooking.php";
    public static final String RESERVATION_BASE_URL = "https://www.3eesh.com.sa/myprofile.php";
    boolean clearHistory = false;
    ConnectivityManager connMgr;
    View parent;
    NetworkInfo networkInfo;
    TextView mTextView;
    BottomNavigationView mBottomNavigationView;

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int progress) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(progress);
            if (progress == 100) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        parent = findViewById(R.id.parent);
        ViewCompat.setLayoutDirection(parent,ViewCompat.LAYOUT_DIRECTION_RTL);

        forceRTLIfSupported();
        mWebView = (WebView) findViewById(R.id.web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mTextView = (TextView) findViewById(R.id.error_message);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setSelectedItemId(R.id.menu_home);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url)
            {
                if (clearHistory)
                {
                    clearHistory = false;
                    mWebView.clearHistory();
                }
                super.onPageFinished(view, url);
            }
        });

        if (checkInternet()) {
            showWeb();
            mWebView.loadUrl(BASE_URL);
        } else {
            showError();
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        if (checkInternet()) {
                            showWeb();
                            mWebView.loadUrl(BASE_URL);
                            clearHistory = true;
                        } else {
                            showError();
                        }
                        return true;
                    case R.id.menu_call_us:
                        if (checkInternet()) {
                            showWeb();
                            mWebView.loadUrl(CALL_US_BASE_URL);
                            clearHistory = true;
                        } else {
                            showError();
                        }
                        return true;
                    case R.id.menu_settings:
                        if (checkInternet()) {
                            showWeb();
                            mWebView.loadUrl(SETTING_BASE_URL);
                            clearHistory = true;
                        } else {
                            showError();
                        }
                        return true;
                    case R.id.menu_reservations:
                        if (checkInternet()) {
                            showWeb();
                            mWebView.loadUrl(RESERVATION_BASE_URL);
                            clearHistory = true;
                        } else {
                            showError();
                        }
                        return true;
                    default: return false;
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            if (checkInternet()) {
                showWeb();
                mWebView.reload();
            } else {
                showError();
            }
            return true;
        } else
            return false;
    }

    public boolean checkInternet() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else if (networkInfo == null) {
            return false;
        } else return false;
    }

    public void showWeb() {
        mTextView.setVisibility(View.INVISIBLE);
        mWebView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showError() {
        mTextView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
