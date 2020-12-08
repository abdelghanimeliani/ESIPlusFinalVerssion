package com.example.esiapp.adapters;

import com.google.firebase.database.ServerValue;

import java.security.Key;

public class Notetodo {
    private String text;
    private long time;
    private String TodoKey;
    private String uname;
    private Object timeStamp;
    public Notetodo(String text, long time, String uname) {
        this.text = text;
        this.time = time;
        this.uname = uname;
        this.timeStamp = ServerValue.TIMESTAMP;


    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }



    public String getTodoKey() {
        return TodoKey;
    }

    public void setTodoKey(String todoKey) {
        TodoKey = todoKey;
    }



    public Notetodo() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}