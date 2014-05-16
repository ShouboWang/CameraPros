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

        cannyEdgeDetection = new CannyEdgeDetectionImpl(intImageArray, width, (int)(intImageArray.length * 0.25), (int)(intImageArray.length/2));
        //cannyEdgeDetection = new CannyEdgeDetectionImpl(intImageArray, width, 0, intImageArray.length/2);
        //Canny edge detection
        cannyEdgeDetection.applyCannyEdgeDetection();

        intImageArray = cannyEdgeDetection.getImageArray();

        //Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, srcImg.getConfig());
        Bitmap afterEffectBitmap = Bitmap.createBitmap(intImageArray, 0, width, width, height, Bitmap.Config.RGB_565);
        return afterEffectBitmap;
    }
}
