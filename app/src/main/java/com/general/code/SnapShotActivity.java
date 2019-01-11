package com.general.code;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;


/**
 * Author: zml
 * Date  : 2019/1/3 - 15:55
 * 拍摄功能
 **/
public class SnapShotActivity extends AppCompatActivity implements SurfaceHolder.Callback,View.OnClickListener
,Camera.PictureCallback{
    private static final String TAG = "SnapShotActivity";

    SurfaceView cameraView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    Handler timerHandler = new Handler();

    Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            camera.takePicture(null,null,null,SnapShotActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        cameraView = findViewById(R.id.camera_view);
        surfaceHolder = cameraView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        cameraView.setFocusable(true);
        cameraView.setFocusableInTouchMode(true);
        cameraView.setClickable(true);

        cameraView.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        try {
            Camera.Parameters parameters = camera.getParameters();
            if (getResources().getConfiguration().orientation!=
                    Configuration.ORIENTATION_LANDSCAPE){
                parameters.set("orientation","portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }else {
                parameters.set("orientation","landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            List<String> colorEffects = parameters.getSupportedColorEffects();
            Iterator<String> iterator = colorEffects.iterator();
            while (iterator.hasNext()){
                String currentEffect = iterator.next();
                if (currentEffect.equals(Camera.Parameters.EFFECT_SOLARIZE)){
                    parameters.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
                    break;
                }
            }
            camera.setParameters(parameters);
            camera.setPreviewDisplay(surfaceHolder);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
            camera.release();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void onClick(View view) {
        camera.takePicture(null,null,this);
        //timerHandler.postDelayed(timerTask,10000);
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new ContentValues());
        try {
            OutputStream os = getContentResolver().openOutputStream(imageUri);
            os.write(bytes);
            os.flush();
            os.close();
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }
        camera.startPreview();
    }
}
