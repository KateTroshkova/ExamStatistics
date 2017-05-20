package com.troshkova.portfolioprogect.examsapp;

import java.io.Serializable;

/**
 * Created by asus-pc on 19.05.2017.
 */
public class Result implements Serializable{

    private int mark;
    private String date;
    private String subject;

    public Result(int mark, String subject, String date){
        this.mark=mark;
        this.subject=subject;
        this.date=date;
    }

    public int getMark() {
        return mark;
    }

    public String getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }
}
