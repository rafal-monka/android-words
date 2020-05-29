package rm.android.words;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class Tab3Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);
        String url = GlobalVariables.WordsUrlJS;
        WebView webViewWordsJS = v.findViewById(R.id.webViewWordsJS);

        webViewWordsJS.setWebChromeClient(new WebChromeClient() {
            //Other methods for your WebChromeClient here, if needed..
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webViewWordsJS.getSettings().setJavaScriptEnabled(true);
        webViewWordsJS.getSettings().setJavaScriptEnabled(true);
        webViewWordsJS.getSettings().setLoadWithOverviewMode(true);
        webViewWordsJS.getSettings().setUseWideViewPort(true);
        webViewWordsJS.getSettings().setBuiltInZoomControls(true);
        //webViewWordsJS.setWebViewClient(new WebViewClient()); --> setWebChromeClient
        webViewWordsJS.loadUrl(url);

        return v;
    }
}