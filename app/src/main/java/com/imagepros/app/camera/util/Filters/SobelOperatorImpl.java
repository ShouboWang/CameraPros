package com.imagepros.app.camera.util.Filters;


import android.util.Log;

public class SobelOperatorImpl {

    private final int redMask = 0xFF;
    private final int greenMask = 0xFF00;
    private final int blueMask = 0xFF0000;

    private char[] thetaArr;

    private final String TAG = "CameraPros_SobelOperatorImpl";

    public char[] getThetaArray() {
        return thetaArr;
    }

    public int[] applySobelOperator(int[] srcImage, int srcImageWidth) {

        int srcImageLength = srcImage.length;

        int[] afterFilterImage = new int[srcImageLength];
        thetaArr = new char[srcImageLength];

        for(int index = 1; index < srcImageLength - 1; index++) {
            afterFilterImage[index] = sobelOperator(srcImage, index, srcImageWidth, srcImageLength);
        }

        return afterFilterImage;

    }

    private int sobelOperator(int[] srcImage, int x, int srcImageWidth, int srcImageLength) {
        int retInt;

        int topRow = x - srcImageWidth;
        int botRow = x + srcImageWidth;

        if(topRow - 1 < 0 || botRow + 1 >= srcImageLength) {
            return srcImage[x];
        }

        int p1 = srcImage[ topRow - 1 ];
        int p2 = srcImage[ topRow ];
        int p3 = srcImage[ topRow + 1 ];
        int p4 = srcImage[ x - 1 ];
        int p6 = srcImage[ x + 1 ];
        int p7 = srcImage[ botRow - 1 ];
        int p8 = srcImage[ botRow ];
        int p9 = srcImage[ botRow + 1 ];

        int Gx = Math.abs(p1-p7 + 2*p2-2*p8 + p3-p9);
        int Gy = Math.abs(p3-p1 + 2*p6-2*p4 + p9-p7);


        calcArcTan(Gx, Gy, x);


        retInt = Gx + Gy;

        int retIntRed = (retInt >> 16) & 0xFF;
        int retIntGreen = (retInt >> 8) & 0xFF;
        int retIntBlue = (retInt) & 0xFF;

        if(retIntRed + retIntBlue + retIntGreen < 400) { //382
            return 0;
        }

        return 16777215;

    }

    private void calcArcTan(int Gx, int Gy, int index) {

        float theta;

        if(Gx == 0) {
            thetaArr[index] = 0;
        } else {
            theta = (float) Math.atan(Gy/(float)Gx);
            if(theta > 1.047197551) {
                thetaArr[index] = 0;
            } else if (theta > 0.52359877559) {
                thetaArr[index] = 45;
            } else {
                thetaArr[index] = 90;
            }
        }
    }




}
