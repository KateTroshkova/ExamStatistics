package com.troshkova.portfolioprogect.examsapp;

import android.content.Intent;
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

public class SubjectActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    CircularProgressBar currentProgress;
    EditText requestField;
    int[] results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        String subject=intent.getStringExtra(getString(R.string.subject_param));
        try {
            results = getResources().getIntArray(getArrayId(subject));
        }
        catch (ArrayIndexOutOfBoundsException e){
            Toast.makeText(this, getString(R.string.subject_exception), Toast.LENGTH_SHORT).show();
            finish();
        }

        TextView actionBarInfo=(TextView)findViewById(R.id.info);
        actionBarInfo.setText(subject);

        requestField=(EditText)findViewById(R.id.editText);
        requestField.setOnEditorActionListener(this);

        currentProgress=(CircularProgressBar)findViewById(R.id.progress);
        currentProgress.setProgress(0);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i==EditorInfo.IME_ACTION_GO){
            try {
                int request = Integer.parseInt(requestField.getText().toString());
                if (request<results.length){
                    currentProgress.setProgressWithAnimation(results[request], 2000);
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
        throw new ArrayIndexOutOfBoundsException();
    }
}
