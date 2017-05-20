package com.troshkova.portfolioprogect.examsapp;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by asus-pc on 20.05.2017.
 */
public class ResourceProvider {

    private Context context;

    public ResourceProvider(Context context){
        this.context=context;
    }

    public int[] getSubjectResultArray(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return context.getResources().getIntArray(R.array.mresult);
        }
        if (subject.equals(subjects[1])){
            return context.getResources().getIntArray(R.array.rresult);
        }
        if (subject.equals(subjects[2])){
            return context.getResources().getIntArray(R.array.iresult);
        }
        if (subject.equals(subjects[3])){
            return context.getResources().getIntArray(R.array.presult);
        }
        throw new Resources.NotFoundException();
    }

    public int getMax(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return context.getResources().getInteger(R.integer.maxm);
        }
        if (subject.equals(subjects[1])){
            return context.getResources().getInteger(R.integer.maxr);
        }
        if (subject.equals(subjects[2])){
            return context.getResources().getInteger(R.integer.maxi);
        }
        if (subject.equals(subjects[3])){
            return context.getResources().getInteger(R.integer.maxp);
        }
        throw new Resources.NotFoundException();
    }

    public int getMin(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return context.getResources().getInteger(R.integer.minm);
        }
        if (subject.equals(subjects[1])){
            return context.getResources().getInteger(R.integer.minr);
        }
        if (subject.equals(subjects[2])){
            return context.getResources().getInteger(R.integer.mini);
        }
        if (subject.equals(subjects[3])){
            return context.getResources().getInteger(R.integer.minp);
        }
        throw new Resources.NotFoundException();
    }

    public String getProgressInfo(int result, int max, int min){
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
}
