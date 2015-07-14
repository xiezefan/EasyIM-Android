package me.xiezefan.easyim.mvp.chat;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatItemDao;
import me.xiezefan.easyim.dao.ChatSession;
import me.xiezefan.easyim.dao.ChatSessionDao;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.model.MessageStatus;
import me.xiezefan.easyim.model.MessageType;
import me.xiezefan.easyim.net.MessageResource;
import me.xiezefan.easyim.net.from.MessageSendForm;
import me.xiezefan.easyim.util.DateUtil;
import me.xiezefan.easyim.util.JsonUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Chat 数据相关的业务方法
 * Created by xiezefan on 15/5/9.
 */
public class ChatInteractor {
    @Inject
    FriendDao friendDao;
    @Inject
    ChatItemDao chatItemDao;
    @Inject
    ChatSessionDao chatSessionDao;
    @Inject
    MessageResource messageResource;

    public List<ChatItem> loadChatItems(ChatSession chatSession, int start, int row) {
        return chatItemDao.queryBuilder()
                .where(ChatItemDao.Properties.ChatSessionId.eq(chatSession.getId()))
                .orderDesc(ChatItemDao.Properties.CreateTime)
                .offset(start)
                .limit(row)
                .list();
    }

    public ChatSession findOrCreateChatSession(String targetId) {
        return insertOrUpdateChatSession(targetId, "");
    }

    /**
     *
     * @param fromId 发送者ID
     * @param targetId 接受者ID
     * @param text 文本正文
     * @param listener 回调
     */


    /**
     * 发生文本消息
     * @param chatSession ChatSession Object
     * @param fromId 发送者ID
     * @param text 文本正文
     * @param listener 回调
     */
    public void sendText(ChatSession chatSession, String fromId, String text, ChatPresenterListener listener) {

        // build extras
        long createTime = DateUtil.getCurrentTimestamp();
        Map<String, Object> extras = new HashMap<String, Object>();
        extras.put("create_time", createTime + "");
        extras.put("msg_text", text);
        extras.put("uid", fromId);

        // build dataForm
        MessageSendForm dataForm = new MessageSendForm();
        dataForm.setTo(chatSession.getTargetId());
        dataForm.setContent(extras);
        dataForm.setType(MessageType.CHAT_TEXT.name());

        // build and save ChatItem
        ChatItem chatItem = new ChatItem();
        chatItem.setChatSessionId(chatSession.getId());
        chatItem.setTargetId(chatSession.getTargetId());
        chatItem.setType(MessageType.CHAT_TEXT.name());
        chatItem.setFromId(fromId);
        chatItem.setContent(text);
        chatItem.setIsSelf(true);
        chatItem.setExtras(JsonUtil.toJson(extras));
        chatItem.setStatus(MessageStatus.CREATE.name());
        chatItem.setCreateTime(createTime);
        chatItemDao.insert(chatItem);
        insertOrUpdateChatSession(chatSession.getTargetId(), text);

        listener.onChatItemSend(chatItem);

        messageResource.send(dataForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {
                    chatItem.setMsgId(msg.id);
                    chatItem.setStatus(MessageStatus.RECEIVED.name());
                    chatItemDao.update(chatItem);
                    listener.onChatItemStatusChange(chatItem);
                }, error -> {
                    chatItem.setStatus(MessageStatus.SEND_FAIL.name());
                    chatItemDao.update(chatItem);
                    listener.onChatItemStatusChange(chatItem);
                });

    }

    public void sendImage(ChatSession chatSession, String fromId, String imageUrl, ChatPresenterListener listener) {
        // build extras
        long createTime = DateUtil.getCurrentTimestamp();
        Map<String, Object> extras = new HashMap<String, Object>();
        extras.put("create_time", createTime + "");
        extras.put("img_url", imageUrl);
        extras.put("uid", fromId);

        // build dataForm
        MessageSendForm dataForm = new MessageSendForm();
        dataForm.setTo(chatSession.getTargetId());
        dataForm.setContent(extras);
        dataForm.setType(MessageType.CHAT_IMAGE.name());

        // build and save ChatItem
        ChatItem chatItem = new ChatItem();
        chatItem.setChatSessionId(chatSession.getId());
        chatItem.setTargetId(chatSession.getTargetId());
        chatItem.setType(MessageType.CHAT_IMAGE.name());
        chatItem.setFromId(fromId);
        chatItem.setContent(imageUrl);
        chatItem.setIsSelf(true);
        chatItem.setExtras(JsonUtil.toJson(extras));
        chatItem.setStatus(MessageStatus.CREATE.name());
        chatItem.setCreateTime(createTime);
        chatItemDao.insert(chatItem);
        insertOrUpdateChatSession(chatSession.getTargetId(), "[图片]");

        listener.onChatItemSend(chatItem);

        messageResource.send(dataForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {
                    chatItem.setMsgId(msg.id);
                    chatItem.setStatus(MessageStatus.RECEIVED.name());
                    chatItemDao.update(chatItem);
                    listener.onChatItemStatusChange(chatItem);
                }, error -> {
                    chatItem.setStatus(MessageStatus.SEND_FAIL.name());
                    chatItemDao.update(chatItem);
                    listener.onChatItemStatusChange(chatItem);
                });
    }



    /**
     * 清除会话消息未读数
     * @param chatSession  ChatSession Object
     */
    public void clearChatSession(ChatSession chatSession) {
        chatSession.setUnread(0);
        chatSessionDao.update(chatSession);
    }




    private ChatSession insertOrUpdateChatSession(String targetId, String text) {
        String title = "未命名聊天";
        String avatar = null;
        if (targetId.charAt(0) == 's') {
            Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(targetId)).unique();
            if (friend != null) {
                title = TextUtils.isEmpty(friend.getNickname()) ? friend.getUsername() : friend.getNickname();
                avatar = friend.getAvatar();
            }
        }

        ChatSession chatSession = chatSessionDao.queryBuilder().where(ChatSessionDao.Properties.TargetId.eq(targetId)).unique();
        if (chatSession == null) {
            chatSession = new ChatSession();
            chatSession.setTargetId(targetId);
            chatSession.setTitle(title);
            chatSession.setLastMessage(text);
            chatSession.setAvatar(avatar);
            chatSession.setUnread(0);
            chatSession.setLastTime(DateUtil.getCurrentTimestamp());
            chatSessionDao.insert(chatSession);
        } else {
            chatSession.setLastMessage(text);
            chatSession.setLastTime(DateUtil.getCurrentTimestamp());
            chatSessionDao.update(chatSession);
        }
        return chatSession;
    }

}
