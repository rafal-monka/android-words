package rm.android.words;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Tab3Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);
        //TextView tv1 = v.findViewById(R.id.text1);
        //tv1.setText("WebView DIKI...");

        String url = GlobalVariables.WordsUrl;
        GlobalVariables.webViewWords = v.findViewById(R.id.webViewWords);
        GlobalVariables.webViewWords.getSettings().setJavaScriptEnabled(true);
        GlobalVariables.webViewWords.getSettings().setJavaScriptEnabled(true);
        GlobalVariables.webViewWords.getSettings().setLoadWithOverviewMode(true);
        GlobalVariables.webViewWords.getSettings().setUseWideViewPort(true);
        GlobalVariables.webViewWords.getSettings().setBuiltInZoomControls(true);
        GlobalVariables.webViewWords.setWebViewClient(new WebViewClient());
        GlobalVariables.webViewWords.loadUrl(url);

        return v;
    }
}