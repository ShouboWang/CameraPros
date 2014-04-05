package com.imagepros.app.camera.util.Filters;

import android.util.Log;

import java.math.*;

public class SobelOperatorImpl {

    //Fields
    private char[] thetaArr;

    /**
     * Gets the theta array
     * */
    public char[] getThetaArray() {
        return thetaArr;
    }

    /**
     *
     * */
    public int[] applySobelOperator(int[] srcImage, int srcImageWidth) {
        int srcImageLength = srcImage.length;

        int[] afterFilterImage = new int[srcImageLength];
        thetaArr = new char[srcImageLength];

        //Apply the operator to every pixel
        for(int index = 1; index < srcImageLength - 1; index++) {
            afterFilterImage[index] = sobelOperator(srcImage, index, srcImageWidth, srcImageLength);
        }

        return afterFilterImage;
    }

    private int sobelOperator(int[] srcImage, int x, int srcImageWidth, int srcImageLength) {
        int retInt;

        int topRow = x - srcImageWidth;
        int botRow = x + srcImageWidth;

        //If outside of the array boundary
        if(topRow - 1 < 0 || botRow + 1 >= srcImageLength) {
            return srcImage[x];
        }

        //Gets the necessary pixel values
        int p1 = srcImage[ topRow - 1 ];
        int p2 = srcImage[ topRow ];
        int p3 = srcImage[ topRow + 1 ];
        int p4 = srcImage[ x - 1 ];
        int p6 = srcImage[ x + 1 ];
        int p7 = srcImage[ botRow - 1 ];
        int p8 = srcImage[ botRow ];
        int p9 = srcImage[ botRow + 1 ];

        // Calculate Gx, Gy
        int Gy = p1 - p7 + 2*p2 - 2*p8 + p3 - p9;
        int Gx = p3 - p1 + 2*p6 - 2*p4 + p9 - p7;

        //Gets the arctan of the values
        calcArcTan(Gx, Gy, x);

        if(x == 658) {
            Log.d("Sobel Gx:", ""+Gx);
            Log.d("Sobel Gy:", ""+Gy);

        }

        //Get the total absolute value
        retInt = Math.abs(Gx) + Math.abs(Gy);

        return retInt;
    }

    /**
     * This method will calculate the tangent to Gx, Gy value
     * **/
    private void calcArcTan(double Gx, int Gy, int index) {

        //May need to remove
        if(valueBelowRange(Gx) && valueBelowRange(Gy)) {
            thetaArr[index] = (char)-1;
            return;
        }

        double theta;
        Gx = Gx + 0.001;

        theta = Math.atan(Gy / Gx);
        if(theta > -1.496619 && theta < -0.668188) {
            theta = 135;
        } else if(theta > 0.668188 && theta < 1.496619) {
            theta = 45;
        } else if(theta > -0.668188 && theta < 0.668188) {
            theta = 0;
        } else {
            theta = 90;
        }
        thetaArr[index] = (char)theta;
    }

    private boolean valueBelowRange(double value) {
        int highValueThreshold = 30;
        int lowValueThreshold = -30;
        if(value > lowValueThreshold && value < highValueThreshold) {
            return true;
        }
        return false;
    }

}
