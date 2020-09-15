package com.example.souvenir.NotificationsOfMsgs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.souvenir.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    //FirebaseMessagingService class in order to receive messages from the FCM server.
    // This service handles the reception, customization, and display of notifications.
    //separated from MyFirebaseIdService , coz of diff work,this is more easier


    @Override
    public void onNewToken(String s) {//only when new token
        super.onNewToken(s);
        Log.i("MyFirebaseIdService","line 29");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String tok = FirebaseInstanceId.getInstance().getToken();//token id
        Log.i("MyFirebaseIdService","line 29"+tok);
        if (firebaseUser != null )
        {
            updateToken(tok);

        }
    }
    //server side
    private void updateToken(String tok) {
        //every token btw 2 will be updated
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tok);
        ref.child(firebaseUser.getUid()).setValue(token);//update token id
        Log.i("MyFirebaseIdService","line 45");
    }

/////////////////////

    //This service handles the reception, customization, and display of notifications. OverrideOnMessageReceived()
    //method within the service so it will be called whenever a new notification message is received.
    //receiver side
    public void onMessageReceived(RemoteMessage remoteMessage) {


        super.onMessageReceived(remoteMessage);
        Log.i("myfirebasemsg","line 59");
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null && sented.equals(firebaseUser.getUid())) {
            sendNotification(remoteMessage);
        }

    }
    //The next step is to make a network request to FCM server
    private void sendNotification(RemoteMessage remoteMessage) {
        //receiver’s topic
        String user = remoteMessage.getData().get("user");

        String phone = remoteMessage.getData().get("phone");

        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Log.i("myfirebasemsg","line 74");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);//when click on the notification
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);//le2eno 3amleh bl msgactivity aslan getintent 3shan a3abe bayanato
        bundle.putString("userNum",phone);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon)).setContentTitle(title)
                .setContentText(body).setAutoCancel(true).setSound(defaultsound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i=0;
        if(j>0) {
            i=j;
        }

        noti.notify(i, builder.build());


    }


}
