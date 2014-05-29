package com.imagepros.app;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.GridView;
import android.view.ViewGroup;
import android.content.res.Resources;
import android.content.Context;
import android.widget.TextView;


import com.imagepros.app.camera.AndroidCannyEdgeDetector;
import com.imagepros.app.camera.CustomCameraImpl;
import com.imagepros.app.camera.util.CannyEdgeDetectionImpl;
import com.imagepros.app.display.CustomPreviewImpl;
import com.imagepros.app.file.FilePathImpl;
import com.imagepros.app.file.FileUtilImpl;
import com.imagepros.app.display.adapter.GridViewImageAdapter;
import com.imagepros.app.display.helper.AppConstant;
import com.imagepros.app.display.helper.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImagePros extends Activity {

    private final String TAG = "CameraPros_ImagePros";

    private Camera mCamera;
    private CustomPreviewImpl mPreview;
    private final CustomCameraImpl mCustomCamera = new CustomCameraImpl();
    private AndroidCannyEdgeDetector androidCannyEdgeDetector;

    private  String path_;


    private Utils utils;
    private ArrayList<String> totalImagePaths = new ArrayList<String>();
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_ayout);


        View cameraButton = this.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                setContentView(R.layout.activity_image_pros3);

                // Create an instance of Camera
                mCamera = mCustomCamera.getCameraInstance();

                // Create our Preview view and set it as the content of our activity.
                mPreview = new CustomPreviewImpl(getApplicationContext(), mCamera);

                //mPreview = new CustomPreviewImpl(this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);

                // Add a listener to the Capture button
                Button captureButton = (Button) findViewById(R.id.button_capture);
                captureButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get an image from the camera
                                FilePathImpl fp = new FilePathImpl();
                                String path = fp.getPath();
                                path_ = path;
                                mCamera.takePicture(null, null, mPicture);
                             }
                        }
                );
            }
         });

        //gridView = (GridView) findViewById(R.id.grid_view);
        utils = new Utils(this);

        // loading all image paths from SD card
        totalImagePaths = utils.getFilePaths();
        ///storage/emulated/0/Pictures/MyCameraApp2/IMG_20140520_023345.jpg
        //String date_header = "";
        //String[] temp = totalImagePaths.get(0).split("_");
        //date_header = temp[1];
        //System.out.println(date_header);

        //View v = vi.inflate(R.layout.activity_main_ayout,null);

        //for (int i = 0; i < totalImagePaths.size(); i++){

          //  if (totalImagePaths.get(i).split("_")[1].equals(date_header)){
          //      imagePaths.add(totalImagePaths.get(i));
          //  } else {
                //create the image view with imagePaths (same date)

                //System.out.println(date_header);
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.activity_grid_view, null);

                //TextView section_header = (TextView) v.findViewById(R.id.date_bar);
                //section_header.setText(date_header);

                gridView = (GridView) v.findViewById(R.id.grid_view);
                // Initilizing Grid View
                InitilizeGridLayout();

                // Gridview adapter
                adapter = new GridViewImageAdapter(ImagePros.this, totalImagePaths, columnWidth);

                // setting grid view adapter
                gridView.setAdapter(adapter);

                //insert into main view

                View insertPoint = findViewById(R.id.insert_point);
                ((ViewGroup) insertPoint).addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                //set the new date_header
                //date_header = totalImagePaths.get(i).split("_")[1];

                //imagePaths.clear();

                //imagePaths.add(totalImagePaths.get(i));
           // }

       // }

    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
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


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;  // >1 to return smaller image to save memory
            options.inScaled = false;


            Bitmap image = BitmapFactory.decodeFile(path_, options);

            androidCannyEdgeDetector = new AndroidCannyEdgeDetector();
            Bitmap imageProcessed = null;
            imageProcessed = androidCannyEdgeDetector.applyCannyEdgeDetection(image);


            setContentView(R.layout.activity_image_pros2);
            ImageView view2 = (ImageView)findViewById(R.id.camera_preview);
            view2.setImageBitmap(imageProcessed); //new img

            File imageChange = new File(path_);
            if(imageChange.exists()){
                imageChange.delete();
            }

            FileOutputStream out;
            try {
                out = new FileOutputStream(path_);
                imageProcessed.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

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
        file.getOutputMediaFile(1, "");

        FileOutputStream out;
        try {
            out = new FileOutputStream(file.getOutputMediaFile(1, ""));
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
