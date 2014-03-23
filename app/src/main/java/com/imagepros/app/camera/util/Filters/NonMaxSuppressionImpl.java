package com.imagepros.app.camera.util.Filters;

import android.util.Log;

public class NonMaxSuppressionImpl {

    public int[] applyNonMaxSuppression(int[] srcImage, char[] thetaArr, int srcImageWidth) {

        int srcImageLength = srcImage.length;
        int[] afterEffectArray = new int[srcImageLength];

        for(int index = 0; index < srcImageLength; index++) {
            afterEffectArray[index] = checkIfMax(srcImage, thetaArr, index, srcImageWidth, srcImageLength);
        }

        return afterEffectArray;
    }

    private int checkIfMax(int[] srcImage, char[] thetaArr, int index, int srcImageWidth, int srcImageLength) {

        char theta = thetaArr[index];

        int srcImageValue1;
        int srcImageValue2;
        int srcOrigValue;

        if(theta == 0) {
            srcImageValue1 = index + 1;
            srcImageValue2 = index - 1;
        } else if(theta == 45) {
            srcImageValue1 = index + srcImageWidth - 1;
            srcImageValue2 = index - srcImageWidth + 1;
        } else if(theta == 90) {
            srcImageValue1 = index + srcImageWidth;
            srcImageValue2 = index - srcImageWidth;
        } else {
            Log.d("TTTEST", "Error " + theta);
            return 0;
        }

        if(srcImageValue1 >= srcImageLength || srcImageValue2 < 0) {
            return 0;
        }

        if(thetaArr[srcImageValue1] != theta || thetaArr[srcImageValue2] != theta) {
            return 0;
        }

        srcImageValue1 = srcImage[srcImageValue1];
        srcImageValue2 = srcImage[srcImageValue2];
        srcOrigValue = srcImage[index];
        if(srcOrigValue > srcImageValue1 && srcOrigValue > srcImageValue2) {
            return 16777215;
        }
        return 0;
    }
}
