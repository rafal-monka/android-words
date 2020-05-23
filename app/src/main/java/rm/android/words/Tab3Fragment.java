package rm.android.words;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class Tab3Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);
        String url = GlobalVariables.WordsUrlJS;
        WebView webViewWordsJS = v.findViewById(R.id.webViewWordsJS);
        webViewWordsJS.getSettings().setJavaScriptEnabled(true);
        webViewWordsJS.getSettings().setJavaScriptEnabled(true);
        webViewWordsJS.getSettings().setLoadWithOverviewMode(true);
        webViewWordsJS.getSettings().setUseWideViewPort(true);
        webViewWordsJS.getSettings().setBuiltInZoomControls(true);
        webViewWordsJS.setWebViewClient(new WebViewClient());
        webViewWordsJS.loadUrl(url);

        return v;
    }
}