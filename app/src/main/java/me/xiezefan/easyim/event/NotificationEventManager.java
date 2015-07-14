package me.xiezefan.easyim.event;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.Random;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import me.xiezefan.easyim.common.FriendHelper;
import me.xiezefan.easyim.common.PromptHelper;
import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequest;
import me.xiezefan.easyim.module.ForApplication;
import me.xiezefan.easyim.mvp.chat.ChatActivity;
import me.xiezefan.easyim.mvp.main.MainActivity;
import me.xiezefan.easyim.util.JsonUtil;

/**
 * Notification Event Manager
 * Created by XieZeFan on 2015/4/26 0026.
 */
public class NotificationEventManager {

    @Inject
    PromptHelper promptHelper;
    @Inject
    FriendDao friendDao;
    @Inject
    FriendHelper friendHelper;


    @Inject
    @ForApplication
    Context context;

    private Random randomInt;

    /**
     * 好友添加请求事件
     * @param newFriend 好友对象
     */
    public void onEventMainThread(FriendRequest newFriend) {
        promptHelper.showBigText(context,
                generateNotificationId(),
                "好友请求" ,
                newFriend.getDisplayName() + "请求添加你为好友",
                new Intent(context, MainActivity.class));
    }

    /**
     * 聊天消息事件
     * @param chatItem 聊天消息
     */
    public void onEventMainThread(ChatItem chatItem) {
        String fromId = chatItem.getFromId();
        String friendName;
        String notifyText;
        if ('s' == fromId.charAt(0)) {
            Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(fromId)).unique();
            if (friend == null) {
                return;
            }
            friendName = friendHelper.getDisplayName(friend);
            notifyText = chatItem.getContent();
        } else {
            // TODO generate notifyText when support group chat
            friendName = "HelloWorld";
            notifyText = "Generate notifyText when support Group Chat";
        }

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_TARGET_ID, fromId);
        promptHelper.showBigText(context,
                new BigDecimal(chatItem.getChatSessionId()).intValueExact(),
                friendName ,
                notifyText,
                intent);

    }

    public int generateNotificationId() {
        return randomInt.nextInt(10000);
    }






    public void register() {
        EventBus.getDefault().register(this);
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    public NotificationEventManager() {
        this.randomInt = new Random();
    }
}
