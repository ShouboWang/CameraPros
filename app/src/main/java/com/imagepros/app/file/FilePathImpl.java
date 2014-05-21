package com.imagepros.app.file;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jack on 2014-05-18.
 */
public class FilePathImpl {

    public String getPath() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp2");


        if(!mediaStorageDir.exists()) {

            if(!mediaStorageDir.mkdirs()) {
                Log.e("MainActivityCAMERA", "Failed to create Directory");
                return null;
            };
        }

        //Create media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String path = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        return path;
    }
}
