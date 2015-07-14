package me.xiezefan.easyim.mvp.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.mvp.activity.BaseActivity;
import me.xiezefan.easyim.widget.ResizeRecyclerView;

/**
 * Created by XieZeFan on 2015/3/21 0021.
 */
public class ChatActivity extends BaseActivity implements ChatView {
    public static final String KEY_TARGET_ID = "KEY_TARGET_ID";

    private static final int RESULT_CODE_GALLERY = 0;
    private static final int RESULT_CODE_PHOTO_CROP = 1;

    @InjectView(R.id.rvChat)
    ResizeRecyclerView rvChat;
    @InjectView(R.id.etInputBox)
    EditText etInputBox;
    @Inject
    ChatPresenter chatPresenter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager chatLayoutManager;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
        EventBus.getDefault().register(this, 5);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    private void initView() {
        chatLayoutManager = new LinearLayoutManager(this);
        chatAdapter = new ChatAdapter(this);
        rvChat.setLayoutManager(chatLayoutManager);
        rvChat.setAdapter(chatAdapter);
        rvChat.requestFocus();
        rvChat.setResizeListener(new ResizeRecyclerView.ResizeListener() {
            @Override
            public void onResize(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });

    }

    private void initData() {
        ((Application) getApplication()).getApplicationGraph().inject(this);
        String targetId = getIntent().getStringExtra(KEY_TARGET_ID);

        chatPresenter.setChatView(this);
        chatPresenter.setTargetId(targetId);
        chatPresenter.initData();
    }

    @Override
    public void beforeInitToolbar() {
        toolbar.setTitle("谢泽帆");
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
    public void notifyChatItemChange(List<ChatItem> dataSet, int start, int row) {
        // row = 0 单点更新
        // row = 1 单点增加
        // row = dataSet.size() 全部更新
        chatAdapter.setDataSet(dataSet);
        if (row == 0) {
            chatAdapter.notifyItemChanged(start);
        } else if (row == 1) {
            chatAdapter.notifyItemInserted(start);
        } else if (row == dataSet.size()) {
            chatAdapter.notifyItemRangeInserted(start, row);
        } else {
            chatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == RESULT_CODE_GALLERY) {
                chatPresenter.processGalleryResult(data);
            } else if (requestCode == RESULT_CODE_PHOTO_CROP) {
                chatPresenter.processImageCrop(data);
            }
        }
    }

    @Override
    public void clearInputView() {
        etInputBox.setText("");
    }

    @Override
    public void scrollToBottom() {
        int count = chatAdapter.getItemCount();
        rvChat.smoothScrollToPosition(count);

    }

    @Override
    public void updateToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void updateAvatarMap(Map<String, String> avatarMap) {
        chatAdapter.setAvatarMap(avatarMap);
    }

    @Override
    public void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, RESULT_CODE_GALLERY);
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
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String text) {
        progressDialog = new MaterialDialog.Builder(this)
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

    @OnClick(R.id.btnGallery)
    public void onSendImageBtnClickj() {
        startGallery();
    }

    @OnClick(R.id.btnSend)
    public void onSendBtnClick(View view) {
        String text = etInputBox.getText().toString();
        chatPresenter.sendText(text);
    }

    /*---- Event ----*/

    public void onEvent(ChatItem chatItem) {
        if (chatPresenter.onReceiveMsg(chatItem)) {
            EventBus.getDefault().cancelEventDelivery(chatItem);
        }
    }
}
