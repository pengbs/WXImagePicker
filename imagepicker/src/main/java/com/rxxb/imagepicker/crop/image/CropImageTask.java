package com.rxxb.imagepicker.crop.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;

import com.rxxb.imagepicker.crop.config.CropIwaSaveConfig;
import com.rxxb.imagepicker.crop.shape.CropIwaShapeMask;
import com.rxxb.imagepicker.crop.util.CropIwaUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Yaroslav Polyakov on 22.03.2017.
 * https://github.com/polyak01
 */

class CropImageTask extends AsyncTask<Void, Void, Throwable> {

    private Context context;
    private CropArea cropArea;
    private CropIwaShapeMask mask;
    private Uri srcUri;
    private CropIwaSaveConfig saveConfig;
    private float mCurrentAngle;

    public CropImageTask(
            Context context, CropArea cropArea, CropIwaShapeMask mask,
            Uri srcUri, CropIwaSaveConfig saveConfig, float mCurrentAngle) {
        this.context = context;
        this.cropArea = cropArea;
        this.mask = mask;
        this.srcUri = srcUri;
        this.saveConfig = saveConfig;
        this.mCurrentAngle = mCurrentAngle;
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            Bitmap bitmap = CropIwaBitmapManager.get().loadToMemory(
                    context, srcUri, saveConfig.getWidth(),
                    saveConfig.getHeight());

            if (bitmap == null) {
                return new NullPointerException("Failed to load bitmap");
            }
            if (mCurrentAngle > 0) {
                Matrix tempMatrix = new Matrix();
                tempMatrix.setRotate(mCurrentAngle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        tempMatrix, true);
                if (bitmap != rotatedBitmap) {
                    bitmap.recycle();
                }
                bitmap = rotatedBitmap;
            }
            Bitmap cropped = cropArea.applyCropTo(bitmap);

            cropped = mask.applyMaskTo(cropped);

            Uri dst = saveConfig.getDstUri();
            OutputStream os = context.getContentResolver().openOutputStream(dst);
            cropped.compress(saveConfig.getCompressFormat(), saveConfig.getQuality(), os);
            CropIwaUtils.closeSilently(os);

            bitmap.recycle();
            cropped.recycle();
        } catch (IOException e) {
            return e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Throwable throwable) {
        if (throwable == null) {
            CropIwaResultReceiver.onCropCompleted(context, saveConfig.getDstUri());
        } else {
            CropIwaResultReceiver.onCropFailed(context, throwable);
        }
    }
}