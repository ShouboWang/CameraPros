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
        options.inSampleSize = 1;  // >1 to return smaller image to save memory
        options.inScaled = false;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");



        String path = mediaStorageDir.getPath() + File.separator + "square.jpg";
        Bitmap image = BitmapFactory.decodeFile(path, options);

        if(image == null) {
            Log.e(TAG, "IMAGE NOT FOUND");
        }

        //Drawable drawable = getResources().getDrawable(R.drawable.testimg4);
        //Bitmap image = ((BitmapDrawable) drawable).getBitmap();
        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.circle);

        ImageView view = (ImageView)findViewById(R.id.camera_preview);
        Bitmap imageProcessed = null;

        cannyEdgeDetection = new CannyEdgeDetectionImpl();

        for(int i = 0; i < 1; i++) {

            long start = System.currentTimeMillis();
            imageProcessed = cannyEdgeDetection.applyCannyEdgeDetection(image, 1, 1);
            long milli = System.currentTimeMillis() - start;
            Log.i(TAG,( milli)+"");

        }
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
