package me.xiezefan.easyim.mvp.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.mvp.activity.BaseActivity;

/**
 * Gallery
 * Created by xiezefan on 15/5/16.
 */
public class GalleryActivity extends BaseActivity implements GalleryView, GalleryAdapter.GalleryListener {
    @InjectView(R.id.rvGallery)
    RecyclerView rvGallery;
    @Inject
    GalleryPresenter galleryPresenter;

    private GalleryAdapter galleryAdapter;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initView();
    }

    private void initView() {
        ((Application) getApplication()).getApplicationGraph().inject(this);
        galleryAdapter = new GalleryAdapter(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setAdapter(galleryAdapter);

        galleryPresenter.setGalleryView(this);
        galleryPresenter.initData();

    }


    @Override
    public void beforeInitToolbar() {
        toolbar.setTitle("选择图片");
    }

    @Override
    public void afterInitToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void notifyDataSetChange(List<String> dataSet, int start, int row) {
        galleryAdapter.setDataSet(dataSet);
        galleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void toPhotoCropActivity(String photo) {

    }

    @Override
    public void toCameraActivity() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String text) {

    }

    @Override
    public void showLoading(String text) {
        progressDialog = new MaterialDialog.Builder(this)
                .content(text)
                .progress(true, 0).show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    public void onCameraItemClick() {

    }

    @Override
    public void onImageItemClick(int position) {

    }
}
