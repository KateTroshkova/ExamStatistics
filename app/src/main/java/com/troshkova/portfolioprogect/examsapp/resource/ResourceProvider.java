package com.troshkova.portfolioprogect.examsapp.resource;

import android.content.Context;
import android.content.res.Resources;

import com.troshkova.portfolioprogect.examsapp.R;

import java.util.GregorianCalendar;

public class ResourceProvider {

    private Context context;

    private int[] images={R.drawable.english, R.drawable.biology, R.drawable.geography, R.drawable.it,
            R.drawable.history, R.drawable.literature, R.drawable.math, R.drawable.society,
            R.drawable.russian, R.drawable.phisics, R.drawable.chemistry};

    private int[] results={R.array.aresult, R.array.bresult, R.array.gresult, R.array.iresult,
            R.array.hresult, R.array.lresult, R.array.mresult, R.array.oresult,
            R.array.rresult, R.array.presult, R.array.cresult};

    private int[] max={R.integer.maxa, R.integer.maxb, R.integer.maxg, R.integer.maxi,
            R.integer.maxh, R.integer.maxl, R.integer.maxm, R.integer.maxo,
            R.integer.maxr, R.integer.maxp, R.integer.maxc};

    private int[] min={R.integer.mina, R.integer.minb, R.integer.ming, R.integer.mini,
            R.integer.minh, R.integer.minl, R.integer.minm, R.integer.mino,
            R.integer.minr, R.integer.minp, R.integer.minc};

    private int[] date={R.string.adate, R.string.bdate, R.string.gdate, R.string.idate,
            R.string.hdate, R.string.ldate, R.string.mdate, R.string.odate,
            R.string.rdate, R.string.pdate, R.string.cdate};

    public ResourceProvider(Context context){
        this.context=context;
    }

    public int[] getSubjectResultArray(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        for(int i=0; i<subjects.length; i++){
            if (subject.equals(subjects[i])){
                return context.getResources().getIntArray(results[i]);
            }
        }
        throw new Resources.NotFoundException();
    }

    public String getProgressInfo(String subject, int result){
        int min=getMin(subject);
        int max=getMax(subject);
        if (result>=max){
            return context.getResources().getString(R.string.good);
        }
        else{
            if (result>min){
                return context.getResources().getString(R.string.normal);
            }
            else{
                return context.getResources().getString(R.string.low);
            }
        }
    }

    public long getTime(String subject){
        String time=getSubjectTime(subject);
        String[] ymd = time.split(":");
        return new GregorianCalendar(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2])).
                getTime().getTime() / 1000 / 3600 / 24;
    }

    public int getImage(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        for(int i=0; i<subjects.length; i++){
            if (subject.equals(subjects[i])){
                return images[i];
            }
        }
        throw new Resources.NotFoundException();
    }

    public int getId(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        for(int i=0; i<subjects.length; i++){
            if (subject.equals(subjects[i])){
                return i;
            }
        }
        throw new Resources.NotFoundException();
    }

    private int getMax(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        for(int i=0; i<subjects.length; i++){
            if (subject.equals(subjects[i])){
                return context.getResources().getInteger(max[i]);
            }
        }
        throw new Resources.NotFoundException();
    }

    private int getMin(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        for(int i=0; i<subjects.length; i++){
            if (subject.equals(subjects[i])){
                return context.getResources().getInteger(min[i]);
            }
        }
        throw new Resources.NotFoundException();
    }

    private String getSubjectTime(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        for(int i=0; i<subjects.length; i++){
            if (subject.equals(subjects[i])){
                return context.getResources().getString(date[i]);
            }
        }
        throw new Resources.NotFoundException();
    }
}
