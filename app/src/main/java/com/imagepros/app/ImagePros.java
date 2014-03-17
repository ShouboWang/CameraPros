package com.imagepros.app;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.imagepros.app.camera.CustomCameraImpl;
import com.imagepros.app.display.CustomPreviewImpl;

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


        mCamera = mCustomCamera.getCameraInstance();
        mCameraPreview = new CustomPreviewImpl(this, mCamera);

        FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);

        preview.addView(mCameraPreview);

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
