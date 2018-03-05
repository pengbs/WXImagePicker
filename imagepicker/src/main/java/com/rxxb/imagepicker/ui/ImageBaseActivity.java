package com.rxxb.imagepicker.ui;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.rxxb.imagepicker.ImagePicker;
import com.rxxb.imagepicker.R;
import com.rxxb.imagepicker.util.CornerUtils;
import com.rxxb.imagepicker.util.Utils;
import com.rxxb.imagepicker.view.SystemBarTintManager;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImageBaseActivity extends AppCompatActivity {

    protected SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.ip_color_primary_dark);  //设置上方状态栏的颜色
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ImagePicker.getInstance().restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ImagePicker.getInstance().saveInstanceState(outState);
    }

    protected void setConfirmButtonBg(Button mBtnOk) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        StateListDrawable btnOkDrawable = CornerUtils.btnSelector(Utils.dp2px(this, 3), Color.parseColor(imagePicker.getViewColor().getoKButtonTitleColorNormal())
                , Color.parseColor(imagePicker.getViewColor().getoKButtonTitleColorNormal()), Color.parseColor(imagePicker.getViewColor().getoKButtonTitleColorDisabled()), -2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtnOk.setBackground(btnOkDrawable);
        } else {
            mBtnOk.setBackgroundDrawable(btnOkDrawable);
        }
        mBtnOk.setPadding(Utils.dp2px(this, 12), 0, Utils.dp2px(this, 12), 0);
        mBtnOk.setTextColor(Color.parseColor(imagePicker.getViewColor().getBarItemTextColor()));
    }
}
