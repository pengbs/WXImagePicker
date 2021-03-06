package com.rxxb.imagepicker.crop.shape;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.rxxb.imagepicker.crop.config.CropIwaOverlayConfig;

/**
 * Created by yarolegovich on 04.02.2017.
 * https://github.com/yarolegovich
 */
public class CropIwaOvalShape extends CropIwaShape {

    private Path clipPath;

    public CropIwaOvalShape(CropIwaOverlayConfig config) {
        super(config);
        clipPath = new Path();
    }

    @Override
    protected void clearArea(Canvas canvas, RectF cropBounds, Paint clearPaint) {
        canvas.drawOval(cropBounds, clearPaint);
    }

    @Override
    protected void drawBorders(Canvas canvas, RectF cropBounds, Paint paint) {
        canvas.drawOval(cropBounds, paint);
        if (overlayConfig.isDynamicCrop()) {
            canvas.drawRect(cropBounds, paint);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void drawGrid(Canvas canvas, RectF cropBounds, Paint paint) {
        clipPath.rewind();
        clipPath.addOval(cropBounds, Path.Direction.CW);

        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipPath(clipPath);
        super.drawGrid(canvas, cropBounds, paint);
        canvas.restore();
    }

    @Override
    public CropIwaShapeMask getMask() {
        return new OvalShapeMask();
    }

    private static class OvalShapeMask implements CropIwaShapeMask {
        @Override
        public Bitmap applyMaskTo(Bitmap croppedRegion) {
            croppedRegion.setHasAlpha(true);

            Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            RectF ovalRect = new RectF(0, 0, croppedRegion.getWidth(), croppedRegion.getHeight());
            Path maskShape = new Path();
            //This is similar to ImageRect\Oval
            maskShape.addRect(ovalRect, Path.Direction.CW);
            maskShape.addOval(ovalRect, Path.Direction.CCW);

            Canvas canvas = new Canvas(croppedRegion);
            canvas.drawPath(maskShape, maskPaint);
            return croppedRegion;
        }
    }
}

