package com.example.liumin.surfacecamera.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by liumin on 2016/8/18.
 */
public class ImageUtil {
    /**
     * ???Bitmap
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}