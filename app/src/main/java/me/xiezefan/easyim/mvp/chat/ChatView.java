package me.xiezefan.easyim.mvp.chat;

import android.content.Context;
import android.net.Uri;

import java.util.List;
import java.util.Map;

import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.mvp.base.BaseView;

public interface ChatView extends BaseView {

    public void notifyChatItemChange(List<ChatItem> dataSet, int start, int row);
    public void clearInputView();
    public void scrollToBottom();
    public void updateToolbarTitle(String title);
    public void updateAvatarMap(Map<String, String> avatarMap);

    public void startGallery();
    public void startImageCrop(Uri uri);
}
