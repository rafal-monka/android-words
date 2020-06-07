package rm.android.words;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HTTPRequests {
    static String logtag = "HTTPRequests";

    public static void MarkAsRemembered(String _id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("counter", -1);

        client.put(GlobalVariables.API_WORDS_URL + "/" +_id, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Do something with response
                Log.d(logtag, response.toString());
//                Sound.playSound(???, R.raw.ting);
            }
        });
    }
}
