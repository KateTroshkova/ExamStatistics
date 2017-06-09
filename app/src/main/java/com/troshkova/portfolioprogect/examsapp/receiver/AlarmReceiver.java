package com.troshkova.portfolioprogect.examsapp.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String subject=intent.getStringExtra(context.getString(R.string.subject_param));
        ResourceProvider resourceProvider=new ResourceProvider(context);
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification=new Notification.Builder(context);
        try {
            notification.setContentText(context.getString(R.string.notification_text));
            notification.setSubText(context.getString(R.string.sub_notification_text));
            notification.setContentTitle(subject);
            notification.setSmallIcon(resourceProvider.getImage(intent.getStringExtra(context.getString(R.string.subject_param))));
        }
        catch (Resources.NotFoundException e){
            notification.setSmallIcon(R.mipmap.ic_launcher);
        }
        notificationManager.notify(0, notification.build());
    }
}
