package com.imagepros.app.camera.util.Filters;


import android.util.Log;

public class SobelOperatorImpl {

    private final int redMask = 0xFF;
    private final int greenMask = 0xFF00;
    private final int blueMask = 0xFF0000;
    int timer = 0;

    private char[] thetaArr;

    private final String TAG = "CameraPros_SobelOperatorImpl";

    public char[] getThetaArray() {
        return thetaArr;
    }

    public int[] applySobelOperator(int[] srcImage, int srcImageWidth) {

        Log.d(TAG, srcImageWidth + "");

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
        int p1r = (p1 >> 16) & 0xFF;
        int p1g = (p1 >> 8) & 0xFF;
        int p1b = (p1) & 0xFF;

        int p2 = srcImage[ topRow ];
        int p2r = (p2 >> 16) & 0xFF;
        int p2g = (p2 >> 8) & 0xFF;
        int p2b = (p2) & 0xFF;

        int p3 = srcImage[ topRow + 1 ];
        int p3r = (p3 >> 16) & 0xFF;
        int p3g = (p3 >> 8) & 0xFF;
        int p3b = (p3) & 0xFF;

        int p4 = srcImage[ x - 1 ];
        int p4r = (p4 >> 16) & 0xFF;
        int p4g = (p4 >> 8) & 0xFF;
        int p4b = (p4) & 0xFF;

        int p6 = srcImage[ x + 1 ];
        int p6r = (p6 >> 16) & 0xFF;
        int p6g = (p6 >> 8) & 0xFF;
        int p6b = (p6) & 0xFF;

        int p7 = srcImage[ botRow - 1 ];
        int p7r = (p7 >> 16) & 0xFF;
        int p7g = (p7 >> 8) & 0xFF;
        int p7b = (p7) & 0xFF;

        int p8 = srcImage[ botRow ];
        int p8r = (p8 >> 16) & 0xFF;
        int p8g = (p8 >> 8) & 0xFF;
        int p8b = (p8) & 0xFF;

        int p9 = srcImage[ botRow + 1 ];
        int p9r = (p9 >> 16) & 0xFF;
        int p9g = (p9 >> 8) & 0xFF;
        int p9b = (p9) & 0xFF;

        int Gxr = Math.abs(p1r-p7r) + Math.abs(2*p2r-2*p8r) + Math.abs(p3r-p9r);
        int Gxg = Math.abs(p1g-p7g) + Math.abs(2*p2g-2*p8g) + Math.abs(p3g-p9g);
        int Gxb = Math.abs(p1b-p7b) + Math.abs(2*p2b-2*p8b) + Math.abs(p3b-p9b);

        int Gyr = Math.abs(p3r-p1r) + Math.abs(2*p6r-2*p4r) + Math.abs(p9r-p7r);
        int Gyg = Math.abs(p3g-p1g) + Math.abs(2*p6g-2*p4g) + Math.abs(p9g-p7g);
        int Gyb = Math.abs(p3b-p1b) + Math.abs(2*p6b-2*p4b) + Math.abs(p9b-p7b);

        int Gx = (Gxr << 16) + (Gxg << 8) + Gxb;
        int Gy = (Gyr << 16) + (Gyg << 8) + Gyb;

        calcArcTan(Gx, Gy, x);

        retInt = Gx + Gy;

        if(retInt > 16777215) {
            return 16777215;
        }

        return retInt;
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

    private void printRGB(int color, String value) {
        Log.d(TAG, "For Color: " + color + " name: " + value);
        Log.d(TAG,"Red" + ((color >> 16) & 0xFF));
        Log.d(TAG,"Green" + ((color >> 8) & 0xFF));
        Log.d(TAG,"Blue" + ((color) & 0xFF));
    }


}
