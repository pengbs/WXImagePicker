package com.rxxb.imagepicker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rxxb.imagepicker.ImagePicker;
import com.rxxb.imagepicker.R;
import com.rxxb.imagepicker.bean.ImageItem;
import com.rxxb.imagepicker.util.Utils;

import java.util.ArrayList;

/**
 * Created by 彭保生 on 2018/3/22.
 */

public class ImageThumbPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private ArrayList<ImageItem> images;       //当前需要显示的所有的图片数据
    private int mImageSize;
    private int selectedPosition;
    private OnThumbItemClickListener listener;

    public ImageThumbPreviewAdapter(Activity context) {
        mContext = context;
        this.images = ImagePicker.getInstance().getSelectedImages();
        mImageSize = Utils.getImageItemWidth(mContext, 6, 5);
    }

    public void setSelected(ImageItem item) {
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
        if (item == null) {
            selectedPosition = -1;
        } else {
            selectedPosition = images.indexOf(item);
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThumbViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_thumb_preview_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ThumbViewHolder) {
            ((ThumbViewHolder)holder).bindThumb(position);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setOnThumbItemClickListener(OnThumbItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnThumbItemClickListener {
        void onThumbItemClick(ImageItem imageItem);
    }

    private class ThumbViewHolder extends RecyclerView.ViewHolder {

        FrameLayout mFrameLayout;
        ImageView mItemView;
        View thumbView;

        ThumbViewHolder(View itemView) {
            super(itemView);
            mFrameLayout = (FrameLayout) itemView.findViewById(R.id.frame_thumb_preview);
            mFrameLayout.setLayoutParams(new AbsListView.LayoutParams(mImageSize, mImageSize)); //让图片是个正方形
            mItemView = (ImageView) itemView.findViewById(R.id.iv_thumb_preview);
            thumbView = itemView.findViewById(R.id.view_thumb_preview);
        }

        void bindThumb(int position){
            final ImageItem imageItem = images.get(position);
            if (selectedPosition == position) {
                thumbView.setBackgroundResource(R.drawable.bg_thumb_selceted_shape);
            } else {
                thumbView.setBackgroundDrawable(null);
            }
            mFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onThumbItemClick(imageItem);
                    }
                }
            });
            ImagePicker.getInstance().getImageLoader().displayImage(mContext, imageItem.path, mItemView, mImageSize, mImageSize); //显示图片
        }
    }
}