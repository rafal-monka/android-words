package rm.android.words;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Tab2Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2, container, false);
        //TextView tv1 = v.findViewById(R.id.text1);
        //tv1.setText("WebView DIKI...");

        String url = GlobalVariables.DikiUrl+GlobalVariables.DikiLastWord;
        GlobalVariables.webViewDiki = v.findViewById(R.id.webViewDiki);
        GlobalVariables.webViewDiki.getSettings().setJavaScriptEnabled(true);
        GlobalVariables.webViewDiki.getSettings().setJavaScriptEnabled(true);
        GlobalVariables.webViewDiki.getSettings().setLoadWithOverviewMode(true);
        GlobalVariables.webViewDiki.getSettings().setUseWideViewPort(true);
        GlobalVariables.webViewDiki.getSettings().setBuiltInZoomControls(true);
        GlobalVariables.webViewDiki.setWebViewClient(new WebViewClient());
        GlobalVariables.webViewDiki.loadUrl(url);

        return v;
    }
}