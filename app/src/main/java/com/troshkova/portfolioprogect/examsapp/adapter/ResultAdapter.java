package com.troshkova.portfolioprogect.examsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.troshkova.portfolioprogect.examsapp.R;
import com.troshkova.portfolioprogect.examsapp.Result;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {

    private ArrayList<Result> results;
    private Context context;

    public ResultAdapter(Context context, ArrayList<Result> results){
        this.context=context;
        this.results=results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int i) {
        return results.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout=view;
        if (layout==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.result_item, null);
        }
        ((TextView)layout.findViewById(R.id.textView3)).setText(context.getString(R.string.mark_info)+results.get(i).getMark());
        ((TextView)layout.findViewById(R.id.textView4)).setText(results.get(i).getDate());
        CircularProgressBar currentProgress=(CircularProgressBar)layout.findViewById(R.id.progress_small);
        currentProgress.setProgress(results.get(i).getMark());
        return layout;
    }
}
