package com.imagepros.app.camera.util.CannyEdgeDetection;

import android.graphics.Bitmap;
import android.util.Log;
import com.imagepros.app.camera.util.helper.ConvolutionMatrix;

public class GaussianFilterImpl {


    private final String TAG = "CameraPros_GaussianFilterImpl";

    //Creates the 1d Gaussian Mask
    public double[][] get1DGaussianKernel( int size, int deviation ) {

        double[] mask = new double[size];

        int deviationSqr2 = 2 * deviation * deviation;
        int half = size / 2;
        double frontValue = 1.0 / ((Math.sqrt( 2 * Math.PI )) * deviation );

        for( int index = half * -1; index <= half; index++) {
            mask[index + half] = frontValue * Math.exp(((-1.0) * (index) * (index))/deviationSqr2);
        }

        mask = normalizeGaussianKernel(mask);

        return get2DGaussianKernel(size, mask);
    }

    public double [][] get2DGaussianKernel( int size, double[] mask) {
        double[][] kernel = new double[size][size];

        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++){
                kernel[row][col] = mask[row] * mask[col];
            }
        }
        return kernel;
    }


    public double[] normalizeGaussianKernel( double[] kernel ) {

        double sum = 0.0;

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

    public Bitmap gaussianConvolution(Bitmap srcImg, int size ,int deviation) {
        double[][] kernel = get1DGaussianKernel( size, deviation );

        int half = size / 2;


        int width = srcImg.getWidth();
        int height = srcImg.getHeight();

        Bitmap tempresult = Bitmap.createBitmap(width, height, srcImg.getConfig());

        for( int x = half; x < width - half; x++ ) {
            for( int y = half; y < height - half; y++ ) {

                double pixel = 0;
                for (int kx = (-1) * half; kx <= half; kx++) {
                    for (int ky = (-1) * half; ky <= half; ky++) {
                        pixel += srcImg.getPixel(x, y) * kernel[kx + half][ky + half];
                    }
                }
                tempresult.setPixel(x, y, (int)pixel);

            }
        }

        /*
        for( int x = 0; x < width; x++ ) {
            for( int y = 0; y < height; y++ ) {
                tempresult.setPixel(x, y, processPoint(srcImg, x, y, kernel, 0));
                //tempresult.setPixel(x, y, srcImg.getPixel(x, y));
            }
        }

        for( int x = 0; x < width; x++ ) {
            for( int y = 0; y < height; y++ ) {
                result.setPixel(x, y, processPoint(tempresult, x, y, kernel, 1));
                //tempresult.setPixel(x, y, srcImg.getPixel(x, y));
            }
        }*/

/*
        for( int x = 0; x < width; x++ ) {
            for( int y = 0; y < height; y++ ) {

                tempresult.setPixel(x, y, srcImg.getPixel(x, y));
            }
        }

  */      /*
        Bitmap ret1 = Bitmap.createBitmap(srcImg.getWidth(), srcImg.getHeight(), Bitmap.Config.RGB_565);
        Bitmap ret2 = Bitmap.createBitmap(srcImg.getWidth(), srcImg.getHeight(), Bitmap.Config.RGB_565);

        //x direction
        for (int row = 0; row < srcImg.getHeight(); row++ ) {
            for (int col = 0; col < srcImg.getWidth(); col++ ) {
                ret1.setPixel(col,row,processPoint(srcImg, col, row, kernel, 0));
            }
        }

        //y direction
        for (int row = 0; row < srcImg.getHeight(); row++ ) {
            for (int col = 0; col < srcImg.getWidth(); col++ ) {
                ret2.setPixel(col,row,processPoint(ret1, col, row, kernel, 1));
            }
        }
        */
        return tempresult;
    }

    private int processPoint(Bitmap srcImg, int x, int y, double[] kernel, int direction) {
        double res = 0;

        int half = kernel.length / 2;

        for( int i = (-1) * half; i <= half; i++ ) {
            int cox = x;
            int coy = y;
            if ( 0 == direction ) {
                cox += i;
            } else {
                coy += i;
            }

            if ( cox >= 0 && coy >= 0 && cox < srcImg.getWidth() && coy <=srcImg.getHeight() ) {
                res += srcImg.getPixel(x, y) * kernel[i + half];
            } else {
               return srcImg.getPixel(x, y);
            }
        }
        return (int)res;
    }

    public Bitmap applyGaussianBlur(Bitmap src) {
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

}
