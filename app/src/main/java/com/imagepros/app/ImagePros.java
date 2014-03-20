package com.imagepros.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.imagepros.app.camera.CustomCameraImpl;
import com.imagepros.app.camera.util.CannyEdgeDetection.GaussianFilterImpl;
import com.imagepros.app.camera.util.helper.HelperUtilImpl;
import com.imagepros.app.display.CustomPreviewImpl;
import com.imagepros.app.file.FileUtilImpl;

import java.io.FileOutputStream;

public class ImagePros extends Activity {

    private final String TAG = "CameraPros_MainActivity";

    private Camera mCamera;
    private CustomPreviewImpl mCameraPreview;
    private final CustomCameraImpl mCustomCamera = new CustomCameraImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Has started! ");

        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_pros);


        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.testimag2);


        Log.i(TAG, (image == null) +" Testss" );

        GaussianFilterImpl gi = new GaussianFilterImpl();
        HelperUtilImpl helper = new HelperUtilImpl();
        image = helper.toGrayscale(image);

        ImageView view = (ImageView)findViewById(R.id.camera_preview);

        long start = System.currentTimeMillis();
        //Bitmap image2 = gi.applyGaussianBlur(image);
        Bitmap image2 = gi.gaussianConvolution(image, 3 ,1);
        long milli = System.currentTimeMillis() - start;
        Log.i(TAG,( milli/1000)+"");


        long milliend = milli + 3000;
        //image = gi.applyGaussianBlur(image);

        //view.setImageBitmap(image); //org img

        while(System.currentTimeMillis() < milliend)
        {

        }


        view.setImageBitmap(image2); //new img

        Log.i(TAG,"newimg");

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
        FileUtilImpl fi = new FileUtilImpl();
        String path = fi.getOutputMediaFile(1);
        FileOutputStream out;
        try {
            out = new FileOutputStream(path);
            image2.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
