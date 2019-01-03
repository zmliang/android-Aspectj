package com.general.code;

import android.support.v4.app.NavUtils;

import java.util.concurrent.TimeUnit;

/**
 * Author: zml
 * Date  : 2019/1/2 - 19:05
 **/
public class StopWatch {
    private long startTime;
    private long endTime;
    private long elaspedTime;

    public StopWatch(){}

    private void reset(){
        startTime = 0;
        endTime = 0;
        elaspedTime = 0;
    }

    public void start(){
        reset();
        startTime = System.currentTimeMillis();
    }

    public void stop(){
        if (startTime!= 0){
            endTime = System.currentTimeMillis();
            elaspedTime = endTime-startTime;
        }else {
            reset();
        }
    }

    public long getTotalTimeMillis(){
        return (elaspedTime!=0) ? TimeUnit.NANOSECONDS.toMillis(endTime-startTime):0;
    }
}
