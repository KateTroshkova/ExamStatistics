package com.troshkova.portfolioprogect.examsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SubjectActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    CircularProgressBar currentProgress;
    EditText requestField;
    TextView markInfo, readyInfo;
    int[] results;
    int min, max;
    String subject;
    int mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        subject=intent.getStringExtra(getString(R.string.subject_param));
        try {
            results = getResources().getIntArray(getArrayId(subject));
            min=getResources().getInteger(getMin(subject));
            max=getResources().getInteger(getMax(subject));
        }
        catch (Resources.NotFoundException e){
            Toast.makeText(this, getString(R.string.subject_exception), Toast.LENGTH_SHORT).show();
            finish();
        }

        TextView actionBarInfo=(TextView)findViewById(R.id.info);
        actionBarInfo.setText(subject);

        requestField=(EditText)findViewById(R.id.editText);
        requestField.setOnEditorActionListener(this);

        currentProgress=(CircularProgressBar)findViewById(R.id.progress);
        currentProgress.setProgress(0);

        markInfo=(TextView)findViewById(R.id.textView);
        readyInfo=(TextView)findViewById(R.id.textView2);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i==EditorInfo.IME_ACTION_GO){
            try {
                int request = Integer.parseInt(requestField.getText().toString());
                if (request<results.length){
                    mark=results[request];
                    currentProgress.setProgressWithAnimation(mark, 2000);
                    markInfo.setText(getString(R.string.mark_info)+mark);
                    readyInfo.setText(getString(R.string.ready_info)+checkProgress(mark));
                }
                else{
                    Toast.makeText(this, getString(R.string.input_exception), Toast.LENGTH_SHORT).show();
                }
            }
            catch(NumberFormatException e){
                Toast.makeText(this, getString(R.string.input_exception), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public void save(View view){
        if (mark>0) {
            DataBaseTask task = new DataBaseTask();
            task.execute();
            Toast.makeText(this, getString(R.string.save_info), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, getString(R.string.empty_exception), Toast.LENGTH_SHORT).show();
        }
    }

    public void show(View view){
        Intent intent=new Intent(this, StatisticsActivity.class);
        intent.putExtra(getString(R.string.subject_param), subject);
        startActivity(intent);
    }

    private class DataBaseTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            SQLiteDatabase database=helper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(getString(R.string.mark_param), mark);
            values.put(getString(R.string.subject_param), subject);
            values.put(getString(R.string.date_param), getTime());
            database.insert(helper.TABLE_NAME, null, values);
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            super.onPostExecute(results);
        }
    }

    private String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private int getArrayId(String subject){
        String[] subjects=getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return R.array.mresult;
        }
        if (subject.equals(subjects[1])){
            return R.array.rresult;
        }
        if (subject.equals(subjects[2])){
            return R.array.iresult;
        }
        if (subject.equals(subjects[3])){
            return R.array.presult;
        }
        throw new Resources.NotFoundException();
    }

    private String checkProgress(int result){
        if (result>=max){
            return getString(R.string.good);
        }
        else{
            if (result>min){
                return getString(R.string.normal);
            }
            else{
                return getString(R.string.low);
            }
        }
    }

    private int getMin(String subject){
        String[] subjects=getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return R.integer.minm;
        }
        if (subject.equals(subjects[1])){
            return R.integer.minr;
        }
        if (subject.equals(subjects[2])){
            return R.integer.mini;
        }
        if (subject.equals(subjects[3])){
            return R.integer.minp;
        }
        throw new Resources.NotFoundException();
    }

    private int getMax(String subject){
        String[] subjects=getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return R.integer.maxm;
        }
        if (subject.equals(subjects[1])){
            return R.integer.maxr;
        }
        if (subject.equals(subjects[2])){
            return R.integer.maxi;
        }
        if (subject.equals(subjects[3])){
            return R.integer.maxp;
        }
        throw new Resources.NotFoundException();
    }
}
