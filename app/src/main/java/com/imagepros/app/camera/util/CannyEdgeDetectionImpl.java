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

import com.imagepros.app.camera.util.Filters.GaussianFilterImpl;
import com.imagepros.app.camera.util.Filters.HysteresisThresholdImpl;
import com.imagepros.app.camera.util.Filters.NonMaxSuppressionImpl;
import com.imagepros.app.camera.util.Filters.SobelOperatorImpl;
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
    public int[] applyCannyEdgeDetection(int[] imgArray, int width) {

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
        imgArray = helperUtil.toGrayScale(imgArray); // convert to gray scale
        imgArray = gaussianFilter.gaussianConvolution(imgArray, width); // apply blue
        imgArray = sobelOperator.applySobelOperator(imgArray, width); // apply sobel
        char[] thetaArr = sobelOperator.getAtanArr(); //get the theta array from sobel
        imgArray = nonMaxSuppression.applyNonMaxSuppression(imgArray, thetaArr, width); //apply non-max suppression
        imgArray = hysteresisThreshold.applyHysteresisThreshold(imgArray, width); //apply double threshold and hypstersis

        //Convert the array to a black and white image
        imgArray = helperUtil.convertBack(imgArray);

        return imgArray;
    }
}
