package rm.android.words;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    String logtag = "NotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int noti_id=intent.getIntExtra("notiID",0);
        String _id=intent.getStringExtra("_id");

        if(action.equals(NotificationHelper.POSITIVE_CLICK)) {
            Log.d(logtag,"Pressed + ID="+_id);
            HTTPRequests.MarkAsRemembered(_id);
            cancel(context, noti_id);
        } else if(action.equals(NotificationHelper.NEGATIVE_CLICK)) {
            Log.d(logtag,"Pressed -");
            cancel(context, noti_id);
        }
    }

    public static void cancel(Context context, int id){
        NotificationManager nM = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        nM.cancel(id);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NotificationHelper.REPLY_TEXT_KEY);
        }
        return null;
    }

}
