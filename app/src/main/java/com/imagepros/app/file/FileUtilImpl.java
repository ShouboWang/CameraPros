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

    private String path;

    public String getFIlePath() {
        return path;
    }



    public File getOutputMediaFile(int type, String filePath) {

        File mediaFile;

        mediaFile = new File(filePath);

        return mediaFile;
    }
}
