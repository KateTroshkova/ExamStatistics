package com.troshkova.portfolioprogect.examsapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private ArrayList<Result> results;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        results=new ArrayList<>();
        Intent intent=getIntent();
        subject=intent.getStringExtra(getString(R.string.subject_param));
        DataBaseTask task=new DataBaseTask();
        task.execute();
    }


    private class DataBaseTask extends AsyncTask<Void, Integer, ArrayList<Result>> {
        @Override
        protected ArrayList<Result> doInBackground(Void... voids) {
            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            SQLiteDatabase database=helper.getWritableDatabase();
            return read(helper, database);
        }

        @Override
        protected void onPostExecute(ArrayList<Result> results) {
            super.onPostExecute(results);
            ListView list=(ListView)findViewById(R.id.listView2);
            ResultAdapter adapter =new ResultAdapter(getApplicationContext(), results);
            list.setAdapter(adapter);
        }

        private ArrayList<Result> read(DataBaseHelper helper, SQLiteDatabase database){
            ArrayList<Result> result=new ArrayList<>();
            Cursor cursor=database.query(helper.TABLE_NAME, null, helper.COLUMN_SUBJECT+" = ?", new String[]{subject}, null, null, helper.COLUMN_DATE);
            while(cursor.moveToNext()){
                int mark=cursor.getInt(cursor.getColumnIndex(getString(R.string.mark_param)));
                String date=cursor.getString(cursor.getColumnIndex(getString(R.string.date_param)));
                result.add(new Result(mark, subject, date));
            }
            database.close();
            return result;
        }
    }
}
