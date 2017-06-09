package com.troshkova.portfolioprogect.examsapp.dialog;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.receiver.AlarmReceiver;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

import java.util.Calendar;

public class NotificationDialog extends DialogFragment implements TimePicker.OnTimeChangedListener{

    private int hour, minute;
    private static String subject;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=(LayoutInflater)getActivity().
                getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        final View layout=inflater.inflate(R.layout.notification_dialog, null, false);
        builder.setView(layout);
        final TimePicker picker=(TimePicker)layout.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        picker.setOnTimeChangedListener(this);
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setAlarm();
            }
        });
        builder.setNeutralButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelAlarm();
            }
        });
        return builder.create();
    }

    public void setSubject(String subject){
        this.subject=subject;
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        hour=i;
        minute=i1;
    }

    private void setAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra(getString(R.string.subject_param), subject);
        int id;
        try {
            ResourceProvider resourceProvider = new ResourceProvider(getActivity());
            id=resourceProvider.getId(subject);
        }
        catch (Resources.NotFoundException e){
            id=42;
        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager=(AlarmManager)getActivity().
                getSystemService(getActivity().ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private void cancelAlarm(){
        int id;
        try {
            ResourceProvider resourceProvider = new ResourceProvider(getActivity());
            id=resourceProvider.getId(subject);
        }
        catch (Resources.NotFoundException e){
            id=42;
        }
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(getActivity(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager=(AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
        manager.cancel(cancelIntent);
    }
}