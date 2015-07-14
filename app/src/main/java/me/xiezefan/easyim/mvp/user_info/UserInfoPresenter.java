package me.xiezefan.easyim.mvp.user_info;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import me.xiezefan.easyim.common.ConstantHelper;
import me.xiezefan.easyim.common.DataCleanHelper;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.mvp.base.SimpleRequestListener;
import me.xiezefan.easyim.mvp.common.FileUploadInteractor;
import me.xiezefan.easyim.util.BitmapUtil;
import me.xiezefan.easyim.util.StringUtil;


public class UserInfoPresenter implements UserInfoAdapter.UserInfoListener, SimpleRequestListener {
    @Inject
    UserInfoInteractor userInfoInteractor;
    @Inject
    FileUploadInteractor fileUploadInteractor;

    private UserInfoView userInfoView;
    private Map<String, String> dataSet;
    private String currentValue;
    private File cameraTempFile;
    public UserInfoPresenter() {
    }

    public void setUserInfoView(UserInfoView userInfoView) {
        this.userInfoView = userInfoView;
    }

    public void initData() {
        dataSet = new HashMap<>();
        dataSet.put("username", SPHelper.getString(SPHelper.USERNAME));
        dataSet.put("nickname", SPHelper.getString(SPHelper.NICKNAME));
        dataSet.put("description", SPHelper.getString(SPHelper.USER_DESCRIPTION));
        dataSet.put("sex", SPHelper.getString(SPHelper.USER_SEX));
        dataSet.put("location", SPHelper.getString(SPHelper.USER_LOCATION));
        dataSet.put("avatar", SPHelper.getString(SPHelper.USER_AVATAR));

        userInfoView.notifyDataSetChange(dataSet);
    }

    public void updateItem(String tag, String value) {
        userInfoView.showLoading("网络请求中...");
        currentValue = value;
        userInfoInteractor.updateUserInfo(tag, value, this);
    }

    public void processGalleryResult(Intent data) {
        Uri uri = data.getData();
        userInfoView.startImageCrop(uri);
    }

    public void processCameraResult(Intent data) {

        if (cameraTempFile != null) {
            userInfoView.startImageCrop(Uri.fromFile(cameraTempFile));
        } else {
            userInfoView.showToast("创建文件失败，应用可能无对应权限");
        }
    }

    public File getCameraTempFile() {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyIM_Cache");
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                userInfoView.showToast("创建文件失败，应用可能无对应权限");
                return null;
            }
        }

        cameraTempFile = new File(dir, StringUtil.getRandomString(32) + ".png");
        Logger.d("FilePath:" + cameraTempFile);
        return cameraTempFile;
    }

    public void processPhotoCropResult(Intent data) {
        Bitmap bitmap = data.getParcelableExtra("data");
        String filePath = "/EasyIM_Cache";
        String name = StringUtil.getRandomString(32) + ".png";

        File file = null;
        try {
            file = BitmapUtil.saveToFile(bitmap, filePath, name);
        } catch (IOException e) {
            Logger.e(e);
            userInfoView.showToast("创建文件失败，应用可能无对应权限");
        }
        currentValue = ConstantHelper.QI_NIU_BUCKET_HOST + name;
        userInfoView.showLoading("上传图片中");
        fileUploadInteractor.upload(file, name, this);

    }


    @Override
    public void onAvatarItemClick() {
        userInfoView.startGallery();
    }

    @Override
    public void onNicknameItemClick() {
        userInfoView.showTextEditor("nickname", "昵称", dataSet.get("nickname"));
    }

    @Override
    public void onDescriptionItemClick() {
        userInfoView.showTextEditor("description", "个性签名", dataSet.get("description"));
    }

    @Override
    public void onSexItemClick() {
        userInfoView.showSexEditor();
    }

    @Override
    public void onLocationItemClick() {
        userInfoView.showTextEditor("location", "所在地", dataSet.get("location"));
    }

    @Override
    public void onLoginOutBtnClick() {
        userInfoInteractor.cleanAllData();
        userInfoView.toLoginActivity();
    }

    @Override
    public void requestSuccess(String tag) {
        if ("image_upload".equals(tag)) {
            userInfoInteractor.updateUserInfo("avatar", currentValue, this);
        } else if ("avatar".equals(tag)) {
            dataSet.put(tag, currentValue);
            SPHelper.setString(SPHelper.USER_AVATAR, currentValue);
            userInfoView.notifyDataSetChange(dataSet);
            userInfoView.hideLoading();
        } else {
            dataSet.put(tag, currentValue);
            userInfoView.notifyDataSetChange(dataSet);
            userInfoView.hideLoading();
        }

    }

    @Override
    public void requestFail(String tag) {
        userInfoView.hideLoading();
        userInfoView.showToast("网络请求失败，请稍后重试");
    }
}
