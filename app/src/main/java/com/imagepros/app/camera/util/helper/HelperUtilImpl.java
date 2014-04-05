package com.imagepros.app.camera.util.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by shoubo.wang on 17/03/14.
 */
public class HelperUtilImpl {

    private int[] imgRed;
    private int[] imgGreen;
    private int[] imgBlue;


    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public int[] toGrayScale(int[] srcImage) {

        int srcImageLength = srcImage.length;
        int[] afterEffectArray = new int[srcImageLength];

        getRGBFromImage(srcImage);

        for(int index = 0; index < srcImageLength; index++) {
            afterEffectArray[index] = (int)(imgRed[index]*0.2989 + imgBlue[index]*0.5870 + imgGreen[index]*0.1140 + 0.5);
        }

        return afterEffectArray;
    }

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
}
