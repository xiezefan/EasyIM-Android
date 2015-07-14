package me.xiezefan.easyim.mvp.gallery;

import java.util.List;

import me.xiezefan.easyim.mvp.base.BaseView;

public interface GalleryView extends BaseView {

    public void notifyDataSetChange(List<String> dataSet, int start, int row);
    public void toPhotoCropActivity(String photo);
    public void toCameraActivity();
}
