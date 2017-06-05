package com.troshkova.portfolioprogect.examsapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.troshkova.portfolioprogect.examsapp.database.DataBaseHelper;
import com.troshkova.portfolioprogect.examsapp.chart.LineChartBuilder;
import com.troshkova.portfolioprogect.examsapp.chart.PieChartBuilder;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.Result;
import com.troshkova.portfolioprogect.examsapp.adapter.ResultAdapter;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class StatisticsActivity extends AppCompatActivity {

    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Intent intent=getIntent();
        subject=intent.getStringExtra(getString(R.string.subject_param));
        DataBaseTask task=new DataBaseTask();
        task.execute();

        TabHost layout=(TabHost)findViewById(R.id.tabHost);
        layout.setup();
        TabHost.TabSpec screen1=layout.newTabSpec("screen1");
        screen1.setIndicator("история");
        screen1.setContent(R.id.linearLayout);
        layout.addTab(screen1);

        TabHost.TabSpec screen2=layout.newTabSpec("screen2");
        screen2.setIndicator("График");
        screen2.setContent(R.id.linearLayout2);
        layout.addTab(screen2);

        TabHost.TabSpec screen3=layout.newTabSpec("screen3");
        screen3.setIndicator("Диаграмма");
        screen3.setContent(R.id.linearLayout3);
        layout.addTab(screen3);
    }


    private class DataBaseTask extends AsyncTask<Void, Integer, ArrayList<Result>> {
        @Override
        protected ArrayList<Result> doInBackground(Void... voids) {
            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            SQLiteDatabase database=helper.getWritableDatabase();
            return read(helper, database);
        }

        @Override
        protected void onPostExecute(final ArrayList<Result> results) {
            super.onPostExecute(results);
            ListView list=(ListView)findViewById(R.id.listView2);
            ResultAdapter adapter =new ResultAdapter(getApplicationContext(), results);
            list.setAdapter(adapter);

            TextView high=(TextView)findViewById(R.id.textView5);
            TextView low=(TextView)findViewById(R.id.textView6);
            TextView middle=(TextView)findViewById(R.id.textView7);

            if (results.size()>0) {
                int min=results.get(0).getMark();
                int max=results.get(0).getMark();
                float sum=0;

                for(int i=0; i<results.size(); i++){
                    if (min>results.get(i).getMark()){
                        min=results.get(i).getMark();
                    }
                    if (max<results.get(i).getMark()){
                        max=results.get(i).getMark();
                    }
                    sum+=results.get(i).getMark();
                }

                high.setText(getString(R.string.max) + max);
                low.setText(getString(R.string.min) + min);
                middle.setText(getString(R.string.middle) + (sum / results.size()));
            }

            PieChart pieChart = (PieChart) findViewById(R.id.piechart);
            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            try {
                pieChart = (PieChart) findViewById(R.id.piechart);
                PieChartBuilder pieBuilder = new PieChartBuilder(getApplicationContext(), pieChart, results);
                pieChart = pieBuilder.build();
                pieChart.invalidate();

                LineChartBuilder lineBuilder=new LineChartBuilder(getApplicationContext(), lineChart, results);
                lineChart=lineBuilder.build();
                lineChart.invalidate();
            }
            catch (EmptyStackException e) {
                pieChart.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.empty_exception), Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<Result> read(DataBaseHelper helper, SQLiteDatabase database){
            ArrayList<Result> result=new ArrayList<>();
            Cursor cursor=database.query(helper.TABLE_NAME, null, helper.COLUMN_SUBJECT+" = ?", new String[]{subject}, null, null, null);
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
