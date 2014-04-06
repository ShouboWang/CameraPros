/**
 * <class>CannyEdgeDetectionImpl</class>
 *
 * <summary>
 * This Class applies canny edge detection to an image
 * </summary>
 *
 * <author>Jack Wang</author>
 * <date>March 5 2014</date>
 * */

 package com.imagepros.app.camera.util;

import android.graphics.Bitmap;

import com.imagepros.app.camera.util.filters.GaussianFilterImpl;
import com.imagepros.app.camera.util.filters.HysteresisThresholdImpl;
import com.imagepros.app.camera.util.filters.NonMaxSuppressionImpl;
import com.imagepros.app.camera.util.filters.SobelOperatorImpl;
import com.imagepros.app.camera.util.helper.HelperUtilImpl;

public class CannyEdgeDetectionImpl {

    //Fields
    private int gaussianKernelSize;
    private int gaussianKernelSigma;
    private int hypHighThreshold;
    private int hypLowThreshold;
    private int sobelHighThreshold;
    private int sobelLowThreshold;

    // Setters
    public void setGaussianKernelSize(int gaussianKernelSize) {
        this.gaussianKernelSize = gaussianKernelSize;
    }
    public void setGaussianKernelSigma(int gaussianKernelSigma) {
        this.gaussianKernelSigma = gaussianKernelSigma;
    }
    public void setHypHighThreshold(int hypHighThreshold){
        this.hypHighThreshold = hypHighThreshold;
    }
    public void setHypLowThreshold(int hypLowThreshold){
        this.hypLowThreshold = hypLowThreshold;
    }
    public void setSobelHighThreshold(int sobelHighThreshold){
        this.sobelHighThreshold = sobelHighThreshold;
    }
    public void setSobelLowThreshold(int sobelLowThreshold){
        this.sobelLowThreshold = sobelLowThreshold;
    }

    // Constructor
    // Set the default values for CannyEdge detector
    public CannyEdgeDetectionImpl() {
        gaussianKernelSize = 5;
        gaussianKernelSigma = 3;
        hypHighThreshold = 150;
        hypLowThreshold = 50;
        sobelHighThreshold = 30;
        sobelLowThreshold = -30;
    }

    // Applies canny edge detection to an image
    public Bitmap applyCannyEdgeDetection(Bitmap srcImg) {
        if(srcImg == null) {
            return srcImg;
        }

        // Basic setup of array and variable from source image
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();
        int[] afterEffectArray = new int[width * height];;

        // Convert bitmap to int array
        srcImg.getPixels(afterEffectArray, 0, width, 0, 0, width, height);

        // Create all the necessary classes
        HelperUtilImpl helperUtil = new HelperUtilImpl();
        GaussianFilterImpl gaussianFilter = new GaussianFilterImpl();
        SobelOperatorImpl sobelOperator = new SobelOperatorImpl();
        NonMaxSuppressionImpl nonMaxSuppression = new NonMaxSuppressionImpl();
        HysteresisThresholdImpl hysteresisThreshold = new HysteresisThresholdImpl();

        // Apply the settings
        gaussianFilter.setKernelDeviation(gaussianKernelSigma);
        gaussianFilter.setKernelSize(gaussianKernelSize);
        sobelOperator.setHighValueThreshold(sobelHighThreshold);
        sobelOperator.setLowValueThreshold(sobelLowThreshold);
        hysteresisThreshold.setHighThreshold(hypHighThreshold);
        hysteresisThreshold.setLowThreshold(hypLowThreshold);

        // Apply the effects
        afterEffectArray = helperUtil.toGrayScale(afterEffectArray); // convert to gray scale
        afterEffectArray = gaussianFilter.gaussianConvolution(afterEffectArray, width); // apply blue
        afterEffectArray = sobelOperator.applySobelOperator(afterEffectArray, width); // apply sobel
        char[] thetaArr = sobelOperator.getAtanArr(); //get the theta array from sobel
        afterEffectArray = nonMaxSuppression.applyNonMaxSuppression(afterEffectArray, thetaArr, width); //apply non-max suppression
        afterEffectArray = hysteresisThreshold.applyHysteresisThreshold(afterEffectArray, width); //apply double threshold and hypstersis

        //Convert the array to a black and white image
        afterEffectArray = helperUtil.convertBack(afterEffectArray);

        //Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, srcImg.getConfig());
        Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, Bitmap.Config.RGB_565);
        return afterEffectBitmap;

    }
}
