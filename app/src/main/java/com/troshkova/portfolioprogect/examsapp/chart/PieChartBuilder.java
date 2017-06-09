package com.troshkova.portfolioprogect.examsapp.chart;

import android.content.Context;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.resource.ResourceProvider;
import com.troshkova.portfolioprogect.examsapp.Result;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class PieChartBuilder {

    private PieChart pieChart;
    private Context context;
    private ArrayList<Result> results;
    private int good, normal, low;

    public PieChartBuilder(Context context, PieChart pieChart, ArrayList<Result> results){
        this.context=context;
        this.pieChart=pieChart;
        this.results=results;
        readData();
    }

    public PieChart build(){
        pieChart.setData(getData());
        pieChart.setDescription(" ");
        return pieChart;
    }

    private ArrayList<Entry> createEntries(){
        ArrayList<Entry> entries=new ArrayList<>();
        entries.add(new Entry(good, 0));
        entries.add(new Entry(normal, 1));
        entries.add(new Entry(low, 2));
        return entries;
    }

    private PieDataSet createDataSet(){
        PieDataSet dataSet=new PieDataSet(createEntries(), " ");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        return dataSet;
    }

    private PieData getData(){
        ArrayList<String> labels=new ArrayList<>();
        labels.add(context.getString(R.string.good));
        labels.add(context.getString(R.string.normal));
        labels.add(context.getString(R.string.low));
        return new PieData(labels, createDataSet());
    }

    private void readData() throws EmptyStackException{
        ResourceProvider resourceProvider=new ResourceProvider(context);
        if (results.size()>0) {
            String subject=results.get(0).getSubject();
            for (int i = 0; i < results.size(); i++) {
                if (resourceProvider.getProgressInfo(subject, results.get(i).getMark()).equals(context.getString(R.string.good))) {
                    good++;
                }
                if (resourceProvider.getProgressInfo(subject, results.get(i).getMark()).equals(context.getString(R.string.normal))) {
                    normal++;
                }
                if (resourceProvider.getProgressInfo(subject, results.get(i).getMark()).equals(context.getString(R.string.low))) {
                    low++;
                }
            }
        }
        else{
            throw new EmptyStackException();
        }
    }
}
