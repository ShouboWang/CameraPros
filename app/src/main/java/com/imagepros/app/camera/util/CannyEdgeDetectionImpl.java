package com.imagepros.app.camera.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.imagepros.app.camera.util.Filters.GaussianFilterImpl;
import com.imagepros.app.camera.util.Filters.HypstersisThreshold;
import com.imagepros.app.camera.util.Filters.NonMaxSuppressionImpl;
import com.imagepros.app.camera.util.Filters.SobelOperatorImpl;
import com.imagepros.app.camera.util.helper.HelperUtilImpl;

/**
 * Created by shoubo.wang on 17/03/14.
 */
public class CannyEdgeDetectionImpl {

    private final String TAG = "CameraPros_CannyEdgeDetectionImpl";

    private GaussianFilterImpl gaussianFilter;
    private HelperUtilImpl helperUtil;

    public Bitmap applyCannyEdgeDetection(Bitmap srcImg, int gaussianKernelSize, int gaussianKernelSigma) {

        Log.d(TAG, "in1");
        if(srcImg == null) {
            return srcImg;
        }

        // Basic setup of array and variable from source image
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();
        int[] afterEffectArray = new int[width * height];;

        // Convert bitmap to int array
        srcImg.getPixels(afterEffectArray, 0, width, 0, 0, width, height);

        // Apply gray filter
        helperUtil = new HelperUtilImpl();
        afterEffectArray = helperUtil.toGrayScale(afterEffectArray);
        //afterEffectArray = helperUtil.getImgBlue();

        // Apply Gaussian filter
        gaussianFilter = new GaussianFilterImpl();
        afterEffectArray = gaussianFilter.gaussianConvolution(afterEffectArray, width, gaussianKernelSize, gaussianKernelSigma);

        // Apply Sobel filter
        SobelOperatorImpl sobelOperator = new SobelOperatorImpl();
        afterEffectArray = sobelOperator.applySobelOperator(afterEffectArray, width);
        char[] thetaArr = sobelOperator.getThetaArray();

        // Apply Non-Maximum Suppression
        NonMaxSuppressionImpl nonMaxSuppression = new NonMaxSuppressionImpl();
        afterEffectArray = nonMaxSuppression.applyNonMaxSuppression(afterEffectArray, thetaArr, width);

        HypstersisThreshold hypstersisThreshold = new HypstersisThreshold();
        afterEffectArray = hypstersisThreshold.applyHypstersisThreshold(afterEffectArray, width);

        afterEffectArray = convertBack(afterEffectArray);

        // Convert int array to Bitmap
        //Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, srcImg.getConfig());
        Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, Bitmap.Config.RGB_565);
        return afterEffectBitmap;

    }

    private void printLog(int[] img, int width) {
        String temp = "";
        String val;
        for(int i = 0; i < img.length; i++) {
            if(i % width == 0) {
                Log.d(TAG, temp);
                temp = "";
            }

            val = img[i] + "";
            if(val.length() == 2) {
                val = val + " ";
            } else if (val.length() == 1) {
                val = val + "  ";
            }
            temp += val + " ";
        }
    }

    private void printLog(char[] img, int width) {
        String temp = "";
        String val;
        for(int i = 0; i < img.length; i++) {
            if(i % width == 0) {
                Log.d(TAG, temp);
                temp = "";
            }

            val = (int)(img[i]) + "";
            if(val.length() == 2) {
                val = val + " ";
            } else if (val.length() == 1) {
                val = val + "  ";
            }
            temp += val + " ";
        }
    }

    private int[] convertBack(int[] src) {
        int[] test = new int[src.length];
        for(int i = 0; i < src.length; i++) {
            if (src[i]  > 0) {
                test[i] = 16777215;
            }
        }
        return test;
    }



}
