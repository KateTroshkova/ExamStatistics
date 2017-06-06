package com.troshkova.portfolioprogect.examsapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.activity.CountdownWidgetConfigureActivity;

import java.util.Date;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CountdownWidgetConfigureActivity CountdownWidgetConfigureActivity}
 */
public class CountdownWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SharedPreferences preferences=context.getSharedPreferences(context.getString(R.string.preference_name), Context.MODE_PRIVATE);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.countdown_widget);
        views.setTextViewText(R.id.textView8, preferences.getString(appWidgetId+context.getString(R.string.subject_param), ""));
        long days=preferences.getLong(appWidgetId+context.getString(R.string.date_param), 0);
        Date currentDate=new Date(System.currentTimeMillis());
        days=days-currentDate.getTime()/1000/3600/24;
        views.setTextViewText(R.id.textView9, context.getString(R.string.until_the_end)+"\n"+days+" "+getCorrectWord(days));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static String getCorrectWord(long days){
        if (days%10==1){
            return "день";
        }
        if (days%10==2 || days%10==3 || days%10==4){
            return "дня";
        }
        return "дней";
    }
}

