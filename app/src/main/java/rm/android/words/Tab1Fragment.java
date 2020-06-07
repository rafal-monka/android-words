package rm.android.words;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Tab1Fragment extends Fragment {
    private static final String logtag = "Tab1Fragment";
    private static final int SPEECH_REQUEST_CODE = 0;
    //    final String API_URL = "http://192.168.253.127:8085/api/words";

    Context appContext;
    private EditText words;
    private EditText sentence;
    private EditText translation;
    private TextView examples;
    private TextView hws;
    private String speechPart;
    private TextView statusBar;
    private List<String> audioUrlArr = new ArrayList<String>();
    ImageButton pronounceButton;
    private Sound sound;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = results.get(0);
            Log.d(logtag, "recognized text="+text);
            switch (requestCode) {
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

    private void displaySpeechRecognizer(int field) {

        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-GB");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Field ["+field+"]. Speak now...");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 15000);
        voiceIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        //Start the activity, the intent will be populated with the speech text
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(voiceIntent, field);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);

        // Getting application context
        appContext = getActivity();
        sound = new Sound();
        ImageButton listenButton1 = v.findViewById(R.id.listen1);
        ImageButton listenButton2 = v.findViewById(R.id.listen2);
        ImageButton saveButton = v.findViewById(R.id.save);
        ImageButton translateButton = v.findViewById(R.id.translate);
        pronounceButton = v.findViewById(R.id.pronounceButton);
        words = v.findViewById(R.id.words);
        sentence = v.findViewById(R.id.sentence);
        translation = v.findViewById(R.id.translation);
        hws = v.findViewById(R.id.hws);
        examples = v.findViewById(R.id.examples);
        statusBar = v.findViewById(R.id.statusBar);
        TextView apiurltv = v.findViewById(R.id.apiurltv);
        apiurltv.setText(GlobalVariables.API_WORDS_URL);

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

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateRequest();
            }
        });

        pronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWord();
            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sound.killMediaPlayer();
    }

    private void translateRequest(){
        AsyncHttpClient HttpClient = new AsyncHttpClient();
        String text = words.getText().toString();
        Log.d(logtag, "translateRequest="+text);

        if (!text.equals("")) {
            translation.setText("Searching...");
            examples.setText("");
            hws.setText("");
            sentence.setText("");
            speechPart = "";
            audioUrlArr.clear();
            pronounceButton.setVisibility(View.INVISIBLE);
            HttpClient.get( GlobalVariables.API_WORDS_URL +"/translate/?text="+text, null,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              JSONObject response) {
                            try {
                                Log.d(logtag, response.toString(3));
                                Log.d(logtag, response.getJSONArray("message").toString());
                                String hwsStr = "";
                                String meaningStr = "";
                                String exampleStr = "";
                                String audioUrlStr = "";
                                String partOfSpeechStr = "";

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

                                        //hws
                                        JSONArray hwArr = arr.getJSONObject(i).getJSONArray("hws");
                                        for (int j = 0; j < hwArr.length(); j++) {
                                            hwsStr += hwArr.getString(j) + ", ";
                                        }

                                        //audios
                                        JSONArray audiourls = arr.getJSONObject(i).getJSONArray("audiourls");
                                        for (int j = 0; j < audiourls.length(); j++) {
                                            audioUrlStr += audiourls.getString(j) + ", ";
                                            audioUrlArr.add(audiourls.getString(j));
                                        }

                                        //examples
                                        JSONArray examplesArr = arr.getJSONObject(i).getJSONArray("examples");
                                        for (int j = 0; j < examplesArr.length(); j++) {
                                            exampleStr += "* "+examplesArr.getString(j) + System.lineSeparator();
                                        }

                                        //partOfSpeech
                                        String part = arr.getJSONObject(i).getString("part");
                                        Log.d(logtag, "part="+part);
                                        if (partOfSpeechStr.indexOf(part)==-1) partOfSpeechStr += part + ", ";
                                    }
                                    if (!audioUrlArr.isEmpty()) {
                                        pronounceButton.setVisibility(View.VISIBLE);
                                    }
                                }

                                hws.setText(hwsStr.substring(0,hwsStr.length()-2));
                                translation.setText(meaningStr.substring(0,meaningStr.length()-2));
                                examples.setText(exampleStr);
                                sentence.setText("");
                                speechPart = partOfSpeechStr.substring(0,partOfSpeechStr.length()-2);
                                words.clearFocus();
                            } catch (Exception e) {
                                Log.e(logtag, e.toString());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                            Log.d(logtag, "Request fail! Status code: " + statusCode);
                            sound.playSound(appContext, R.raw.fail);
                            translation.setText("");
                            hws.setText("");
                            speechPart = "";
                            pronounceButton.setVisibility(View.INVISIBLE);
                            examples.setText("");
                            Toast.makeText(
                                    appContext,
                                    "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+(response != null ?response.toString():""),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String str, Throwable throwable) {
                            Log.d(logtag, "Request fail! Status code: " + statusCode);
                            sound.playSound(appContext, R.raw.fail);
                            translation.setText("");
                            hws.setText("");
                            speechPart = "";
                            pronounceButton.setVisibility(View.INVISIBLE);
                            examples.setText("");
                            Toast.makeText(
                                    appContext,
                                    "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+str,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        } else {
            Toast.makeText(
                    appContext,
                    "No phrase to search.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void saveWord() {
        Log.d(logtag, "saveWord..."+GlobalVariables.API_WORDS_URL);
        String _phrase = words.getText().toString();
        String _hws = hws.getText().toString();
        String _speechpart = speechPart;
        Log.d(logtag, "speechPart="+speechPart);
        String _translation = translation.getText().toString();
        String _examples = examples.getText().toString();
        Log.d(logtag, "1["+_phrase+"]["+_translation+"]");
        if (_phrase.equals("") || _translation.equals("")) {
            Toast.makeText(
                    appContext,
                    "Phrase and translation are required",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Log.d(logtag, "2["+_phrase+"]["+_translation+"]");
        statusBar.setVisibility(View.VISIBLE);
        statusBar.setText("Saving...");

        RequestParams params = new RequestParams();
        params.put("phrase", _phrase);
        params.put("hws", _hws);
        params.put("speechpart", _speechpart);
        params.put("sentence", sentence.getText().toString());
        params.put("translation", _translation);
        params.put("examples", _examples);
        params.put("tags", "A"); //###default A?

        AsyncHttpClient HttpClient = new AsyncHttpClient();
        HttpClient.post( GlobalVariables.API_WORDS_URL +"/create/", params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        try {
                            Log.d(logtag, response.toString(3));
                            sound.playSound(appContext, R.raw.ting);
                            statusBar.setText("");
                            statusBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(
                                    appContext,
                                    "Saved #"+response.get("_id"), //response.toString(2),
                                    Toast.LENGTH_SHORT
                            ).show();
                        } catch (Exception e) {
                            Log.e(logtag, e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        Log.d(logtag, "Request fail! Status code: " + statusCode);
                        String errTxt = "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+(response != null ?response.toString():"");
                        sound.playSound(appContext, R.raw.fail);
                        statusBar.setText(errTxt);
                        Toast.makeText(
                                appContext,
                                errTxt,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String str, Throwable throwable) {
                        Log.d(logtag, throwable.toString());
                        String errTxt = "Error. statusCode="+statusCode+"\n"+throwable.toString()+"\n"+str;
                        statusBar.setText(errTxt);
                        sound.playSound(appContext, R.raw.fail);
                        Toast.makeText(
                                appContext,
                                errTxt,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    public void playAudio() {
        try {
            Log.d(logtag, "playAudio");
            Log.d(logtag, audioUrlArr.toString());
            if (!audioUrlArr.isEmpty()) {
                String url = audioUrlArr.get(0).toString();
                Log.d(logtag, url);
                sound.playAudioUrl(url);
            }
            //
        } catch (Exception e) {
            Log.e(logtag, e.toString());
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}