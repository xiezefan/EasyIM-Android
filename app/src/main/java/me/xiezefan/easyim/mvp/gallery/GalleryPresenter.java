package me.xiezefan.easyim.mvp.gallery;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xiezefan.easyim.util.JsonUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GalleryPresenter {

    @Inject
    GalleryInteractor galleryInteractor;

    private GalleryView galleryView;
    private List<String> photos;

    public GalleryPresenter() {
        photos = new ArrayList<>();
    }

    public void setGalleryView(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    public void initData() {
        galleryView.showLoading("正在扫描图片");
        Observable.just(galleryInteractor.scanImage(galleryView.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map -> {
                    for (String key : map.keySet()) {
                        photos.addAll(map.get(key));
                    }
                    Logger.d("Photos:" + JsonUtil.toJson(photos));
                    galleryView.notifyDataSetChange(photos, 0, photos.size());
                    galleryView.hideLoading();
                }, error -> Logger.e(error.getMessage()));

    }

    public void onCameraItemClick() {
        galleryView.toCameraActivity();
    }

    public void onPhotoItemClick(int position) {
        galleryView.toPhotoCropActivity(photos.get(position));
    }











}
