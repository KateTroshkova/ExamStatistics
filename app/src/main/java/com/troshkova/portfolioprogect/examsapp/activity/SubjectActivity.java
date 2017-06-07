package com.troshkova.portfolioprogect.examsapp.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.troshkova.portfolioprogect.examsapp.AlarmReceiver;
import com.troshkova.portfolioprogect.examsapp.database.DataBaseHelper;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SubjectActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    private CircularProgressBar currentProgress;
    private EditText requestField;
    private TextView markInfo, readyInfo;
    private int[] results;
    private int min, max;
    private String subject;
    private int mark;
    private ResourceProvider resourceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        resourceProvider=new ResourceProvider(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        subject=intent.getStringExtra(getString(R.string.subject_param));
        try {
            results = resourceProvider.getSubjectResultArray(subject);
            min=resourceProvider.getMin(subject);
            max=resourceProvider.getMax(subject);
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
                mark=results[request];
                currentProgress.setProgressWithAnimation(mark, 2000);
                markInfo.setText(getString(R.string.mark_info)+mark);
                readyInfo.setText(getString(R.string.ready_info)+resourceProvider.getProgressInfo(mark, max, min));
            }
            catch(NumberFormatException e){
                Toast.makeText(this, getString(R.string.input_exception), Toast.LENGTH_SHORT).show();
            }
            catch (ArrayIndexOutOfBoundsException e){
                Toast.makeText(this, getString(R.string.input_exception), Toast.LENGTH_SHORT).show();
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
                dialog.show(getFragmentManager(), "");
                return true;
            }
            default:{
                return false;
            }
        }
    }

    private class NotificationDialog extends DialogFragment implements TimePicker.OnTimeChangedListener{

        private int hour, minute;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder=new AlertDialog.Builder(SubjectActivity.this);
            LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout=inflater.inflate(R.layout.notification_dialog, null, false);
            builder.setView(layout);
            final TimePicker picker=(TimePicker)layout.findViewById(R.id.timePicker);
            picker.setIs24HourView(true);
            picker.setOnTimeChangedListener(this);
            builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(getString(R.string.subject_param), subject);
                    int code=0;
                    try {
                        ResourceProvider provider = new ResourceProvider(SubjectActivity.this);
                        code=provider.getId(subject);
                    }
                    catch (Resources.NotFoundException e){
                        code=0;
                    }
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(SubjectActivity.this, code, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, alarmIntent);
                }
            });
            builder.setNeutralButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int code=0;
                    try {
                        ResourceProvider provider = new ResourceProvider(SubjectActivity.this);
                        code=provider.getId(subject);
                    }
                    catch (Resources.NotFoundException e){
                        code=0;
                    }
                    Intent intent = new Intent(SubjectActivity.this, AlarmReceiver.class);
                    PendingIntent cancelIntent = PendingIntent.getBroadcast(SubjectActivity.this, code, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
                    manager.cancel(cancelIntent);
                }
            });
            return builder.create();
        }

        @Override
        public void onTimeChanged(TimePicker timePicker, int i, int i1) {
            hour=i;
            minute=i1;
        }
    }
}
