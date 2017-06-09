package com.troshkova.portfolioprogect.examsapp.activity;

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
    private ArrayList<Result> results;
    private int start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        results=new ArrayList<>();

        subject=getIntent().getStringExtra(getString(R.string.subject_param));

        DataBaseTask task=new DataBaseTask();
        task.execute();

        TabHost layout=(TabHost)findViewById(R.id.tabHost);
        layout.setup();
        layout.addTab(createSpec(layout, "screen1", "История", R.id.history_layout));
        layout.addTab(createSpec(layout, "screen2", "График", R.id.line_chart_layout));
        layout.addTab(createSpec(layout, "screen3", "Диаграмма", R.id.pie_chart_layout));
    }

    private TabHost.TabSpec createSpec(TabHost host, String tag, String indicator, int content){
        return host.newTabSpec(tag).setIndicator(indicator).setContent(content);
    }

    public void next(View view){
        start+=10;
        createLineChart(results, start);
        if (start>=results.size()){
            Toast.makeText(this, getString(R.string.last_exception), Toast.LENGTH_SHORT).show();
        }
    }

    public void previous(View view){
        start-=10;
        createLineChart(results, start);
        if (start<0){
            Toast.makeText(this, getString(R.string.first_exception), Toast.LENGTH_SHORT).show();
        }
    }

    private class DataBaseTask extends AsyncTask<Void, Integer, ArrayList<Result>> {

        @Override
        protected ArrayList<Result> doInBackground(Void... voids) {
            return read();
        }

        @Override
        protected void onPostExecute(final ArrayList<Result> data) {
            super.onPostExecute(data);
            results=data;
            createHistoryList();
            if (results.size()>0) {
                ((TextView)findViewById(R.id.max_value_text)).
                        setText(getString(R.string.max) + getMaxResult());
                ((TextView)findViewById(R.id.min_value_text)).
                        setText(getString(R.string.min) + getMinResult());
                ((TextView)findViewById(R.id.middle_value_text)).
                        setText(getString(R.string.middle) + getAverageValue());
            }
            createLineChart(results, results.size()-10);
            createPieChart(results);
        }

        private ArrayList<Result> read(){
            DataBaseHelper helper=new DataBaseHelper(getApplicationContext());
            SQLiteDatabase database=helper.getWritableDatabase();
            ArrayList<Result> result=new ArrayList<>();
            Cursor cursor=database.query(DataBaseHelper.TABLE_NAME, null, DataBaseHelper.COLUMN_SUBJECT +" = ?", new String[]{subject}, null, null, null);
            while(cursor.moveToNext()){
                int mark=cursor.getInt(cursor.getColumnIndex(getString(R.string.mark_param)));
                String date=cursor.getString(cursor.getColumnIndex(getString(R.string.date_param)));
                result.add(new Result(mark, subject, date));
            }
            database.close();
            cursor.close();
            return result;
        }
    }

    private void createPieChart(ArrayList<Result> results){
        PieChart pieChart = (PieChart) findViewById(R.id.pie_chart);
        try {
            PieChartBuilder pieBuilder = new PieChartBuilder(getApplicationContext(), pieChart, results);
            pieChart = pieBuilder.build();
            pieChart.invalidate();
        }
        catch (EmptyStackException e) {
            pieChart.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), getString(R.string.empty_exception), Toast.LENGTH_SHORT).show();
        }
    }

    private void createLineChart(ArrayList<Result> results, int start){
        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);
        try{
            LineChartBuilder lineBuilder=new LineChartBuilder(getApplicationContext(), lineChart, results, start);
            lineChart=lineBuilder.build();
            lineChart.invalidate();
        }
        catch (EmptyStackException e) {
            lineChart.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), getString(R.string.empty_exception), Toast.LENGTH_SHORT).show();
        }
    }

    private void createHistoryList(){
        ListView list=(ListView)findViewById(R.id.history_listview);
        ResultAdapter adapter = new ResultAdapter(getApplicationContext(), results);
        list.setAdapter(adapter);
    }

    private int getMinResult(){
        int min=results.get(0).getMark();
        for(int i=0; i<results.size(); i++){
            if (min>results.get(i).getMark()){
                min=results.get(i).getMark();
            }
        }
        return min;
    }

    private int getMaxResult(){
        int max=results.get(0).getMark();
        for(int i=0; i<results.size(); i++){
            if (max<results.get(i).getMark()){
                max=results.get(i).getMark();
            }
        }
        return max;
    }

    private float getAverageValue(){
        float sum=0;
        for(int i=0; i<results.size(); i++){
            sum+=results.get(i).getMark();
        }
        return sum/results.size();
    }
}
