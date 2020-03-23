package com.root.rootcheck.utils;

import android.util.Log;

/**
 * Created by Mahesh Manseta on 01/09/16.
 */
public class MyLog {

    private static final boolean isLive = true;

    public static void e(String message){
        if(!isLive)
        Log.e("INSTA_","LOG: "+message);
    }

    public static void e(String tag, String message){
        if(!isLive)
        Log.e("INSTA_"+tag,"LOG: "+message);
    }

    public static void e(int message){
        if(!isLive)
        Log.e("INSTA_","LOG: "+message);
    }

}
