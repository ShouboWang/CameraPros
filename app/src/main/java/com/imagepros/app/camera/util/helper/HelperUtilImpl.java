/**
 * <class>HelperUtilImpl</class>
 *
 * <summary>
 * This Class is used by CannyEdgeDetectionImpl for any common operations
 * or helping operations
 * </summary>
 *
 * <author>Jack Wang</author>
 * <date>March 5 2014</date>
 * */

package com.imagepros.app.camera.util.helper;

import android.util.Log;

public class HelperUtilImpl {

    // fields
    private int[] imgRed;
    private int[] imgGreen;
    private int[] imgBlue;

    // getters
    public int[] getImgRed()
    {
        return this.imgRed;
    }
    public int[] getImgGreen()
    {
        return this.imgGreen;
    }
    public int[] getImgBlue()
    {
        return this.imgBlue;
    }

    // Converts an image to gray scale
    // returns an int array of value 0-255 representing an
    // average of RGB color distribution
    public void toGrayScale(int[] srcImage, int startIndex, int endIndex) {
        getRGBFromImage(srcImage);

        for(int index = startIndex; index < endIndex; index++) {
            //srcImage[index] = (int)(imgRed[index]*0.2989 + imgBlue[index]*0.5870 + imgGreen[index]*0.1140 + 0.5);
            srcImage[index] = (imgRed[index]);
        }
    }

    // Extracts RGB color from an image
    // Populates corresponding arrays
    public void getRGBFromImage(int[] srcImage) {
        int srcImageLength = srcImage.length;
        int srcImagePixel;

        imgRed = new int[srcImageLength];
        imgGreen = new int[srcImageLength];
        imgBlue = new int[srcImageLength];

        for(int index = 0; index < srcImageLength; index++) {
            srcImagePixel = srcImage[index];
            imgRed[index] = ( srcImagePixel >> 16 ) & 0xFF;
            imgGreen[index] = ( srcImagePixel >> 8 ) & 0xFF;
            imgBlue[index] = ( srcImagePixel ) & 0xFF;
        }

    }

    // Takes an image, and magnify all values above 0(black) to 16777215(white)
    public void convertBack(int[] src, int start, int end) {
        for(int i = start; i < end; i++) {
            if (src[i]  > 0) {
                src[i] = 0;
            }
            else
            {
                src[i] = 16777215;
            }
        }
    }
}
