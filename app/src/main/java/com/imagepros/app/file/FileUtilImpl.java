package com.imagepros.app.file;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtilImpl {

    private final String TAG = "CameraPros_CameraFileUtilImpl";

    /// Creates the file with destination folder
    //org File
    public String getOutputMediaFile(int type) {


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");


        if(!mediaStorageDir.exists()) {

            if(!mediaStorageDir.mkdirs()) {
                Log.e("MainActivityCAMERA", "Failed to create Directory");
                return null;
            };
        }

        //Create media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";

        //return mediaFile;
    }
}
