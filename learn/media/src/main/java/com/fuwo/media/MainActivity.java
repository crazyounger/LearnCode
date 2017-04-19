package com.fuwo.media;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwo.media.utils.ViewFind;
import com.fuwo.media.weixin.WeixinSmallVedioActivity;

public class MainActivity extends BaseActivity implements AudioRecorderManager.OnAudioCallBack{


    private static final int FLAG_VIDEO = 1 ;
    private static final int FLAG_AUDIO = 2 ;
    private LinearLayout mContentLayout;
    private Button mVideoBtn;
    private Button mAudioBtn;
    private Button mStartBtn;
    private Button mEndBtn;
    private SurfaceView mSufaceView;
    private TextView mAudioTimeTv;

    private int mRecordFlag ;

    private AudioRecorderManager mAudioManager = null ;
    private VideoRecorderManager mVideoManager;

    @Override
    protected int getContentResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mAudioBtn = ViewFind.findView(this,R.id.btn_audio_record);
        mVideoBtn = ViewFind.findView(this,R.id.btn_video_record);
        mContentLayout = ViewFind.findView(this,R.id.layout_content);

        mStartBtn = ViewFind.findView(this,R.id.btn_start);
        mEndBtn = ViewFind.findView(this,R.id.btn_end);

        mSufaceView = ViewFind.findView(this,R.id.sufaceview);
        mAudioTimeTv = ViewFind.findView(this,R.id.tv_audio_time);

        mAudioBtn.setOnClickListener(this);
        mVideoBtn.setOnClickListener(this);
        mStartBtn.setOnClickListener(this);
        mEndBtn.setOnClickListener(this);
        ViewFind.findView(this,R.id.btn_weixin_small_video).setOnClickListener(this);

        mAudioManager = new AudioRecorderManager(this);
        mAudioManager.setAudioCallBack(this);

        mVideoManager = new VideoRecorderManager(this,mSufaceView.getHolder());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_audio_record:
                onAudioClick();
                break;
            case R.id.btn_video_record:
                onVideoClick();
                break;
            case R.id.btn_start:
                onStartClick();
                break;
            case R.id.btn_end:
                onEndClick();
                break;
            case R.id.btn_weixin_small_video:
                startActivity(new Intent(this, WeixinSmallVedioActivity.class));
                break;
        }
    }

    private void onEndClick() {
        mEndBtn.setEnabled(false);
        mStartBtn.setEnabled(true);
        if(mRecordFlag == FLAG_AUDIO){
            onAudioEnd();
        }else if(mRecordFlag == FLAG_VIDEO){
            onVideoEnd();
        }
    }


    private void onVideoStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
        } else {
            startRecordVideo();
        }
    }

    private void startRecordVideo() {
        mSufaceView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mVideoManager.start();
            }
        },800);

    }

    private void onVideoEnd() {
        mVideoManager.stop();
        mSufaceView.setVisibility(View.GONE);
        mStartBtn.setEnabled(true);
        mEndBtn.setEnabled(false);
    }

    private void onStartClick() {
        mStartBtn.setEnabled(false);
        mEndBtn.setEnabled(true);

        if(mRecordFlag == FLAG_AUDIO){
            onAudioStart();
        }else if(mRecordFlag == FLAG_VIDEO){
            onVideoStart();
        }
    }



    private void onAudioStart() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},111);
        } else {
            startRecording();
        }

    }

    private void startRecording() {
        mAudioTimeTv.setVisibility(View.VISIBLE);
        mAudioTimeTv.setText("");
        mAudioManager.start();
    }

    private void onAudioEnd() {
        mAudioTimeTv.setVisibility(View.GONE);
        mAudioManager.stop();
        mStartBtn.setEnabled(true);
        mEndBtn.setEnabled(false);
    }


    private void onVideoClick() {
        mAudioBtn.setEnabled(false);
        mContentLayout.setVisibility(View.VISIBLE);
        mRecordFlag = FLAG_VIDEO;

    }

    private void onAudioClick() {
        mVideoBtn.setEnabled(false);
        mContentLayout.setVisibility(View.VISIBLE);
        mRecordFlag = FLAG_AUDIO ;
    }

    @Override
    public void onSuccess(String filePath) {
        showToast("audio path is " + filePath);
    }

    @Override
    public void onFailure(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void onUpdate(int time) {
        mAudioTimeTv.setText("已录制"+String.valueOf(time)+"s");
    }
}
