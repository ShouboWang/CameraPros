package com.imagepros.app.camera.util.Filters;

import android.util.Log;

/**
 * Created by jack on 2014-04-05.
 */
public class HypstersisThreshold {

    int highThreshold = 150; //500
    int lowThreshold = 50; //100

    final int MAX_VALUE = 2;
    final int MIN_VALUE = 0;
    final int CHECK_VALUE = 1;


    public int[] applyHypstersisThreshold(int[] srcImage, int width) {
        applyThreshold(srcImage, width);
        int srcLength = srcImage.length;
        int height = srcLength / width;
        int index;



        for(int row = 1; row < height - 1; row++) {
            for(int col = 1; col < width - 1; col++) {
                index = row * width + col;
                if (srcImage[index] == CHECK_VALUE) {
                    if (nextToMax(srcImage, index, width)) {
                        fillEdge(srcImage, index, width, srcLength);
                    }
                }
            }
        }

        for (int i = 0; i < srcLength; i++) {
            if (srcImage[i] != MAX_VALUE) {
                srcImage[i] = MIN_VALUE;
            }
        }
        return srcImage;
    }

    private void applyThreshold(int[] srcImage, int width) {
        int srcLength = srcImage.length;

        for(int index = width; index < srcLength - width; index++) {
            if(srcImage[index] > highThreshold) {
                srcImage[index] = MAX_VALUE;
            } else if(srcImage[index] < lowThreshold) {
                srcImage[index] = MIN_VALUE;
            } else {
                srcImage[index] = CHECK_VALUE;
            }
        }
    }

    private boolean nextToMax(int[] srcImage, int index, int width) {

        //top
        if(srcImage[index - width] == MAX_VALUE) {
            return true;
        } else if (srcImage[index - width + 1] == MAX_VALUE) {
            return true;
        } else if (srcImage[index + 1] == MAX_VALUE) {
            return true;
        } else if (srcImage[index + width + 1] == MAX_VALUE) {
            return true;
        } else if (srcImage[index + width] == MAX_VALUE) {
            return true;
        } else if (srcImage[index + width - 1] == MAX_VALUE) {
            return true;
        } else if (srcImage[index - 1] == MAX_VALUE) {
            return true;
        } else if (srcImage[index - width - 1] == MAX_VALUE) {
            return true;
        }
        return false;
    }

    private void fillEdge(int[] srcImage, int index, int width, int length) {

        // Make sure that the index is not outside of the boundary or go out of the screen
        // Check if the value is a checking value
        if(index >= length || index < 0 || index%width == 0) {
            return;
        } else if (srcImage[index] != CHECK_VALUE) {
            return;
        }

        // set the CHECK value value as MAX value
        srcImage[index] = MAX_VALUE;

        // check if can go left and right
        if (index % width != 0) { //left
            fillEdge(srcImage, index + width - 1, width, length); //bottom left
            fillEdge(srcImage, index - 1, width, length); //left
            fillEdge(srcImage, index - width - 1, width, length); //top left
        } else if (((index + 1) % width )!= 0) { //right
            fillEdge(srcImage, index - width + 1, width, length); //top right
            fillEdge(srcImage, index + 1, width, length); //right
            fillEdge(srcImage, index + width + 1, width, length); //bottom right
        }
        fillEdge(srcImage, index - width, width, length); //top
        fillEdge(srcImage, index + width, width, length); //bottom

    }

}
