package com.troshkova.portfolioprogect.examsapp.resource;

import android.content.Context;
import android.content.res.Resources;

import com.troshkova.portfolioprogect.examsapp.R;

public class ResourceProvider {

    private Context context;

    public ResourceProvider(Context context){
        this.context=context;
    }

    public int[] getSubjectResultArray(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return context.getResources().getIntArray(R.array.aresult);
        }
        if (subject.equals(subjects[1])){
            return context.getResources().getIntArray(R.array.bresult);
        }
        if (subject.equals(subjects[2])){
            return context.getResources().getIntArray(R.array.gresult);
        }
        if (subject.equals(subjects[3])){
            return context.getResources().getIntArray(R.array.iresult);
        }
        if (subject.equals(subjects[4])){
            return context.getResources().getIntArray(R.array.hresult);
        }
        if (subject.equals(subjects[5])){
            return context.getResources().getIntArray(R.array.lresult);
        }
        if (subject.equals(subjects[6])){
            return context.getResources().getIntArray(R.array.mresult);
        }
        if (subject.equals(subjects[7])){
            return context.getResources().getIntArray(R.array.oresult);
        }
        if (subject.equals(subjects[8])){
            return context.getResources().getIntArray(R.array.rresult);
        }
        if (subject.equals(subjects[9])){
            return context.getResources().getIntArray(R.array.presult);
        }
        if (subject.equals(subjects[10])){
            return context.getResources().getIntArray(R.array.cresult);
        }
        throw new Resources.NotFoundException();
    }

    public int getMax(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return context.getResources().getInteger(R.integer.maxa);
        }
        if (subject.equals(subjects[1])){
            return context.getResources().getInteger(R.integer.maxb);
        }
        if (subject.equals(subjects[2])){
            return context.getResources().getInteger(R.integer.maxg);
        }
        if (subject.equals(subjects[3])){
            return context.getResources().getInteger(R.integer.maxi);
        }
        if (subject.equals(subjects[4])){
            return context.getResources().getInteger(R.integer.maxh);
        }
        if (subject.equals(subjects[5])){
            return context.getResources().getInteger(R.integer.maxl);
        }
        if (subject.equals(subjects[6])){
            return context.getResources().getInteger(R.integer.maxm);
        }
        if (subject.equals(subjects[7])){
            return context.getResources().getInteger(R.integer.maxo);
        }
        if (subject.equals(subjects[8])){
            return context.getResources().getInteger(R.integer.maxr);
        }
        if (subject.equals(subjects[9])){
            return context.getResources().getInteger(R.integer.maxp);
        }
        if (subject.equals(subjects[10])){
            return context.getResources().getInteger(R.integer.maxc);
        }
        throw new Resources.NotFoundException();
    }

    public int getMin(String subject){
        String[] subjects=context.getResources().getStringArray(R.array.subjects);
        if (subject.equals(subjects[0])){
            return context.getResources().getInteger(R.integer.mina);
        }
        if (subject.equals(subjects[1])){
            return context.getResources().getInteger(R.integer.minb);
        }
        if (subject.equals(subjects[2])){
            return context.getResources().getInteger(R.integer.ming);
        }
        if (subject.equals(subjects[3])){
            return context.getResources().getInteger(R.integer.mini);
        }
        if (subject.equals(subjects[4])){
            return context.getResources().getInteger(R.integer.minh);
        }
        if (subject.equals(subjects[5])){
            return context.getResources().getInteger(R.integer.minl);
        }
        if (subject.equals(subjects[6])){
            return context.getResources().getInteger(R.integer.minm);
        }
        if (subject.equals(subjects[7])){
            return context.getResources().getInteger(R.integer.mino);
        }
        if (subject.equals(subjects[8])){
            return context.getResources().getInteger(R.integer.minr);
        }
        if (subject.equals(subjects[9])){
            return context.getResources().getInteger(R.integer.minp);
        }
        if (subject.equals(subjects[10])){
            return context.getResources().getInteger(R.integer.minc);
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
