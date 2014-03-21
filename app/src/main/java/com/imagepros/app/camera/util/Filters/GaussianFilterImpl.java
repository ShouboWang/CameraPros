package com.imagepros.app.camera.util.Filters;

import android.util.Log;

public class GaussianFilterImpl {

    private final int redMask = 0xFF;
    private final int greenMask = 0xFF00;
    private final int blueMask = 0xFF0000;
    private final String TAG = "CameraPros_GaussianFilterImpl";


    public int[] gaussianConvolution(int[] srcImgArr, int srcImgWidth, int size ,int deviation) {

        Log.d(TAG, "gaussianConvolution has started");

        int srcImgLength = srcImgArr.length;

        float[] kernel = get1DGaussianKernel( size, deviation );

        int[] resultArr1 = new int[srcImgLength];
        int[] resultArr2 = new int[srcImgLength];


        // Process X direction
        for( int index = kernel.length/2; index < srcImgLength - kernel.length/2; index++ ) {
            resultArr1[index] = processPointX(srcImgArr, index, kernel);
        }

        // Get Y direction
        for( int index = kernel.length/2; index < srcImgLength - kernel.length/2; index++ ) {
            resultArr2[index] = processPointY(resultArr1, index, srcImgWidth, kernel);
        }

        Log.d(TAG, "gaussianConvolution has finished");

        return resultArr2;
    }

    //Creates the 1d Gaussian Mask
    private float[] get1DGaussianKernel( int size, int deviation ) {

        float[] mask = new float[size];

        int deviationSqr2 = 2 * deviation * deviation;
        int half = size / 2;
        float frontValue = (float)(1.0f / ((Math.sqrt( 2 * Math.PI )) * deviation ));

        for( int index = half * -1; index <= half; index++) {
            mask[index + half] = (float)(frontValue * Math.exp(((-1.0) * (index) * (index))/deviationSqr2));
        }

        mask = normalizeGaussianKernel(mask);
        return mask;
    }

    // Normalize the filter by dividing all valued by the sum
    private float[] normalizeGaussianKernel( float[] kernel ) {

        float sum = 0.0f;

        for( int index = 0; index < kernel.length; index++ ) {
            sum += kernel[index];
        }

        if(sum != 0.0) {
            for( int index = 0; index < kernel.length; index++ ) {
                kernel[index] /= sum;
            }
        }
        return kernel;
    }

    // Process the array in the X direction
    // Gets the color value via byte shifting
    // Returns the processed int value
    private int processPointX(int[] srcImg, int x, float[] kernel) {

        final int half = kernel.length / 2;

        int colorRed = 0;
        int colorBlue = 0;
        int colorGreen = 0;

        float kernelValue;
        int colorValue;

        for( int i = (-1) * half; i <= half; i++ ) {

            kernelValue = kernel[i + half];
            colorValue = srcImg[i + x];

            colorRed += (colorValue & redMask) * kernelValue;
            colorGreen += ((colorValue & greenMask) >> 8) * kernelValue;
            colorBlue += ((colorValue & blueMask) >> 16) * kernelValue;
        }

        return colorRed + (colorGreen << 8) + (colorBlue << 16);
    }

    // Process the array in the Y direction
    // Gets the color value via byte shifting
    // Returns the processed int value
    private int processPointY(int[] srcImg, int x, int width ,float[] kernel) {

        final int half = kernel.length / 2;
        final int srcImgLength = srcImg.length;

        int colorRed = 0;
        int colorBlue = 0;
        int colorGreen = 0;

        float kernelValue;
        int colorValue;
        int colorValueIndex = 0;

        for( int i = (-1) * half; i <= half; i++ ) {

            colorValueIndex = x + Math.abs(i) * width;

            // Check for out of bound
            if(colorValueIndex <= 0 || colorValueIndex >= srcImgLength) {
                continue;
            }

            kernelValue = kernel[i + half];
            colorValue = srcImg[colorValueIndex];

            colorRed += (colorValue & redMask) * kernelValue;
            colorGreen += ((colorValue & greenMask) >> 8) * kernelValue;
            colorBlue += ((colorValue & blueMask) >> 16) * kernelValue;

        }

        return colorRed + (colorGreen << 8) + (colorBlue << 16);
    }

}
