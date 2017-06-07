package com.troshkova.portfolioprogect.examsapp.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.troshkova.portfolioprogect.examsapp.widget.CountdownWidget;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

import java.util.List;

/**
 * The configuration screen for the {@link CountdownWidget CountdownWidget} AppWidget.
 */
public class CountdownWidgetConfigureActivity extends Activity{

    private int mAppWidgetId;
    //private String subject;
    private ListView list;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.countdown_widget_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        list = (ListView) findViewById(R.id.spinner);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.subjects, android.R.layout.simple_list_item_single_choice);
        list.setAdapter(adapter);
    }

    public void configure(View view){
        try {
            if (list.getCheckedItemPosition() > 0) {
                ResourceProvider resourceProvider = new ResourceProvider(getApplicationContext());
                SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String subject = getResources().getStringArray(R.array.subjects)[list.getCheckedItemPosition()];
                editor.putString(mAppWidgetId + getString(R.string.subject_param), subject);
                editor.putLong(mAppWidgetId + getString(R.string.date_param), resourceProvider.getTime(subject));
                editor.commit();

                CountdownWidget.updateAppWidget(getApplicationContext(), AppWidgetManager.getInstance(this), mAppWidgetId);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.create_widget_exception), Toast.LENGTH_SHORT).show();
            }
        }
        catch(Resources.NotFoundException e){
            Toast.makeText(this, getString(R.string.subject_exception), Toast.LENGTH_SHORT).show();
        }
    }
}

