package rm.android.words;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Sound {
    MediaPlayer mediaPlayer;
    List<String> audiosArr = new ArrayList<String>();

    private static final String logtag = "WORDS.SOUND";
    public void playSound(Context ctx, int soundResId) {
        Log.d(logtag, "playSound:"+soundResId);

        try {
            mediaPlayer = MediaPlayer.create(ctx, soundResId);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.d(logtag, "###playSound:"+e.toString());
        }
        //new SoundTask().execute(MainActivity.getInstance().getApplicationContext(), soundResId);
    }

    public void playAudioUrl(String url) throws Exception
    {
        Log.d(logtag, "playAudioUrl:"+url);
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(logtag, "mediaPlayer onCompletion");
                mediaPlayer.release();
                mediaPlayer = null;

            }
        });
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public void killMediaPlayer() {
        if(mediaPlayer!=null) {
            Log.d(logtag, "mediaPlayer!=null killMediaPlayer");
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            }  catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
