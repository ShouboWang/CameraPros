package com.imagepros.app.camera;

import android.provider.MediaStore;
import android.util.Log;

import android.content.Context;
import android.hardware.Camera;
import android.content.pm.PackageManager;

import com.imagepros.app.file.FileUtilImpl;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jack on 3/8/14.
 */
public class CustomCameraImpl {

    private final String TAG = "CustomCamera Class";

    /** Check if camera is available for the android device **/
    private boolean checkCameraHardwareAvailable(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /** Get an instance of the Camera **/
    /**Gets the back facing camera**/
    public Camera getCameraInstance() {
        Camera camera = null;

        try {
            camera = Camera.open();
        } catch(Exception ex) {
            Log.e(TAG, ex.toString());
        }

        return camera;
    }


    public Camera.PictureCallback getPictureCallback() {
        return mPicture;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            FileUtilImpl cameraFileUtil = new FileUtilImpl();

            File pictureFile = cameraFileUtil.getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

            if(pictureFile == null) {
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes);
                fos.close();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    };



}
