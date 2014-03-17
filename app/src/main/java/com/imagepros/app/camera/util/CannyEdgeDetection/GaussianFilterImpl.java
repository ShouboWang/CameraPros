package com.imagepros.app.camera.util.CannyEdgeDetection;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.imagepros.app.camera.util.helper.HelperUtilImpl;
/**
 * Created by shoubo.wang on 17/03/14.
 */
public class GaussianFilterImpl {

    private int maskSize;
    private HelperUtilImpl helperUtil;

    public Bitmap applyCannyEdgeDetection( Bitmap sourceImage ) {
        Bitmap processedImage = applyGaussianBlur(sourceImage);

        return processedImage;
    }

    private Bitmap applyGaussianBlur( Bitmap sourceImage ) {
        Bitmap processedImage = Bitmap.createBitmap( sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getConfig() );

        int[][] gaussianFilter = helperUtil.getGaussianFilter();
        int gaussianFilterHalfValue = gaussianFilter.length / 2;

        int partialValue = 0;
        int totalValue = helperUtil.getGaussianFilterTotal(gaussianFilter);

        boolean useTotal = true;


        int sourceImageHeight = sourceImage.getHeight();
        int sourceImageWidth = sourceImage.getWidth();

        for( int row = 0; row < sourceImageHeight; row ++ ) {
            for( int col = 0; col < sourceImageWidth; col++ ) {
                int pixAlpha = Color.alpha(sourceImage.getPixel(row, col));
                int pixRed = 0;
                int pixGreen = 0;
                int pixBlue = 0;

                for( int filterRow = (-1) * gaussianFilterHalfValue; filterRow < gaussianFilterHalfValue; filterRow++ ) {
                    for( int filterCol = (-1) * gaussianFilterHalfValue; filterCol < gaussianFilterHalfValue; filterCol++ ) {
                        int pixY = row + filterRow;
                        int pixX = col + filterCol;
                        if( pixX < 0 || pixY < 0 || pixX > sourceImageWidth || pixY > sourceImageHeight ) {
                            useTotal = false;
                            continue;
                        }
                        if( !useTotal ) {
                            partialValue += gaussianFilter[row + gaussianFilterHalfValue][col + gaussianFilterHalfValue];
                        }

                        int sourcePix = sourceImage.getPixel(pixX, pixY);
                        pixRed += Color.red(sourcePix);
                        pixGreen += Color.green(sourcePix);
                        pixBlue += Color.blue(sourcePix);
                    }
                }

                if( useTotal ) {
                    partialValue = totalValue;
                }

                pixBlue /= partialValue;
                pixGreen /= partialValue;
                pixRed /= partialValue;

                processedImage.setPixel(row, col, Color.argb(pixAlpha, pixRed, pixGreen, pixBlue));
            }
        }

        return processedImage;
    }
}
