package com.imagepros.app.camera.util.helper;

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
}
