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

        if(theta == (char)0) {
            srcImageValue1 = index + 1;
            srcImageValue2 = index - 1;
        } else if(theta == (char)45) {
            srcImageValue1 = index + srcImageWidth - 1;
            srcImageValue2 = index - srcImageWidth + 1;
        } else if(theta == (char)90) {
            srcImageValue1 = index + srcImageWidth;
            srcImageValue2 = index - srcImageWidth;
        } else if(theta == (char)135) {
            srcImageValue1 = index + srcImageWidth + 1;
            srcImageValue2 = index - srcImageWidth - 1;
        } else {
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


        if(index == 564 || index == 565 || index == 566) {
            Log.d("Nonmax index: ", index + "");
            Log.d("Nonmax srcOrigValue ", srcOrigValue + "");
            Log.d("Nonmax srcImageValue1 ", srcImageValue1 + "");
            Log.d("Nonmax srcImageValue2 ", srcImageValue2 + "");

        }

        if(srcOrigValue >= srcImageValue1 && srcOrigValue >= srcImageValue2) {
            return srcOrigValue;
        }

        return 0;
    }
}
