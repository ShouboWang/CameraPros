package com.imagepros.app.camera.util.Filters;

import android.util.Log;

public class SobelOperatorImpl {

    private final int redMask = 0xFF;
    private final int greenMask = 0xFF00;
    private final int blueMask = 0xFF0000;

    private final String TAG = "CameraPros_SobelOperatorImpl";

    public int[] applySobelOperator(int[] srcImage, int srcImageWidth) {

        int srcImageLength = srcImage.length;
        int[] afterFilterImage = new int[srcImageLength];

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

        retInt = (p1 + 2*p2 + p3) - (p7 + 2*p8 + p9) + (p3 + 2*p6 + p9) - (p1 - 2*p4 + p7);

        return Math.abs(retInt);
    }

}
