package com.fuwo.media;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by zpf on 2017/4/19.
 */

public class VideoRecorderManager implements SurfaceHolder.Callback{

    private static final String TAG = VideoRecorderManager.class.getSimpleName();
    private Context mContext;
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder surfaceHolder;

    private Camera  camera ;
    private boolean isOpenCamera = true;
    private int mTimeCount;
    private Timer mTimer;
    private int mRecordMaxTime = 60;

    public VideoRecorderManager(Context context , SurfaceHolder surfaceHolder){
        this.mContext = context ;
        this.surfaceHolder = surfaceHolder ;
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
    }


    public void initRecord(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (camera != null)
            mMediaRecorder.setCamera(camera);
        mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
//        mMediaRecorder.setVideoSize(200, 250);// 设置分辨率：
//         mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
//        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// 设置帧频率，然后就清晰了
        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        mMediaRecorder.setOutputFile(getVideoDir(mContext).getAbsolutePath() + "/" + UUID.randomUUID().toString() + ".mp4");
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            if (camera == null)// 如果未打开摄像头，则打开
                initCamera();
            initRecord();
            mTimeCount = 0;// 时间计数器重新赋值
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mTimeCount++;
                    if (mTimeCount == mRecordMaxTime) {// 达到指定时间，停止拍摄
                        stop();
                    }
                }
            }, 0, 1000);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        stopRecord();
        releaseRecorder();
        freeCameraResource();
    }

    private void releaseRecorder() {
        if(mMediaRecorder != null){
            mMediaRecorder.release();
            mMediaRecorder = null ;
        }
    }

    private void stopRecord() {
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private File getVideoDir(Context context) {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File voiceCacheDir = new File(context.getExternalCacheDir(), "voice");
            if (!voiceCacheDir.exists()) {
                voiceCacheDir.mkdirs();
            }
            return voiceCacheDir;
        }else{
            File file = new File(Environment.getExternalStorageDirectory(),"fuwo-video");
            if(!file.exists()){
                file.mkdirs();
            }
            return file ;
        }
    }


    private void initCamera() throws IOException {
        if(camera != null)
            freeCameraResource();
        try{
            camera = Camera.open();

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            freeCameraResource();
        }
        if(camera == null) return ;
        setCameraParams();
        camera.setDisplayOrientation(90);
        camera.setPreviewDisplay(surfaceHolder);
        camera.cancelAutoFocus();
        camera.startPreview();
        camera.unlock();
        isOpenCamera = false ;
    }

    private void setCameraParams() {
        if(camera != null) {
            Camera.Parameters params = camera.getParameters();
            params.set("orientation", "portrait");
            camera.setParameters(params);
        }
    }

    private void freeCameraResource() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            surfaceHolder = holder ;
            initCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder ;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceHolder = holder ;
        if (!isOpenCamera)
            return;
        freeCameraResource();
    }



}
