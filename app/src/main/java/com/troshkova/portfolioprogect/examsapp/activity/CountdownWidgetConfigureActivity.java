package com.troshkova.portfolioprogect.examsapp.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.troshkova.portfolioprogect.examsapp.widget.CountdownWidget;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

public class CountdownWidgetConfigureActivity extends Activity{

    private int mAppWidgetId;
    private ListView subjectList;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.countdown_widget_configure);
        Intent intent = getIntent();
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        subjectList = (ListView) findViewById(R.id.widget_subject_list);
        subjectList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.subjects, android.R.layout.simple_list_item_single_choice);
        subjectList.setAdapter(adapter);
    }

    public void configure(View view){
        try {
            if (subjectList.getCheckedItemPosition() > 0) {
                savePreferences();
                CountdownWidget.updateAppWidget(getApplicationContext(), AppWidgetManager.getInstance(this), mAppWidgetId);
                closeActivity();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.create_widget_exception), Toast.LENGTH_SHORT).show();
            }
        }
        catch(Resources.NotFoundException e){
            Toast.makeText(this, getString(R.string.subject_exception), Toast.LENGTH_SHORT).show();
        }
    }

    private void savePreferences(){
        ResourceProvider resourceProvider = new ResourceProvider(getApplicationContext());
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String subject = getResources().getStringArray(R.array.subjects)[subjectList.getCheckedItemPosition()];
        editor.putString(mAppWidgetId + getString(R.string.subject_param), subject);
        editor.putLong(mAppWidgetId + getString(R.string.date_param), resourceProvider.getTime(subject));
        editor.apply();
    }

    private void closeActivity(){
        Intent finishIntent = new Intent();
        finishIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, finishIntent);
        finish();
    }
}

