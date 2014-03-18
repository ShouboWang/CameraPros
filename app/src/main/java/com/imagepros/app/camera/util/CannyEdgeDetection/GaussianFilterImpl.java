package com.imagepros.app.camera.util.CannyEdgeDetection;


public class GaussianFilterImpl {

    public double[] get1DGaussianKernel( int deviation ) {
        int size = deviation * 6 + 1;
        return normalizeGaussianKernel(get1DGaussianKernel( size, deviation ));
    }

    //Creates the 1d Gaussian Mask
    public double[] get1DGaussianKernel( int size, int deviation ) {

        double[] mask = new double[size];

        int deviationSqr2 = 2 * deviation * deviation;
        int half = size / 2;
        double frontValue = 1.0 / ((Math.sqrt( 2 * Math.PI )) * deviation );

        for( int index = half * -1; index <= half; index++) {
            mask[index + half] = frontValue * Math.exp(((-1.0) * (index) * (index))/deviationSqr2);
        }

        return mask;
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

    public double[][] gaussianConvolution(double[][] matrix, int deviation) {
        double[] kernel = get1DGaussianKernel( deviation );

        double[][] ret1 = new double[matrix[0].length][matrix[1].length];
        double[][] ret2 = new double[matrix[0].length][matrix[1].length];

        //x direction
        for (int row = 0; row < matrix[0].length; row++ ) {
            for (int col = 0; col < matrix[1].length; col++ ) {
                ret1[row][col] = processPoint(matrix, row, col, kernel, 0);
            }
        }

        //y direction
        for (int row = 0; row < matrix[0].length; row++ ) {
            for (int col = 0; col < matrix[1].length; col++ ) {
                ret2[row][col] = processPoint(ret1, row, col, kernel, 1);
            }
        }

        return null;
    }

    private double processPoint(double[][] matrix, int row, int col, double[] kernel, int direction) {
        double res = 0;

        int half = kernel.length / 2;

        for( int i = (-1) * half; i <= half; i++ ) {
            int cox = row;
            int coy = col;
            if ( 0 == direction ) {
                cox += i;
            } else {
                coy += i;
            }

            if ( cox >= 0 && coy >= 0 && cox < matrix.length && coy <=matrix[0].length ) {
                res += matrix[cox][coy] * kernel[i + half];
            }
        }
        return res;
    }
}
