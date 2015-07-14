package me.xiezefan.easyim.mvp.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import me.xiezefan.easyim.common.ConstantHelper;
import me.xiezefan.easyim.common.FriendHelper;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatSession;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.mvp.base.SimpleRequestListener;
import me.xiezefan.easyim.mvp.common.FileUploadInteractor;
import me.xiezefan.easyim.mvp.interactor.FriendInteractor;
import me.xiezefan.easyim.util.BitmapUtil;
import me.xiezefan.easyim.util.StringUtil;

/**
 * Chat Presenter
 * Created by xiezefan on 15/5/6.
 */
public class ChatPresenter implements ChatPresenterListener, SimpleRequestListener {
    @Inject
    ChatInteractor chatInteractor;
    @Inject
    FriendInteractor friendInteractor;
    @Inject
    FriendHelper friendHelper;
    @Inject
    FileUploadInteractor fileUploadInteractor;

    private ChatView chatView;
    private List<ChatItem> chatItemList;
    private String userId;
    private ChatSession chatSession;
    private Map<String, String> avatarMap;
    public ChatPresenter() {
        chatItemList = new ArrayList<>();
        userId = SPHelper.getString(SPHelper.USER_ID);
        avatarMap = new HashMap<>();
        String userAvatar = SPHelper.getString(SPHelper.USER_AVATAR);
        if (!TextUtils.isEmpty(userAvatar)) {
           avatarMap.put(userId, userAvatar);
        }
    }


    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    public void setTargetId(String targetId) {
        chatSession = chatInteractor.findOrCreateChatSession(targetId);
        chatInteractor.clearChatSession(chatSession);
        if (!TextUtils.isEmpty(chatSession.getAvatar())) {
            avatarMap.put(targetId, chatSession.getAvatar());
        }
        chatView.updateAvatarMap(avatarMap);
    }


    public void initData() {
        Friend friend = friendInteractor.findByUID(chatSession.getTargetId());
        chatView.updateToolbarTitle(friendHelper.getDisplayName(friend));
        loadChatList(0, 30);
        chatView.clearInputView();
        chatView.scrollToBottom();
    }


    /**
     * 从数据库中读取聊天历史
     * @param start  查询偏移
     * @param row 行数
     */
    public void loadChatList(int start, int row) {
        List<ChatItem> list =chatInteractor.loadChatItems(chatSession, start, row);

        int _start = list.size();
        int _row = chatItemList.size();

        for (ChatItem item : list) {
            chatItemList.add(0, item);
        }

        chatView.notifyChatItemChange(chatItemList, _start, _row);
    }

    public void sendText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        chatInteractor.sendText(chatSession, userId, text, this);
    }

    private String latestImageUrl = "";
    public void processGalleryResult(Intent data) {
        Uri uri = data.getData();
        chatView.startImageCrop(uri);
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(chatView.getContext().getContentResolver(), uri);
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            Logger.d(String.format("Width:%s, Height:%s", width, height));
//            if (width > 800 || height > 800) {
//                if (width > height) {
//                    height = height * (800 / width);
//                    width = 800;
//
//                } else {
//                    width = width * (800 / height);
//                    height = 800;
//                }
//                bitmap = BitmapUtil.compressBySize(bitmap, width, height);
//            }
//            Logger.d(String.format("Width:%s, Height:%s", width, height));
//
//            String filename = StringUtil.getRandomString(32) + ".png";
//            String filePath = "/EasyIM_Cache";
//            File file = BitmapUtil.saveToFile(bitmap, filePath, filename);
//            latestImageUrl = ConstantHelper.QI_NIU_BUCKET_HOST + filename;
//            fileUploadInteractor.upload(file, filename, this);
//            chatView.showLoading("上传图片中");
//
//        } catch (IOException e) {
//            chatView.showToast("读取图片失败，应用可能没有读取SD卡权限");
//        }
    }

    public void processImageCrop(Intent data) {
        Bitmap bitmap = data.getParcelableExtra("data");
        String filePath = "/EasyIM_Cache";
        String name = StringUtil.getRandomString(32) + ".png";

        File file = null;
        try {
            file = BitmapUtil.saveToFile(bitmap, filePath, name);
            latestImageUrl = ConstantHelper.QI_NIU_BUCKET_HOST + name;
            fileUploadInteractor.upload(file, name, this);
            chatView.showLoading("上传图片中");
        } catch (IOException e) {
            Logger.e(e);
            chatView.showToast("创建文件失败，应用可能无对应权限");
        }
    }



    /**
     * 处理收到新的ChatItem
     * @param chatItem ChatItem对象
     * @return 如果消费此消息，返回true， 否之返回false
     */
    public boolean onReceiveMsg(ChatItem chatItem) {
        if (!chatItem.getChatSessionId().equals(chatSession.getId())) {
            return false;
        }

        if (chatItem.getIsSelf()) {
            return true;
        }

        chatItemList.add(chatItem);

        int _row = chatItemList.size();
        int _start = _row - 1;

        chatView.notifyChatItemChange(chatItemList, _start, _row);

        chatView.clearInputView();
        chatView.scrollToBottom();
        return true;
    }

    @Override
    public void onChatItemSend(ChatItem chatItem) {
        chatItemList.add(chatItem);
        chatView.notifyChatItemChange(chatItemList, chatItemList.size(), 1);
        chatView.clearInputView();
        chatView.scrollToBottom();
        EventBus.getDefault().post(chatItem);
    }

    @Override
    public void onChatItemStatusChange(ChatItem chatItem) {
        int index = chatItemList.indexOf(chatItem);
        if (index >= 0) {
            chatView.notifyChatItemChange(chatItemList, index, 0);
        }
    }


    @Override
    public void requestSuccess(String tag) {
        if ("image_upload".equals(tag)) {
            chatInteractor.sendImage(chatSession, userId, latestImageUrl, this);
        }
        chatView.hideLoading();
    }

    @Override
    public void requestFail(String tag) {
        chatView.showToast("上传图片失败，请检查网络并重试");
    }
}
