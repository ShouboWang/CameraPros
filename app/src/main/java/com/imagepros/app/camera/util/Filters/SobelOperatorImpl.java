/**
 * <class>SobelOperatorImpl</class>
 *
 * <summary>
 * This Class applies the sobel mask in the X and Y direction to a int[] image
 * This Class will also calculate the Theta value for each pixel
 * </summary>
 *
 * <author>Jack Wang</author>
 * <date>March 5 2014</date>
 * */

package com.imagepros.app.camera.util.filters;

public class SobelOperatorImpl {

    //arc tan array
    private char[] atanArr;
    private int highValueThreshold;
    private int lowValueThreshold;


    // Getter for theta array
    public char[] getAtanArr() {
        return atanArr;
    }

    // Setters
    public void setHighValueThreshold(int highValueThreshold){
        this.highValueThreshold = highValueThreshold;
    }
    public void setLowValueThreshold(int lowValueThreshold){
        this.lowValueThreshold = lowValueThreshold;
    }

    //This Class applies the sobel mask in the X and Y direction to a int[] image
    //This Class will also calculate the Theta value for each pixel
    public int[] applySobelOperator(int[] srcImage, int srcImageWidth) {
        int srcImageLength = srcImage.length;

        int[] afterFilterImage = new int[srcImageLength];
        atanArr = new char[srcImageLength];

        //Apply the operator to every pixel
        for(int index = 1; index < srcImageLength - 1; index++) {
            afterFilterImage[index] = sobelOperator(srcImage, index, srcImageWidth, srcImageLength);
        }

        return afterFilterImage;
    }

    // Takes the index of the pixel and calculate X and Y gradient by applying
    // Sobel operator in the X and Y direction
    // Also populates the theta array by calculating the theta based on the gradient in the X and Y direction
    // Returns the combined X and Y value of the gradient
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

        //Gets the arc tan of the values
        atanArr[x] = calcArcTan(Gx, Gy);

        //Get the total absolute value
        retInt = Math.abs(Gx) + Math.abs(Gy);

        return retInt;
    }

    // Takes the X and Y gradient value and calculates the arc tan value
    // returns the arc tan value of the
    private char calcArcTan(double Gx, int Gy) {
        if(valueBelowRange(Gx) && valueBelowRange(Gy)) {
            return (char)-1;
        }

        Gx = Gx + 0.001;
        double theta;
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
        return (char)theta;
    }


    // This method takes a value and determine if the value is a valid value for theta array
    // returns true if value is within the invalid range, false otherwise
    private boolean valueBelowRange(double value) {
        if(value > lowValueThreshold && value < highValueThreshold) {
            return true;
        }
        return false;
    }

}
