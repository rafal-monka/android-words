package rm.android.words;

import android.webkit.WebView;

public class GlobalVariables {
    static String CONST_SERVER = "http://words-apps.apps.ca-central-1.starter.openshift-online.com"; //app.os.user1@gmail.com
    //static String CONST_SERVER = "http://nodejs-words-git-memory.apps.us-west-1.starter.openshift-online.com" //rafal.monka.geoloc@gmail.com
    //static String CONST_SERVER = "http://192.168.253.127:8085/api/tokens"; //Papiernicza wifi
    //static String CONST_SERVER = "http://192.168.8.101:8085";//Zbytowa wifi

    static String API_WORDS_URL = CONST_SERVER+"/api/words";
    static String API_TOKENS_URL = CONST_SERVER+"/api/tokens";
    static String WordsUrlJS = CONST_SERVER;
    static WebView webViewDiki;
    static String DikiUrl = "https://www.diki.pl/slownik-angielskiego?q=";
    static String WordsUrlBuyList = "http://react-buylist-git-buylists.apps.us-west-1.starter.openshift-online.com/buylists"; //monka.rafal.geoloc@gmail.com
    static String DikiLastWord = "";
}
