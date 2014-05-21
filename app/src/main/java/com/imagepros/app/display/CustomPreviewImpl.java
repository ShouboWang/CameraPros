package com.imagepros.app.display;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

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

    @Override
    protected void onDraw(Canvas canvas) {
      //  canvas.drawRect(new Rect((int) Math.random() * 100,
       //         (int) Math.random() * 100, 200, 200), rectanglePaint);
        Log.w(this.getClass().getName(), "On Draw Called");
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
