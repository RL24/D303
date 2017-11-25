package ucol.a1599116.tuckbox.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.dialog.Callback;
import ucol.a1599116.tuckbox.util.dialog.DialogButton;

/**
 * Firebase chat message helper
 * Used to handle any and all notifications sent via firebase notifications
 */
public class FCMHelper extends FirebaseMessagingService {

    /**
     * Handle receiving messages from Firebase
     *
     * @param message The message to display
     */
    @Override
    public void onMessageReceived(RemoteMessage message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(message.getNotification().getTitle()).setContentText(message.getNotification().getBody());
        if (notificationManager != null)
            notificationManager.notify(1, builder.build());
    }

}
