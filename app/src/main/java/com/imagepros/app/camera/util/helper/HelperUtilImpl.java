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
    public int getGaussianFilterTotal(int[][] gaussianFilter)
    {
        int totalvalue = 0;

        for(int row = 0; row < gaussianFilter.length; row++)
        {
            for(int col = 0; col < gaussianFilter[row].length; col++)
            {
                totalvalue += gaussianFilter[row][col];
            }
        }

        return totalvalue;
    }

    public int[][] getGaussianFilter()
    {
        int[][] gaussianFilter = new int[][]{{2,4,5,4,2},{4,9,12,9,4},{5,12,15,12,5},{4,9,12,9,4},{2,4,5,4,2}};
        return gaussianFilter;
    }

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
}
