package com.imagepros.app.camera;

import android.provider.MediaStore;
import android.util.Log;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.content.pm.PackageManager;

import com.imagepros.app.file.FileUtilImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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

    /**Gets the back facing camera**/
    public Camera getCameraInstance() {
        Camera camera = null;





        try {
            camera = Camera.open();
            Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size size = getOptimalSize(sizes);
            parameters.setPreviewSize(size.width, size.height);
            parameters.setPictureSize(size.width, size.height);
            camera.setParameters(parameters);



        } catch(Exception ex) {
            Log.e(TAG, ex.toString());
        }

        return camera;
    }

    private Camera.Size getOptimalSize (List<Camera.Size> sizes) {

        return sizes.get(0);
    }


    private String path_;
    public Camera.PictureCallback getPictureCallback(String path) {
        path_ = path;
        return mPicture;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {

            FileUtilImpl cameraFileUtil = new FileUtilImpl();
            Log.d("ininin", "in");

            File pictureFile;
            pictureFile = cameraFileUtil.getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, path_);

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
