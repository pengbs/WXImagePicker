package com.rxxb.imagepicker.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxxb.imagepicker.ImagePicker;
import com.rxxb.imagepicker.R;
import com.rxxb.imagepicker.crop.CropIwaView;
import com.rxxb.imagepicker.crop.config.CropIwaSaveConfig;
import com.rxxb.imagepicker.crop.shape.CropIwaOvalShape;
import com.rxxb.imagepicker.view.CropImageView;

import java.io.File;

public class CropActivity extends ImageBaseActivity implements View.OnClickListener {

    private static final String EXTRA_URI = "CropImage";
    private ImagePicker imagePicker;
    private CropIwaView cropView;
    private ProgressDialog mProgressDialog;
    private String dstPath;

    public static Intent callingIntent(Context context, Uri imageUri) {
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(EXTRA_URI, imageUri);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        //初始化View
        findViewById(R.id.tv_rotate).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        Button mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setText(getString(R.string.ip_complete));
        mBtnOk.setOnClickListener(this);
        imagePicker = ImagePicker.getInstance();
        Uri imageUri = getIntent().getParcelableExtra(EXTRA_URI);
        cropView = (CropIwaView) findViewById(R.id.cv_crop_image);
        cropView.setImageUri(imageUri);
        cropView.configureOverlay()
                .setAspectRatio(imagePicker.getAspectRatio())
                .setDynamicCrop(imagePicker.isDynamicCrop())
                .apply();
        if (imagePicker.getStyle() == CropImageView.Style.CIRCLE) {
            cropView.configureOverlay().setCropShape(new CropIwaOvalShape(cropView.configureOverlay())).apply();
        }
        File cropCacheFolder;
        if (imagePicker.getCutType() == 2) {
            cropCacheFolder = new File(Environment.getExternalStorageDirectory() + "/RXImagePicker/");
        } else {
            cropCacheFolder = imagePicker.getCropCacheFolder(this);
        }
        if (!cropCacheFolder.exists() || !cropCacheFolder.isDirectory()) {
            cropCacheFolder.mkdirs();
        }
        dstPath = new File(cropCacheFolder, "IMG_" + System.currentTimeMillis() + ".png").getAbsolutePath();
        cropView.setCropSaveCompleteListener(new CropIwaView.CropSaveCompleteListener() {

            @Override
            public void onCroppedRegionSaved(Uri bitmapUri) {
                dismiss();
                ImagePicker.galleryAddPic(getApplicationContext(), bitmapUri);
                Intent intent = new Intent();
                intent.putExtra(ImagePicker.EXTRA_OUT_URI, bitmapUri);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        cropView.setErrorListener(new CropIwaView.ErrorListener() {
            @Override
            public void onError(Throwable e) {
                dismiss();
            }
        });

        setConfirmButtonBg(mBtnOk);
        findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor(imagePicker.getViewColor().getNaviBgColor()));
        ((TextView)findViewById(R.id.tv_des)).setTextColor(Color.parseColor(imagePicker.getViewColor().getNaviTitleColor()));
    }

    private void dismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            setResult(RESULT_CANCELED);
            finish();
        } else if(id == R.id.tv_rotate){
            cropView.rotateImage(90);
        } else if (id == R.id.btn_ok) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("正在处理中...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
            CropIwaSaveConfig.Builder builder = new CropIwaSaveConfig.Builder(Uri.fromFile(new File(dstPath)));
            if (imagePicker.getOutPutX() != 0 && imagePicker.getOutPutY() != 0 && !imagePicker.isOrigin()) {
                builder.setSize(imagePicker.getOutPutX(), imagePicker.getOutPutY());
            }
            builder.setQuality(imagePicker.getQuality());
            cropView.crop(builder.build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismiss();
        mProgressDialog = null;
    }
}