package com.imagepros.app.camera.util;

import android.graphics.Bitmap;
import com.imagepros.app.camera.util.Filters.GaussianFilterImpl;
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

        if(srcImg == null) {
            return srcImg;
        }
        // Basic setup of array and variable from source image
        int width = srcImg.getWidth();
        int height = srcImg.getHeight();
        int[] srcImgArray = new int[width * height];
        int[] afterEffectArray = new int[width * height];;

        // Apply gray filter
        helperUtil = new HelperUtilImpl();
        srcImg = helperUtil.toGrayscale(srcImg);

        // Convert bitmap to int array
        srcImg.getPixels(afterEffectArray, 0, width, 0, 0, width, height);

        // Apply Gaussian filter
        gaussianFilter = new GaussianFilterImpl();
        afterEffectArray = gaussianFilter.gaussianConvolution(afterEffectArray, width, gaussianKernelSize, gaussianKernelSigma);

        // Apply Sobel filter
        SobelOperatorImpl sobelOperator = new SobelOperatorImpl();
        afterEffectArray = sobelOperator.applySobelOperator(afterEffectArray, width);
        char[] thetaArr = sobelOperator.getThetaArray();

        // Apply Non-Maximum Suppression
        NonMaxSuppressionImpl nonMaxSuppression = new NonMaxSuppressionImpl();
        //afterEffectArray = nonMaxSuppression.applyNonMaxSuppression(afterEffectArray, thetaArr, width);

        // Convert int array to Bitmap
        Bitmap afterEffectBitmap = Bitmap.createBitmap(afterEffectArray, 0, width, width, height, srcImg.getConfig());
        return afterEffectBitmap;

    }

}
