package com.argando.parcersample;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.jetbrains.annotations.NotNull;

public class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(@NotNull WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}