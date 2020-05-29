package rm.android.words;

import android.webkit.WebView;

public class GlobalVariables {
    static WebView webViewDiki;
    static String API_URL = "http://nodejs-words-git-memory.apps.us-west-1.starter.openshift-online.com/api/words";
//    static String API_TOKENS_URL = "http://192.168.253.127:8085/api/tokens";
    static String API_TOKENS_URL = "http://nodejs-words-git-memory.apps.us-west-1.starter.openshift-online.com/api/tokens";

    static String DikiUrl = "https://www.diki.pl/slownik-angielskiego?q=";
    static String WordsUrlJS = "http://nodejs-words-git-memory.apps.us-west-1.starter.openshift-online.com";
    static String WordsUrlBuyList = "http://react-buylist-git-buylists.apps.us-west-1.starter.openshift-online.com/buylists";
    static String DikiLastWord = "";
}
