package com.imagepros.app.display;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by Jack on 3/8/14.
 */
public class CustomPreviewImpl extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "CameraPros_CameraPreviewImpl";
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CustomPreviewImpl(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;

        //Install a SurfaceHolder.Callback so we get notified when the
        //Underlying surface is created and destroyed
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);

        //deprecated setting, but required on Android

        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch(Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        mCamera.stopPreview();
        mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If the preview rotated or rotated,
        // Stop the preview before resizing or reformatting

        if(mSurfaceHolder.getSurface() == null) {
            // Preview surface does not exist
            return;
        }

        // Set the preview size and make any resizing, rotate or
        // reformatting changes here

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    /*
    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        YuvImage temp = new YuvImage(data, camera.getParameters().getPreviewFormat(), camera.getParameters().getPictureSize().width, camera.getParameters().getPictureSize().height, null);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        temp.compressToJpeg(new Rect(0, 0, temp.getWidth(), temp.getHeight()), 80, os);

        Bitmap preview = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);

    }
    */

}
