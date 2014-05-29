/**
 * <class>AndroidCannyEdgeDetector</class>
 *
 * <summary>
 * This Class is used for the implementation of canny edge detector
 * for android environment
 * </summary>
 *
 * <author>Jack Wang</author>
 * <date>March 5 2014</date>
 * */
package com.imagepros.app.camera;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.imagepros.app.camera.util.CannyEdgeDetectionImpl;

public class AndroidCannyEdgeDetector {

    private CannyEdgeDetectionImpl cannyEdgeDetection;

    // Setters
    public void setGaussianKernelSize(int gaussianKernelSize) {
        cannyEdgeDetection.setGaussianKernelSize(gaussianKernelSize);
    }
    public void setGaussianKernelSigma(int gaussianKernelSigma) {
        cannyEdgeDetection.setGaussianKernelSigma(gaussianKernelSigma);
    }
    public void setHypHighThreshold(int hypHighThreshold){
        cannyEdgeDetection.setHypHighThreshold(hypHighThreshold);
    }
    public void setHypLowThreshold(int hypLowThreshold){
        cannyEdgeDetection.setHypLowThreshold(hypLowThreshold);
    }
    public void setSobelHighThreshold(int sobelHighThreshold){
        cannyEdgeDetection.setSobelHighThreshold(sobelHighThreshold);
    }
    public void setSobelLowThreshold(int sobelLowThreshold){
        cannyEdgeDetection.setSobelLowThreshold(sobelLowThreshold);
    }

    // Constructor
    // Initialize CannyEdgeDetection
    public AndroidCannyEdgeDetector()
    {

    }

    public Bitmap applyCannyEdgeDetection(Bitmap srcImage) {
        if(srcImage == null) {
            return srcImage;
        }

        // Basic setup of array and variable from source image
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int[] intImageArray = new int[width * height];;

        // Convert bitmap to int array
        srcImage.getPixels(intImageArray, 0, width, 0, 0, width, height);

        /*
        //cannyEdgeDetection = new CannyEdgeDetectionImpl(intImageArray, width, (int)(intImageArray.length * 0.25), (int)(intImageArray.length/2));
        cannyEdgeDetection = new CannyEdgeDetectionImpl(intImageArray, width);
        //Canny edge detection
        cannyEdgeDetection.applyCannyEdgeDetection();

        intImageArray = cannyEdgeDetection.getImageArray();
        */

        //Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, srcImg.getConfig());

        //Thread single = new Thread(new CannyEdgeWorker(width, 0, intImageArray.length , intImageArray));




        int threadNum = 3;
        Thread[] threadArr = new Thread[threadNum];

        for(int i = 0; i < threadNum; i++)
        {
            threadArr[i] = new Thread(new CannyEdgeWorker(width, (int)(intImageArray.length * (i/(double)threadNum)), (int)(intImageArray.length * ((i+1)/(double)threadNum)), intImageArray));
            threadArr[i].start();
        }
        boolean done = false;
        while(!done)
        {
            for(int i = 0; i < threadNum; i++)
            {
                if(threadArr[i].isAlive()) {
                    done = false;
                    break;
                }
                done = true;
            }
        }


        //single.start();
        //while(single.isAlive()){}
        //Runnable thread = new CannyEdgeWorker(width, 0, (int)(intImageArray.length), intImageArray);




        Bitmap afterEffectBitmap = Bitmap.createBitmap(intImageArray, 0, width, width, height, Bitmap.Config.RGB_565);

        return afterEffectBitmap;
    }

    private class CannyEdgeWorker implements Runnable {

        private final int start_;
        private final int finish_;
        private final int width_;
        private int[] image_;

        CannyEdgeWorker(int width, int start, int end, int[] image) {
            start_ = start;
            finish_ = end;
            width_ = width;
            image_ = image;
        }

        @Override
        public void run() {
            CannyEdgeDetectionImpl cannyEdgeDetection = new CannyEdgeDetectionImpl(image_, width_, start_, finish_);
            cannyEdgeDetection.setGaussianKernelSize(5);
            cannyEdgeDetection.applyCannyEdgeDetection();
            image_ = cannyEdgeDetection.getImageArray();
        }
    }
}

