package com.fuwo.learn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zpf on 2017/4/19.
 */

public abstract  class BasActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        initViews();
        doLogic();
    }

    protected void doLogic() {
    }

    protected abstract void initViews();

    protected abstract int getContentResId();

    public void showToast(String toast){
        if(!TextUtils.isEmpty(toast)){
            Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
        }
    }

    public void log(String tag,String log){
        if(!TextUtils.isEmpty(log)){
            Log.d(tag,log);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
