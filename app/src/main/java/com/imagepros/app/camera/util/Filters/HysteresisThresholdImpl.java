/**
 * <class>NonMaxSuppressionImpl</class>
 *
 * <summary>
 * This Class is used by CannyEdgeDetectionImpl to apply Hysteresis threshold
 * on an image of int array
 * </summary>
 *
 * <author>Jack Wang</author>
 * <date>March 5 2014</date>
 * */

 package com.imagepros.app.camera.util.Filters;

public class HysteresisThresholdImpl {

    // final values
    final int MAX_VALUE = 2;
    final int MIN_VALUE = 0;
    final int CHECK_VALUE = 1;

    // fields
    private int highThreshold;
    private int lowThreshold;

    //Setters for high/low thresholds;
    public void setHighThreshold(int highThreshold) {
        this.highThreshold = highThreshold;
    }
    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    // This Class is used by CannyEdgeDetectionImpl to apply Hysteresis threshold
    // on an image of int array
    // returns the image after hysteresis threshold
    public void applyHysteresisThreshold( int[] srcImage, int width, int startIndex, int endIndex ) {
        int processLength = endIndex - startIndex;
        int process = processLength / width;
        int index;

        // apply double threshold
        applyDoubleThreshold(srcImage, width, startIndex, endIndex);

        // apply hysteresis threshold
        for(int row = 1; row < process - 1; row++) {
            for(int col = 1; col < width - 1; col++) {
                index = row * width + col;
                if (srcImage[index] == CHECK_VALUE) {
                    if (nextToMax(srcImage, index, width)) {
                        fillEdge(srcImage, index, width, startIndex, endIndex);
                    }
                }
            }
        }

        //change any value that is not MAX_VALUE to MIN_VALUE
        for (int i = startIndex; i < endIndex; i++) {
            if (srcImage[i] != MAX_VALUE) {
                srcImage[i] = MIN_VALUE;
            }
        }
    }

    // apply the double threshold to an image array
    // returns the image after threshold
    private void applyDoubleThreshold(int[] srcImage, int width, int startIndex, int endIndex) {

        // avoid boundary conditions
        if(startIndex == 0)
        {
            startIndex = width;
        }
        if(endIndex == srcImage.length)
        {
            endIndex = srcImage.length - width;
        }

        // Apply threshold
        for(int index = startIndex; index < endIndex; index++) {
            if(srcImage[index] > highThreshold) {
                srcImage[index] = MAX_VALUE;
            } else if(srcImage[index] < lowThreshold) {
                srcImage[index] = MIN_VALUE;
            } else {
                srcImage[index] = CHECK_VALUE;
            }
        }
    }

    // Checks if a given pixel has surrounding pixel with MAX VALUE
    // return true if there is a surrounding pixel with MAX_VALUE
    // false otherwise
    private boolean nextToMax(int[] srcImage, int index, int width) {

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

    // Given an index of pixel with CHECK_VALUE, populates all surrounding pixel with
    // CHECK_VALUE to MAX_VALUE
    private void fillEdge(int[] srcImage, int index, int width, int startIndex, int endIndex) {

        // Make sure that the index is not outside of the boundary or go out of the screen
        // Check if the value is a checking value
        if(index >= endIndex || index < startIndex || index%width == 0) {
            return;
        } else if (srcImage[index] != CHECK_VALUE) {
            return;
        }

        // set the CHECK value value as MAX value
        srcImage[index] = MAX_VALUE;

        // check if can go left and right
        if (index % width != 0) { //left
            fillEdge(srcImage, index + width - 1, width, startIndex, endIndex); //bottom left
            fillEdge(srcImage, index - 1, width, startIndex, endIndex); //left
            fillEdge(srcImage, index - width - 1, width, startIndex, endIndex); //top left
        } else if (((index + 1) % width )!= 0) { //right
            fillEdge(srcImage, index - width + 1, width, startIndex, endIndex); //top right
            fillEdge(srcImage, index + 1, width, startIndex, endIndex); //right
            fillEdge(srcImage, index + width + 1, width, startIndex, endIndex); //bottom right
        }
        fillEdge(srcImage, index - width, width, startIndex, endIndex); //top
        fillEdge(srcImage, index + width, width, startIndex, endIndex); //bottom

    }

}
