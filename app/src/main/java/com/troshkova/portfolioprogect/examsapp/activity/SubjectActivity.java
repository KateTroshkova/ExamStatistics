package com.troshkova.portfolioprogect.examsapp.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.troshkova.portfolioprogect.examsapp.dialog.NotificationDialog;
import com.troshkova.portfolioprogect.examsapp.database.DataBaseHelper;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubjectActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    private CircularProgressBar currentProgress;
    private EditText requestField;
    private int[] results;
    private String subject;
    private int mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        subject=getIntent().getStringExtra(getString(R.string.subject_param));
        ((TextView)findViewById(R.id.subject_name_text)).setText(subject);
        requestField=(EditText)findViewById(R.id.user_request_edit_text);
        requestField.setOnEditorActionListener(this);
        currentProgress=(CircularProgressBar)findViewById(R.id.progress);
        currentProgress.setProgress(0);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ResourceProvider resourceProvider=new ResourceProvider(getApplicationContext());
        try {
            results = resourceProvider.getSubjectResultArray(subject);
        }
        catch (Resources.NotFoundException e){
            Toast.makeText(this, getString(R.string.subject_exception), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        ResourceProvider resourceProvider=new ResourceProvider(getApplicationContext());
        if (i==EditorInfo.IME_ACTION_GO){
            try {
                mark=results[Integer.parseInt(requestField.getText().toString())];
                currentProgress.setProgressWithAnimation(mark, 2000);
                ((TextView)findViewById(R.id.result_mark_text))
                        .setText(getString(R.string.mark_info)+mark);
                ((TextView)findViewById(R.id.user_ready_text))
                        .setText(getString(R.string.ready_info)+resourceProvider.getProgressInfo(subject, mark));
                hideKeyboard();
            }
            catch(NumberFormatException e){
                Toast.makeText(this, getString(R.string.input_exception), Toast.LENGTH_SHORT).show();
            }
            catch (ArrayIndexOutOfBoundsException e){
                Toast.makeText(this, getString(R.string.input_exception), Toast.LENGTH_SHORT).show();
            }
            catch(NullPointerException e){
                //keyboard without focus
            }
        }
        return true;
    }

    public void save(View view){
        if (mark>0) {
            DataBaseTask task = new DataBaseTask();
            task.execute();
        }
        else{
            Toast.makeText(this, getString(R.string.empty_exception), Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
            database.insert(DataBaseHelper.TABLE_NAME, null, values);
            database.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            super.onPostExecute(results);
            Toast.makeText(getApplicationContext(), getString(R.string.save_info), Toast.LENGTH_SHORT).show();
        }

        private String getTime(){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            return dateFormat.format(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subject_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_statistics: {
                Intent intent=new Intent(this, StatisticsActivity.class);
                intent.putExtra(getString(R.string.subject_param), subject);
                startActivity(intent);
                return true;
            }
            case R.id.action_notification:{
                NotificationDialog dialog = new NotificationDialog();
                dialog.setSubject(subject);
                dialog.show(getFragmentManager(), "");
                return true;
            }
            default:{
                return false;
            }
        }
    }
}
