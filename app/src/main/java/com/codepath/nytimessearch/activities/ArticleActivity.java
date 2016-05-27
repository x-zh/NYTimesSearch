package com.codepath.nytimessearch.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.codepath.nytimessearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlie_zhou on 5/27/16.
 */
public class ArticleActivity extends AppCompatActivity {
    @BindView(R.id.wvArticle)
    WebView wvArticle;

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        wvArticle.getSettings().setLoadsImagesAutomatically(true);
        wvArticle.getSettings().setJavaScriptEnabled(true);
        wvArticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        wvArticle.setWebViewClient(new MyBrowser(pbLoading));
        // Load the initial URL
        wvArticle.loadUrl(getIntent().getStringExtra("url"));
    }

    private class MyBrowser extends WebViewClient {
        private ProgressBar pbLoading;

        public MyBrowser (ProgressBar pbLoading) {
            this.pbLoading = pbLoading;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pbLoading.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pbLoading.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
