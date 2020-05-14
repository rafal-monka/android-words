package rm.android.words;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final String logtag = "WORDS";
//    final String API_URL = "http://192.168.253.127:8085/api/words";
    final String API_URL = "http://nodejs-words-git-memory.apps.us-west-1.starter.openshift-online.com/api/words";
    private static final int SPEECH_REQUEST_CODE = 0;
    private EditText words;
    private EditText sentence;
    private EditText translation;
    private TextView examples;
    private int listenField;
    private Sound sound;

    private void displaySpeechRecognizer(int field) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //Start the activity, the intent will be populated with the speech text
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.listenField = field;
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton listenButton1 = (ImageButton) findViewById(R.id.listen1);
        ImageButton listenButton2 = (ImageButton) findViewById(R.id.listen2);
        ImageButton saveButton = (ImageButton) findViewById(R.id.save);

        sound = new Sound();
        words = findViewById(R.id.words);
        sentence = findViewById(R.id.sentence);
        translation = findViewById(R.id.translation);
        examples = findViewById(R.id.examples);

        TextView apiurltv = findViewById(R.id.apiurltv);
        apiurltv.setText(API_URL);

        words.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        listenButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySpeechRecognizer(1);
            }
        });

        listenButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySpeechRecognizer(2);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWord();
            }
        });
    }

    public void onTranslateClick(View v) {
        translateRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = results.get(0);
            Log.d(logtag, "recognized text="+text);
            switch (this.listenField) {
                case 1:
                    words.setText(text);
                    translateRequest();
                    break;
                case 2:
                    sentence.setText(text);
                    break;
            }
        }
    }

    private void translateRequest(){
        AsyncHttpClient HttpClient = new AsyncHttpClient();
        String text = words.getText().toString();
        Log.d(logtag, "translateRequest="+text);

        if (!text.equals("")) {
            translation.setText("Searching...");
            examples.setText("");
            HttpClient.get( API_URL+"/translate/?text="+text, null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        try {
                            Log.d(logtag, response.toString(3));
                            Log.d(logtag, response.getJSONArray("message").toString());
                            String meaningStr = "";
                            String exampleStr = "";

                            if (false) {
                                meaningStr = response.getJSONArray("message").toString();
                                exampleStr = "---";
                            } else {
                                JSONArray arr = response.getJSONArray("message");
                                for (int i = 0; i < arr.length(); i++) {
                                    //meanings
                                    JSONArray meaningsArr = arr.getJSONObject(i).getJSONArray("meanings");
                                    for (int j = 0; j < meaningsArr.length(); j++) {
                                        meaningStr += meaningsArr.getString(j) + ", ";
                                    }

                                    //examples
                                    JSONArray examplesArr = arr.getJSONObject(i).getJSONArray("examples");
                                    for (int k = 0; k < examplesArr.length(); k++) {
                                        exampleStr += examplesArr.getString(k) + "\n";
                                    }
                                }
                            }

                            translation.setText(meaningStr.substring(0,meaningStr.length()-2));
                            examples.setText(exampleStr);
                            sentence.setText("");
                            words.clearFocus();
                        } catch (Exception e) {
                            Log.e(logtag, e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        Log.d(logtag, "Request fail! Status code: " + statusCode);
                        sound.playSound(getApplicationContext(), R.raw.fail);
                        translation.setText("");
                        examples.setText("");
                        Toast.makeText(
                                getApplicationContext(),
                                "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+(response != null ?response.toString():""),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String str, Throwable throwable) {
                        Log.d(logtag, "Request fail! Status code: " + statusCode);
                        sound.playSound(getApplicationContext(), R.raw.fail);
                        translation.setText("");
                        examples.setText("");
                        Toast.makeText(
                                getApplicationContext(),
                                "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+str,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "No phrase to search.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void saveWord() {
        Log.d(logtag, "saveWord..."+API_URL);

        String _phrase = words.getText().toString();
        String _hws = "__HWS__";
        String _speechpart = "__SPEECHPART__";
        String _translation = translation.getText().toString();
        String _examples = examples.getText().toString();
        Log.d(logtag, "1["+_phrase+"]["+_translation+"]");
        if (_phrase.equals("") || _translation.equals("")) {
            Toast.makeText(
                    getApplicationContext(),
                    "Phrase and translation are required",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Log.d(logtag, "2["+_phrase+"]["+_translation+"]");

        RequestParams params = new RequestParams();
        params.put("phrase", _phrase);
        params.put("hws", _hws);
        params.put("speechpart", _speechpart);
        params.put("sentence", sentence.getText().toString());
        params.put("translation", _translation);
        params.put("examples", _examples);

        AsyncHttpClient HttpClient = new AsyncHttpClient();
        HttpClient.post( API_URL+"/create/", params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        try {
                            Log.d(logtag, response.toString(3));
                            sound.playSound(getApplicationContext(), R.raw.ting);
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Saved #"+response.get("id"), //response.toString(2),
                                    Toast.LENGTH_SHORT
                            ).show();
                        } catch (Exception e) {
                            Log.e(logtag, e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        Log.d(logtag, "Request fail! Status code: " + statusCode);
                        sound.playSound(getApplicationContext(), R.raw.fail);
                        Toast.makeText(
                                getApplicationContext(),
                                "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+(response != null ?response.toString():""),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String str, Throwable throwable) {
                        Log.d(logtag, throwable.toString());
                        sound.playSound(getApplicationContext(), R.raw.fail);
                        Toast.makeText(
                                getApplicationContext(),
                                "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+str,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
