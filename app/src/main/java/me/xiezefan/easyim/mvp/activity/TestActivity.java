package me.xiezefan.easyim.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.mvp.gallery.GalleryActivity;
import me.xiezefan.easyim.mvp.gallery.GalleryInteractor;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.from.RegisterForm;
import me.xiezefan.easyim.net.vo.DefaultResponseVo;
import me.xiezefan.easyim.net.vo.RequestFailError;
import me.xiezefan.easyim.util.JsonUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btnGallery)
    public void toGallery() {
        startActivity(new Intent(this, GalleryActivity.class));
    }


}
