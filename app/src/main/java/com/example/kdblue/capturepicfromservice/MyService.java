package com.example.kdblue.capturepicfromservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        takePhoto();
    }

    private void takePhoto() {

        Log.d("kkkk","Preparing to take photo");
        Camera camera = null;

        //int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        //cameraCount = Camera.getNumberOfCameras();
        //for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            // SystemClock.sleep(1000);

            int front = 1;
            //int back=0;

            Camera.getCameraInfo(front, cameraInfo);

            try {
                camera = Camera.open(front);
            } catch (RuntimeException e) {
                Log.d("kkkk","Camera not available: " + 1);
                camera = null;
                //e.printStackTrace();
            }
            try {
                if (null == camera) {
                    Log.d("kkkk","Could not get camera instance");
                } else {
                    Log.d("kkkk","Got the camera, creating the dummy surface texture");
                    //SurfaceTexture dummySurfaceTextureF = new SurfaceTexture(0);
                    try {
                        //camera.setPreviewTexture(dummySurfaceTextureF);
                        camera.setPreviewTexture(new SurfaceTexture(0));
                        camera.startPreview();
                    } catch (Exception e) {
                        Log.d("kkkk","Could not set the surface preview texture");
                        e.printStackTrace();
                    }
                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            File pictureFileDir=new File("/sdcard/CaptureByService");

                            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                                pictureFileDir.mkdirs();
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                            String date = dateFormat.format(new Date());
                            String photoFile = "ServiceClickedPic_" + "_" + date + ".jpg";
                            String filename = pictureFileDir.getPath() + File.separator + photoFile;
                            File mainPicture = new File(filename);

                            try {
                                FileOutputStream fos = new FileOutputStream(mainPicture);
                                fos.write(data);
                                fos.close();
                                Log.d("kkkk","image saved");
                            } catch (Exception error) {
                                Log.d("kkkk","Image could not be saved");
                            }
                            camera.release();
                        }
                    });
                }
            } catch (Exception e) {
                camera.release();
            }


       // }
    }
}
