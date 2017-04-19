package com.fuwo.media.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by zpf on 2017/4/19.
 */

public class ViewFind {

    public static <T extends View> T findView(Activity activity , @IdRes int resId){
        if(resId <= 0 || activity == null) return null ;
        return (T) activity.findViewById(resId);
    }

    public static <T extends  View> T findView(View view ,@IdRes int redId){
        if(redId <= 0 || view == null) return null ;
        return (T)view.findViewById(redId);
    }
}
