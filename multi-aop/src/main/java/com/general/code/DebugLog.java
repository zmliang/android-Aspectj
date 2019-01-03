package com.general.code;

import android.util.Log;

/**
 * Author: zml
 * Date  : 2019/1/2 - 19:09
 **/
public class DebugLog {
    private DebugLog(){}

    public static void log(String tag,String message){
        Log.i(tag,message);
    }
}
