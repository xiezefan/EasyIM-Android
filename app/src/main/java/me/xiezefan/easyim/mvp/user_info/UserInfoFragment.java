package me.xiezefan.easyim.mvp.user_info;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.mvp.activity.BaseActivity;
import me.xiezefan.easyim.mvp.activity.LoginActivity;
import me.xiezefan.easyim.util.StringUtil;

/**
 * 用户个人信息管理Fragment
 * Created by xiezefan on 15/5/17.
 */
public class UserInfoFragment extends Fragment implements UserInfoView{
    private static final int RESULT_CODE_GALLERY = 0;
    private static final int RESULT_CODE_CAMERA = 1;
    private static final int RESULT_CODE_PHOTO_CROP = 2;

    @InjectView(R.id.rvList)
    RecyclerView rvList;

    @Inject
    UserInfoPresenter userInfoPresenter;

    private UserInfoAdapter userInfoAdapter;
    private MaterialDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }



    private void initView() {
        // inject
        ((Application) getActivity().getApplication()).getApplicationGraph().inject(this);

        // init toolbar
        BaseActivity _activity = (BaseActivity) getActivity();
        _activity.getToolbar().setTitle("个人信息");

        // init list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        userInfoAdapter = new UserInfoAdapter(getActivity());
        userInfoAdapter.setUserInfoListener(userInfoPresenter);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(userInfoAdapter);

        userInfoPresenter.setUserInfoView(this);
        userInfoPresenter.initData();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(String.format("RequestCode:%s, ResultCode:%s", requestCode, resultCode));
        if (data != null && resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_CODE_GALLERY:
                    userInfoPresenter.processGalleryResult(data);
                    break;
                case RESULT_CODE_CAMERA:
                    userInfoPresenter.processCameraResult(data);
                    break;
                case RESULT_CODE_PHOTO_CROP:
                    userInfoPresenter.processPhotoCropResult(data);
                    break;
            }
        }
    }

    @Override
    public void notifyDataSetChange(Map<String, String> dataSet) {
        userInfoAdapter.setDataSet(dataSet);
        userInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, RESULT_CODE_GALLERY);
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = userInfoPresenter.getCameraTempFile();
        if (file != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, RESULT_CODE_CAMERA);
        }
    }

    @Override
    public void startImageCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_CODE_PHOTO_CROP);
    }

    @Override
    public void showTextEditor(String tag, String hint, String preFill) {
        new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(hint, preFill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        userInfoPresenter.updateItem(tag, input.toString());
                    }
                }).show();
    }

    @Override
    public void showSexEditor() {
        new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .items(new String[]{"男", "女", "保密"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == -1) {
                            return true;
                        }
                        userInfoPresenter.updateItem("sex", text.toString());
                        return true;
                    }
                })
                .positiveText("确定")
                .show();

    }

    @Override
    public void showPhotoSelector() {
        new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .items(new String[]{"图库", "相机"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == -1) {
                            return true;
                        } else if (which == 0) {
                            startGallery();
                        } else if (which == 1) {
                            startCamera();
                        }
                        return true;
                    }
                })
                .positiveText("确定")
                .show();

    }


    @Override
    public void toLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String text) {
        progressDialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .content(text)
                .progress(true, 0)
                .build();
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}
