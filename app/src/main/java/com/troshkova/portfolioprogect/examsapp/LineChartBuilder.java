package com.troshkova.portfolioprogect.examsapp;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class LineChartBuilder {

    private LineChart lineChart;
    private Context context;
    private ArrayList<Result> results;
    private ArrayList<Entry> entries;
    private ArrayList<String> label;

    public LineChartBuilder(Context context, LineChart lineChart, ArrayList<Result> results){
        this.context=context;
        this.lineChart=lineChart;
        this.results=results;
        readData();
    }

    public LineChart build(){
        lineChart.setData(getData());
        lineChart.setDescription("");
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Toast.makeText(context, results.get(entries.indexOf(e)).getDate(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return lineChart;
    }

    private LineData getData(){
       return new LineData(label, createDataSet());
    }

    private LineDataSet createDataSet(){
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(context.getResources().getColor(R.color.colorAccent));
        dataSet.setValueTextColor(Color.BLACK);
        return dataSet;
    }

    private void readData(){
        entries=new ArrayList<Entry>();
        label=new ArrayList<>();
        for(int i=0; i<results.size(); i++){
            entries.add(new Entry(results.get(i).getMark(), i));
            label.add("");
        }
    }
}
