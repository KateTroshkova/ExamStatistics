package com.troshkova.portfolioprogect.examsapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder=new Notification.Builder(context);
        builder.setSubText(context.getString(R.string.sub_notification_text));
        builder.setContentText(context.getString(R.string.notification_text));
        builder.setContentTitle(intent.getStringExtra(context.getString(R.string.subject_param)));
        try {
            ResourceProvider provider=new ResourceProvider(context);
            builder.setSmallIcon(provider.getImage(intent.getStringExtra(context.getString(R.string.subject_param))));
        }
        catch (Resources.NotFoundException e){
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        manager.notify(0, builder.build());
    }
}
