package com.imagepros.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imagepros.app.camera.AndroidCannyEdgeDetector;
import com.imagepros.app.camera.CustomCameraImpl;
import com.imagepros.app.camera.util.CannyEdgeDetectionImpl;
import com.imagepros.app.display.CustomPreviewImpl;
import com.imagepros.app.file.FileUtilImpl;

import java.io.File;
import java.io.FileOutputStream;

public class ImagePros extends Activity {

    private final String TAG = "CameraPros_ImagePros";

    private Camera mCamera;
    private CustomPreviewImpl mCameraPreview;
    private final CustomCameraImpl mCustomCamera = new CustomCameraImpl();

    private AndroidCannyEdgeDetector androidCannyEdgeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Has started! ");

        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pros2);

        cameraclass();

    }

    private void cameraclass(){


        Log.i(TAG, "I AM IMMMM");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;  // >1 to return smaller image to save memory
        options.inScaled = false;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES), "MyCameraApp");



        String path = mediaStorageDir.getPath() + File.separator + "diamond.jpg";
        Bitmap image = BitmapFactory.decodeFile(path, options);

        ImageView view = (ImageView)findViewById(R.id.camera_preview);
        Bitmap imageProcessed = null;

        view.setImageBitmap(image); //new img


        long start;


        androidCannyEdgeDetector = new AndroidCannyEdgeDetector();

        for(int i = 0; i < 1; i++) {

            start = System.currentTimeMillis();
            imageProcessed = androidCannyEdgeDetector.applyCannyEdgeDetection(image);
            long milli = System.currentTimeMillis() - start;
            Log.i(TAG,( milli)+"");

        }

        Log.d(TAG, "new img");
        view.setImageBitmap(imageProcessed); //new img

        FileUtilImpl file = new FileUtilImpl();
        file.getOutputMediaFile(1);

        FileOutputStream out;
        try {
            out = new FileOutputStream(file.getOutputMediaFile(1));
            imageProcessed.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mCamera = mCustomCamera.getCameraInstance();
 //       mCameraPreview = new CustomPreviewImpl(this, mCamera);

        //FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);

        //preview.addView(mCameraPreview);
        /*

        Button captureButton = (Button)findViewById(R.id.button_capture);

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCamera.takePicture(null,null,mCustomCamera.getPictureCallback());
                    }
                }
        );
        */


    }

}
