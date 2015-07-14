package me.xiezefan.easyim.mvp.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.util.CommonUtil;

/**
 * Gallery Adapter
 * Created by xiezefan on 15/5/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int VIEW_TYPE_CAMERA = 0;
    private static final int VIEW_TYPE_IMAGE = 1;
    private static final int IMAGE_POSITION_OFFSET = 1;

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> dataSet;
    private int cellSize;
    private GalleryListener galleryListener;

    public GalleryAdapter(Context context) {
        this.context = context;
        initData();

    }

    private void initData() {
        this.layoutInflater = LayoutInflater.from(context);
        this.cellSize = CommonUtil.getScreenWidth(context) / 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_CAMERA;
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_gallery, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        if (viewType == VIEW_TYPE_CAMERA) {
            Picasso.with(context)
                    .load(R.drawable.ic_camera)
                    .resize(cellSize, cellSize)
                    .into(viewHolder.ivImage);
            viewHolder.ivImage.setOnClickListener(this);
            viewHolder.ivImage.setTag(position);
        } else if (viewType == VIEW_TYPE_IMAGE) {
            Picasso.with(context)
                    .load(new File(dataSet.get(position - IMAGE_POSITION_OFFSET)))
                    .resize(cellSize, cellSize)
                    .placeholder(R.drawable.bg_default_gallery)
                    .into(viewHolder.ivImage);
            viewHolder.ivImage.setOnClickListener(this);
            viewHolder.ivImage.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 1 : 1 + dataSet.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (galleryListener != null) {
            if (position == 0) {
                galleryListener.onCameraItemClick();
            } else {
                galleryListener.onImageItemClick(position - 1);
            }
        }
    }

    public void setDataSet(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    public void setGalleryListener(GalleryListener galleryListener) {
        this.galleryListener = galleryListener;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivImage)
        public ImageView ivImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface GalleryListener {
        public void onCameraItemClick();
        public void onImageItemClick(int position);
    }

}
