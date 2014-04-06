/**
 * <class>GaussianFilterImpl</class>
 *
 * <summary>
 * This Class is used by CannyEdgeDetectionImpl Gaussian blur
 * </summary>
 *
 * <author>Jack Wang</author>
 * <date>March 5 2014</date>
 * */

package com.imagepros.app.camera.util.filters;

public class GaussianFilterImpl {

    // fields
    private int kernelSize;
    private int kernelDeviation;

    // getters
    public void setKernelSize(int kernelSize) {
        this.kernelSize = kernelSize;
    }
    public void setKernelDeviation(int kernelDeviation){
        this.kernelDeviation = kernelDeviation;
    }

    // This Class is used by CannyEdgeDetectionImpl Gaussian blur
    // returns the image after blue
    public int[] gaussianConvolution(int[] srcImgArr, int srcImgWidth) {
        int srcImgLength = srcImgArr.length;

        // get the gaussian kernel
        float[] kernel = get1DGaussianKernel( kernelSize, kernelDeviation );

        int[] resultArr1 = new int[srcImgLength];
        int[] resultArr2 = new int[srcImgLength];

        // Process X direction
        for( int index = kernel.length/2; index < srcImgLength - kernel.length/2; index++ ) {
            resultArr1[index] = processPointX(srcImgArr, index, kernel);
        }

        // Process Y direction
        for( int index = kernel.length/2; index < srcImgLength - kernel.length/2; index++ ) {
            resultArr2[index] = processPointY(resultArr1, index, srcImgWidth, kernel);
        }

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

        for( float kernelValue : kernel) {
            sum += kernelValue;
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

        float kernelValue;
        int colorValue;
        double processedColor = 0;

        for( int i = (-1) * half; i <= half; i++ ) {
            kernelValue = kernel[i + half];
            colorValue = srcImg[i + x];

            processedColor += colorValue * kernelValue;
        }
        return (int)(processedColor + 0.5);
    }

    // Process the array in the Y direction
    // Gets the color value via byte shifting
    // Returns the processed int value
    private int processPointY(int[] srcImg, int x, int width ,float[] kernel) {
        final int half = kernel.length / 2;
        final int srcImgLength = srcImg.length;

        float kernelValue;
        int colorValue;
        int colorValueIndex;
        double processedColor = 0;

        for( int i = (-1) * half; i <= half; i++ ) {
            colorValueIndex = x + i * width;
            kernelValue = kernel[i + half];

            // Check for out of bound
            if(colorValueIndex < 0 || colorValueIndex >= srcImgLength) {
                processedColor += srcImg[x] * kernelValue;
                continue;
            }

            colorValue = srcImg[colorValueIndex];
            processedColor += colorValue * kernelValue;
        }
        return (int)(processedColor + 0.5);
    }

}