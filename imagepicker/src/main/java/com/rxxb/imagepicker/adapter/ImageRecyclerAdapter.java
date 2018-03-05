package com.rxxb.imagepicker.adapter;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rxxb.imagepicker.ImagePicker;
import com.rxxb.imagepicker.R;
import com.rxxb.imagepicker.bean.ImageItem;
import com.rxxb.imagepicker.ui.ImageBaseActivity;
import com.rxxb.imagepicker.ui.ImageGridActivity;
import com.rxxb.imagepicker.util.Utils;
import com.rxxb.imagepicker.view.SuperCheckBox;
import com.rxxb.imagepicker.view.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载相册图片的RecyclerView适配器
 *
 * 用于替换原项目的GridView，使用局部刷新解决选中照片出现闪动问题
 *
 * 替换为RecyclerView后只是不再会导致全局刷新，
 *
 * 但还是会出现明显的重新加载图片，可能是picasso图片加载框架的问题
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-04-05  10:04
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {


    private static final int ITEM_TYPE_CAMERA = 0;  //第一个条目是相机
    private static final int ITEM_TYPE_NORMAL = 1;  //第一个条目不是相机
    private ImagePicker imagePicker;
    private Activity mActivity;
    private ArrayList<ImageItem> images;       //当前需要显示的所有的图片数据
    private ArrayList<ImageItem> mSelectedImages; //全局保存的已经选中的图片数据
    private boolean isShowCamera;         //是否显示拍照按钮
    private int mImageSize;               //每个条目的大小
    private LayoutInflater mInflater;
    private OnImageItemClickListener listener;   //图片被点击的监听
    private TextDrawable.IBuilder mDrawableBuilder;
    private List<Integer> alreadyChecked;

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImageItem imageItem, int position);
    }

    public void refreshData(ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) this.images = new ArrayList<>();
        else this.images = images;
        notifyDataSetChanged();
    }

    public void refreshCheckedData(int position) {
        List<Integer> checked = new ArrayList<>(imagePicker.getSelectLimit());
        if (alreadyChecked != null) {
            checked.addAll(alreadyChecked);
        }
        String payload = "add";
        if (!checked.contains(position)) {
            //选中新的
            checked.add(position);
        } else {
            payload = "remove";
        }
        if (checked.size() == imagePicker.getSelectLimit()) {
            notifyItemRangeChanged(isShowCamera ? 1 : 0, images.size(), payload);
        } else {
            if (!checked.isEmpty()) {
                for (Integer check : checked) {
                    notifyItemChanged(check, payload);
                }
            }
        }
    }

    /**
     * 构造方法
     */
    public ImageRecyclerAdapter(Activity activity, ArrayList<ImageItem> images) {
        this.mActivity = activity;
        if (images == null || images.size() == 0) this.images = new ArrayList<>();
        else this.images = images;

        mImageSize = Utils.getImageItemWidth(mActivity);
        imagePicker = ImagePicker.getInstance();
        isShowCamera = imagePicker.isShowCamera();
        mSelectedImages = imagePicker.getSelectedImages();
        mInflater = LayoutInflater.from(activity);
        mDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .width(Utils.dp2px(activity, 18))
                .height(Utils.dp2px(activity, 18))
                .endConfig().roundRect(Utils.dp2px(activity, 3));
        alreadyChecked = new ArrayList<>(imagePicker.getSelectLimit());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA){
            return new CameraViewHolder(mInflater.inflate(R.layout.adapter_camera_item,parent,false));
        }
        return new ImageViewHolder(mInflater.inflate(R.layout.adapter_image_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder){
            ((CameraViewHolder)holder).bindCamera();
        }else if (holder instanceof ImageViewHolder){
            ((ImageViewHolder)holder).bind(position);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (holder instanceof CameraViewHolder){
            ((CameraViewHolder)holder).bindCamera();
        }else if (holder instanceof ImageViewHolder){
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            if (payloads == null || payloads.isEmpty()) {
                viewHolder.bind(position);
            } else {
                final ImageItem imageItem = getItem(position);
                int index = mSelectedImages.indexOf(imageItem);
                if (index >= 0) {
                    if (!alreadyChecked.contains(position)) {
                        alreadyChecked.add(position);
                    }
                    viewHolder.cbCheck.setChecked(true);
                    viewHolder.cbCheck.setButtonDrawable(mDrawableBuilder.build(String.valueOf(index+1), Color.parseColor("#1AAD19")));
                } else {
                    alreadyChecked.remove((Integer)position);
                    viewHolder.cbCheck.setChecked(false);
                    viewHolder.cbCheck.setButtonDrawable(R.mipmap.checkbox_normal);
                }
                int selectLimit = imagePicker.getSelectLimit();
                if (mSelectedImages.size() >= selectLimit) {
                    viewHolder.mask.setVisibility(index < 0 ? View.VISIBLE : View.GONE);
                } else {
                    viewHolder.mask.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCamera) return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return isShowCamera ? images.size() + 1 : images.size();
    }

    public ImageItem getItem(int position) {
        if (isShowCamera) {
            if (position == 0) return null;
            return images.get(position - 1);
        } else {
            return images.get(position);
        }
    }

    private class ImageViewHolder extends ViewHolder{

        View rootView;
        ImageView ivThumb;
        View mask;
        View checkView;
        SuperCheckBox cbCheck;

        ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            mask = itemView.findViewById(R.id.mask);
            checkView=itemView.findViewById(R.id.checkView);
            cbCheck = (SuperCheckBox) itemView.findViewById(R.id.cb_check);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
        }

        void bind(final int position){
            final ImageItem imageItem = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onImageItemClick(rootView, imageItem, position);
                }
            });
            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbCheck.setChecked(!cbCheck.isChecked());
                    int selectLimit = imagePicker.getSelectLimit();
                    if (cbCheck.isChecked() && mSelectedImages.size() >= selectLimit) {
                        Toast.makeText(mActivity.getApplicationContext(), mActivity.getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                        cbCheck.setChecked(false);
                        //mask.setVisibility(View.GONE);
                    } else {
                        imagePicker.addSelectedImageItem(position, imageItem, cbCheck.isChecked());
                        //mask.setVisibility(View.VISIBLE);
                    }
                }
            });
            //根据是否多选，显示或隐藏checkbox
            if (imagePicker.isMultiMode()) {
                cbCheck.setVisibility(View.VISIBLE);
                int index = mSelectedImages.indexOf(imageItem);
                if (index >= 0) {
                    if (!alreadyChecked.contains(position)) {
                        alreadyChecked.add(position);
                    }
                    //mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                    cbCheck.setButtonDrawable(mDrawableBuilder.build(String.valueOf(index+1), Color.parseColor("#1AAD19")));
                } else {
                    alreadyChecked.remove((Integer)position);
                    //mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                    cbCheck.setButtonDrawable(R.mipmap.checkbox_normal);
                }
                int selectLimit = imagePicker.getSelectLimit();
                if (mSelectedImages.size() >= selectLimit) {
                    mask.setVisibility(index < 0 ? View.VISIBLE : View.GONE);
                } else {
                    mask.setVisibility(View.GONE);
                }
            } else {
                cbCheck.setVisibility(View.GONE);
                alreadyChecked.clear();
            }
            imagePicker.getImageLoader().displayImage(mActivity, imageItem.path, ivThumb, mImageSize, mImageSize); //显示图片
        }
    }

    private class CameraViewHolder extends ViewHolder{

        View mItemView;

        CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        void bindCamera(){
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            mItemView.setTag(null);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((ImageBaseActivity) mActivity).checkPermission(Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, ImageGridActivity.REQUEST_PERMISSION_CAMERA);
                    } else {
                        imagePicker.takePicture(mActivity, ImagePicker.REQUEST_CODE_TAKE);
                    }
                }
            });
        }
    }
}
