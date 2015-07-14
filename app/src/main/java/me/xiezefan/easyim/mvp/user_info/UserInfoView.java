package me.xiezefan.easyim.mvp.user_info;

import android.net.Uri;

import java.util.Map;

import me.xiezefan.easyim.mvp.base.BaseView;

public interface UserInfoView extends BaseView {
    public void notifyDataSetChange(Map<String, String> dataSet);

    public void startImageCrop(Uri uri);
    public void startGallery();

    public void showTextEditor(String tag, String hint, String preFill);
    public void showSexEditor();
    public void showPhotoSelector();

    public void toLoginActivity();


}
