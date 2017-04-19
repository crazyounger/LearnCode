package com.fuwo.media;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zpf on 2017/4/19.
 */
public class AudioRecorderManager {

    private static final String AUDIO_PREFIX = "fuwo_audio_";
    private  MediaRecorder mMediaRecorder;
    private Context mContext;
    private String mAudioFilePath ;
    private File mAudioFile ;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            int time = (int)msg.obj;
            if(mCallBack != null){
                if(time == 60){
                    mCallBack.onSuccess(mAudioFilePath);
                    stop();
                }else{
                    mCallBack.onUpdate(time);
                }
            }
        }
    };
    private OnAudioCallBack mCallBack;

    public AudioRecorderManager(Context context){
        this.mContext = context ;
    }

    public void init(){
        try {
            mMediaRecorder = new MediaRecorder();
            //设置文件路径
            mAudioFile =new File(getAudioDir(mContext), UUID.randomUUID().toString().replaceAll("-",""));
            mAudioFilePath = mAudioFile.getAbsolutePath() ;
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            //设置麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置文件输出格式  THREE_GPP/MPEG-4/RAW_AMR/Default
            //  THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置音频编码
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        if(mMediaRecorder == null) {
            init();
        }
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            startTimeTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTimeTask() {
        new Thread(new Runnable() {

            int time = 0 ;
            @Override
            public void run() {
                while (true){
                    if(time > 60){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;
                    Message message = handler.obtainMessage();
                    message.obj = time ;
                    message.sendToTarget();
                }
            }
        }).start();
    }

    private File getAudioDir(Context context) {

        File voiceCacheDir = new File(context.getExternalCacheDir(), "voice");
        if (!voiceCacheDir.exists()) {
            voiceCacheDir.mkdirs();
        }
        return voiceCacheDir;
    }

    public void stop(){
        if(mMediaRecorder != null){
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null ;
            if(mCallBack != null){
                mCallBack.onSuccess(mAudioFilePath);
            }
        }
    }

    public void setAudioCallBack(OnAudioCallBack callBack){
        this.mCallBack = callBack ;
    }

    public interface OnAudioCallBack{
        public void onSuccess(String filePath);
        public void onFailure(String errorMsg);
        public void onUpdate(int time);
    }
}
