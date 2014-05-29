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

import android.util.Log;

//import com.imagepros.app.camera.util.Filters.BoxBlur;
import com.imagepros.app.camera.util.Filters.GaussianFilterImpl;
import com.imagepros.app.camera.util.Filters.HysteresisThresholdImpl;
import com.imagepros.app.camera.util.Filters.NonMaxSuppressionImpl;
import com.imagepros.app.camera.util.Filters.SobelOperatorImpl;
import com.imagepros.app.camera.util.helper.HelperUtilImpl;

public class CannyEdgeDetectionImpl {

    // Filter settings
    private int gaussianKernelSize_;
    private int gaussianKernelSigma_;
    private int hypHighThreshold_;
    private int hypLowThreshold_;
    private int sobelHighThreshold_;
    private int sobelLowThreshold_;

    // Image and image properties
    private int[] image_;
    private final int imageWidth_;

    // Image processing start and end index
    private int startIndex_;
    private int endIndex_;

    // Setters
    public void setGaussianKernelSize(int gaussianKernelSize) {
        this.gaussianKernelSize_ = gaussianKernelSize;
    }
    public void setGaussianKernelSigma(int gaussianKernelSigma) {
        this.gaussianKernelSigma_ = gaussianKernelSigma;
    }
    public void setHypHighThreshold(int hypHighThreshold){
        this.hypHighThreshold_ = hypHighThreshold;
    }
    public void setHypLowThreshold(int hypLowThreshold){
        this.hypLowThreshold_ = hypLowThreshold;
    }
    public void setSobelHighThreshold(int sobelHighThreshold){
        this.sobelHighThreshold_ = sobelHighThreshold;
    }
    public void setSobelLowThreshold(int sobelLowThreshold){
        this.sobelLowThreshold_ = sobelLowThreshold;
    }

    // Constructor
    // Set the default values for CannyEdge detector
    public CannyEdgeDetectionImpl( int[] image , int imageWidth ) {
        image_ = image;
        imageWidth_ = imageWidth;
        startIndex_ = 0;
        endIndex_ = image.length;
        filterSettingInit();
    }

    // Constructor
    // Set the default values for CannyEdge detector
    public CannyEdgeDetectionImpl( int[] image, int imageWidth, int startIndex, int endIndex ) {
        image_ = image;
        imageWidth_ = imageWidth;
        startIndex_ = startIndex;
        endIndex_ = endIndex;
        filterSettingInit();
    }

    // Initalize the filter settings
    private void filterSettingInit() {
        gaussianKernelSize_ = 5;
        gaussianKernelSigma_ = 3;
        hypHighThreshold_ = 80; //80//30
        hypLowThreshold_ = 30; //30//10
        sobelHighThreshold_ = 30; //30//25
        sobelLowThreshold_ = -30; //-30//25
    }

    // Applies canny edge detection to an image
    public void applyCannyEdgeDetection() {



        // Create all the necessary classes
        HelperUtilImpl helperUtil = new HelperUtilImpl();
        GaussianFilterImpl gaussianFilter = new GaussianFilterImpl();
        SobelOperatorImpl sobelOperator = new SobelOperatorImpl();
        NonMaxSuppressionImpl nonMaxSuppression = new NonMaxSuppressionImpl();
        HysteresisThresholdImpl hysteresisThreshold = new HysteresisThresholdImpl();

        // Apply the settings
        gaussianFilter.setKernelDeviation(gaussianKernelSigma_);
        gaussianFilter.setKernelSize(gaussianKernelSize_);
        sobelOperator.setHighValueThreshold(sobelHighThreshold_);
        sobelOperator.setLowValueThreshold(sobelLowThreshold_);
        hysteresisThreshold.setHighThreshold(hypHighThreshold_);
        hysteresisThreshold.setLowThreshold(hypLowThreshold_);

        // Apply the effects
        helperUtil.toGrayScale( image_, startIndex_, endIndex_ ); // convert to gray scale
        gaussianFilter.gaussianConvolution( image_, imageWidth_, startIndex_, endIndex_ ); // apply blue
        sobelOperator.applySobelOperator(image_, imageWidth_, startIndex_, endIndex_ ); // apply sobel
        char[] thetaArr = sobelOperator.getAtanArr(); //get the theta array from sobel
        nonMaxSuppression.applyNonMaxSuppression(image_, thetaArr, imageWidth_, startIndex_, endIndex_ ); //apply non-max suppression
        hysteresisThreshold.applyHysteresisThreshold(image_, imageWidth_, startIndex_, endIndex_); //apply double threshold and hypstersis

        //Convert the array to a black and white image
        helperUtil.convertBack(image_, startIndex_, endIndex_);

    }

    public int[] getImageArray()
    {
        return image_;
    }

    private void print()
    {
        String s = "";
        for(int i = startIndex_; i < endIndex_; i++)
        {
            s += image_[i] + " ";
            if(i % imageWidth_ == 0)
            {
                Log.d("print: ", s);
                s = "";
            }
        }
    }

}
