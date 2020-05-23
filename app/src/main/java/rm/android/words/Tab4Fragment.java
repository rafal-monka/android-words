package rm.android.words;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class Tab4Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab4, container, false);
        String url = GlobalVariables.WordsUrlBuyList;
        WebView webViewBuyList = v.findViewById(R.id.webViewBuyList);
        webViewBuyList.getSettings().setJavaScriptEnabled(true);
        webViewBuyList.getSettings().setJavaScriptEnabled(true);
        webViewBuyList.getSettings().setLoadWithOverviewMode(true);
        webViewBuyList.getSettings().setUseWideViewPort(true);
        webViewBuyList.getSettings().setBuiltInZoomControls(true);
        webViewBuyList.setWebViewClient(new WebViewClient());
        webViewBuyList.loadUrl(url);

        return v;
    }
}