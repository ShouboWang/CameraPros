package com.imagepros.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imagepros.app.camera.CustomCameraImpl;
import com.imagepros.app.camera.util.CannyEdgeDetectionImpl;
import com.imagepros.app.display.CustomPreviewImpl;

import java.io.File;

public class ImagePros extends Activity {

    private final String TAG = "CameraPros_ImagePros";

    private Camera mCamera;
    private CustomPreviewImpl mCameraPreview;
    private final CustomCameraImpl mCustomCamera = new CustomCameraImpl();

    private CannyEdgeDetectionImpl cannyEdgeDetection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Has started! ");

        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pros);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");




        String path = mediaStorageDir.getPath() + File.separator + "testimg4.jpg";
        Bitmap image = BitmapFactory.decodeFile(path, options);

        //Drawable drawable = getResources().getDrawable(R.drawable.testimg4);
        //Bitmap image = ((BitmapDrawable) drawable).getBitmap();
        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.testimg4);

        ImageView view = (ImageView)findViewById(R.id.camera_preview);

        cannyEdgeDetection = new CannyEdgeDetectionImpl();

        long start = System.currentTimeMillis();
        Bitmap imageProcessed = cannyEdgeDetection.applyCannyEdgeDetection(image, 5, 1);
        long milli = System.currentTimeMillis() - start;
        Log.i(TAG,( milli)+"");

        view.setImageBitmap(imageProcessed); //new img


        //mCamera = mCustomCamera.getCameraInstance();
        //mCameraPreview = new CustomPreviewImpl(this, mCamera);

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
