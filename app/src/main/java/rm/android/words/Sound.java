package rm.android.words;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Sound {
    private static final String logtag = "WORDS.SOUND";
    public void playSound(Context ctx, int soundResId) {
        Log.d(logtag, "playSound:"+soundResId);

        try {
            MediaPlayer mMediaPlayer = MediaPlayer.create(ctx, soundResId);
            mMediaPlayer.start();
        } catch (Exception e) {
            Log.d(logtag, "###playSound:"+e.toString());
        }
        //new SoundTask().execute(MainActivity.getInstance().getApplicationContext(), soundResId);
    }
}
