package me.xiezefan.easyim.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Handler Resize Event RecyclerView
 * Created by xiezefan on 15/5/16.
 */
public class ResizeRecyclerView extends RecyclerView {
    private ResizeListener resizeListener;

    public ResizeRecyclerView(Context context) {
        super(context);
    }

    public ResizeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resizeListener != null) {
            resizeListener.onResize(w, h, oldw, oldh);
        }
    }

    public void setResizeListener(ResizeListener resizeListener) {
        this.resizeListener = resizeListener;
    }

    public interface ResizeListener {
        public void onResize(int w, int h, int oldw, int oldh);
    }
}
