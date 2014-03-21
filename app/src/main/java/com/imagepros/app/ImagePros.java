package com.imagepros.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imagepros.app.camera.CustomCameraImpl;
import com.imagepros.app.camera.util.CannyEdgeDetectionImpl;
import com.imagepros.app.display.CustomPreviewImpl;

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


        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.testimage);

        ImageView view = (ImageView)findViewById(R.id.camera_preview);

        cannyEdgeDetection = new CannyEdgeDetectionImpl();

        long start = System.currentTimeMillis();
        Bitmap imageProcessed = cannyEdgeDetection.applyCannyEdgeDetection(image, 7, 5);
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
