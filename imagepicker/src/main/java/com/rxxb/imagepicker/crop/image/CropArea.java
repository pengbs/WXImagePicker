package com.rxxb.imagepicker.crop.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author yarolegovich
 * 25.02.2017.
 */
public class CropArea {

    public static CropArea create(RectF coordinateSystem, RectF imageRect, RectF cropRect) {
        return new CropArea(
                moveRectToCoordinateSystem(coordinateSystem, imageRect),
                moveRectToCoordinateSystem(coordinateSystem, cropRect));
    }

    private static Rect moveRectToCoordinateSystem(RectF system, RectF rect) {
        float originX = system.left, originY = system.top;
        return new Rect(
                Math.round(rect.left - originX), Math.round(rect.top - originY),
                Math.round(rect.right - originX), Math.round(rect.bottom - originY));
    }

    private final Rect imageRect;
    private final Rect cropRect;

    public CropArea(Rect imageRect, Rect cropRect) {
        this.imageRect = imageRect;
        this.cropRect = cropRect;
    }

    public Bitmap applyCropTo(Bitmap bitmap) {
        int x = findRealCoordinate(bitmap.getWidth(), cropRect.left, imageRect.width());
        int y = findRealCoordinate(bitmap.getHeight(), cropRect.top, imageRect.height());
        int width = Math.abs(findRealCoordinate(bitmap.getWidth(), cropRect.width(), imageRect.width()));
        int height = Math.abs(findRealCoordinate(bitmap.getHeight(), cropRect.height(), imageRect.height()));
        int cropX, cropY, cropWidth, cropHeight;
        Bitmap immutableCropped = null;
        if (imageRect.contains(cropRect)) {
            //
            System.out.println("完全包含....");
            immutableCropped = Bitmap.createBitmap(bitmap, x, y, width, height);
        } else if (Rect.intersects(imageRect, cropRect)) {
            System.out.println("有相交的部分");
            immutableCropped = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            immutableCropped.eraseColor(Color.BLACK);//填充颜色
            Canvas canvas = new Canvas(immutableCropped);
            Rect intersectRect = new Rect();
            if (x < 0) {
                intersectRect.left = -x;
                cropX = 0;
            } else {
                intersectRect.left = 0;
                cropX = x;
            }
            if (y < 0) {
                intersectRect.top = -y;
                cropY = 0;
            } else {
                intersectRect.top = 0;
                cropY = y;
            }
            cropWidth = findRealCoordinate(bitmap.getWidth(),Math.min(cropRect.right, imageRect.right) - Math.max(cropRect.left, imageRect.left), imageRect.width());
            cropHeight = findRealCoordinate(bitmap.getHeight(),Math.min(cropRect.bottom, imageRect.bottom) - Math.max(cropRect.top, imageRect.top), imageRect.height());
            Bitmap tempCropped = Bitmap.createBitmap(bitmap, cropX, cropY, cropWidth, cropHeight);
            intersectRect.right = intersectRect.left + cropWidth;
            intersectRect.bottom = intersectRect.top + cropHeight;
            canvas.drawBitmap(tempCropped, new Rect(0, 0, cropWidth, cropHeight), intersectRect, null);
            tempCropped.recycle();
        } else {
            System.out.println("完全没相交的部分");
        }
        return immutableCropped.copy(immutableCropped.getConfig(), true);
    }


    private int findRealCoordinate(int imageRealSize, int cropCoordinate, float cropImageSize) {
        return Math.round((imageRealSize * cropCoordinate) / cropImageSize);
    }

}