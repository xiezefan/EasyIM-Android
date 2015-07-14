package me.xiezefan.easyim.service;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import de.greenrobot.event.EventBus;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatItemDao;
import me.xiezefan.easyim.dao.ChatSession;
import me.xiezefan.easyim.dao.ChatSessionDao;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequest;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.event.model.ChatMessageEvent;
import me.xiezefan.easyim.model.FriendRequestStatus;
import me.xiezefan.easyim.model.MessageStatus;
import me.xiezefan.easyim.model.MessageType;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.net.vo.MessageVo;
import me.xiezefan.easyim.net.vo.UserVo;
import me.xiezefan.easyim.util.DateUtil;
import me.xiezefan.easyim.util.JsonUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieZeFan on 2015/4/25 0025.
 */
public class MessageService {

    private FriendRequestDao friendRequestDao;
    private ChatItemDao chatItemDao;
    private ChatSessionDao chatSessionDao;
    private FriendDao friendDao;
    private UserResource userResource = RequestManager.getInstance().getUserResource();

    public void processMessage(MessageVo msg) {
        if (!msg.validate()) {
            return;
        }

        MessageType type = MessageType.format(msg.type);
        switch (type) {
            case FRIEND_ADD_REQUEST:
                processFriendAddRequest(msg);
                break;
            case FRIEND_ADD_ACCEPT:
                processFriendAddAccept(msg);
                break;
            case CHAT_TEXT:
                processChatText(msg);
                break;
            case CHAT_TIP:
                processChatTip(msg);
                break;
            case CHAT_IMAGE:
                processChatImage(msg);
            case UN_KNOWN_TYPE:
                // ignore
                break;
            
            default:
                // can't be happened
        }
    }

    private void processChatImage(MessageVo msg) {
        String _createTime = (String) msg.content.get("create_time");
        Long createTime = Long.valueOf(_createTime);
        String imgUrl = (String) msg.content.get("img_url");
        String friendId = (String) msg.content.get("uid");

        String userId = SPHelper.getString(SPHelper.USER_ID);

        // validate Friend exist
        Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(friendId)).unique();
        if (friend == null) {
            // TODO 此处应该像服务端查下是否存在该用户，查不到才return
            return;
        }

        // validate ChatSession exist
        ChatSession chatSession = chatSessionDao.queryBuilder().where(ChatSessionDao.Properties.TargetId.eq(friendId)).unique();
        if (chatSession == null) {
            String displayName = TextUtils.isEmpty(friend.getNickname()) ? friend.getUsername() : friend.getNickname();
            chatSession = new ChatSession();
            chatSession.setTargetId(friend.getUid());
            chatSession.setTitle(displayName);
            chatSession.setLastMessage("[图片]");
            chatSession.setAvatar(friend.getAvatar());
            chatSession.setUnread(1);
            chatSession.setLastTime(DateUtil.getCurrentTimestamp());
        } else {
            chatSession.setLastMessage("[图片]");
            chatSession.setUnread(chatSession.getUnread() + 1);
            chatSession.setLastTime(DateUtil.getCurrentTimestamp());
        }
        chatSessionDao.insertOrReplace(chatSession);


        ChatItem chatItem = new ChatItem();
        chatItem.setChatSessionId(chatSession.getId());
        chatItem.setTargetId(userId);
        chatItem.setType(MessageType.CHAT_IMAGE.name());
        chatItem.setFromId(friendId);
        chatItem.setContent(imgUrl);
        chatItem.setIsSelf(false);
        chatItem.setExtras(JsonUtil.toJson(msg.content));
        chatItem.setStatus(MessageStatus.RECEIVED.name());
        chatItem.setCreateTime(createTime);
        chatItemDao.insert(chatItem);

        // send event
        EventBus.getDefault().post(chatItem);

        // TODO 发送以接收命令
    }

    private void processFriendAddAccept(MessageVo msg) {
        String friendId = (String) msg.content.get("uid");
        userResource.getUserInfo(friendId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userVo -> {
                    Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(userVo.id)).unique();
                    if (friend == null) {
                        friend = userVo.generateFriend();
                        friendDao.insert(friend);
                    } else {
                        friend = userVo.fixFriend(friend);
                        friendDao.update(friend);
                    }
                }, error -> {
                    Logger.e("Receiver a FRIEND_ADD_ACCEPT message, but sync error.");
                });
    }

    private void processChatTip(MessageVo msg) {
        
    }

    private void processChatText(MessageVo msg) {
        String _createTime = (String) msg.content.get("create_time");
        Long createTime = Long.valueOf(_createTime);
        String msgText = (String) msg.content.get("msg_text");
        String friendId = (String) msg.content.get("uid");

        String userId = SPHelper.getString(SPHelper.USER_ID);

        // validate Friend exist
        Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(friendId)).unique();
        if (friend == null) {
            // TODO 此处应该像服务端查下是否存在该用户，查不到才return
            return;
        }

        // validate ChatSession exist
        ChatSession chatSession = chatSessionDao.queryBuilder().where(ChatSessionDao.Properties.TargetId.eq(friendId)).unique();
        if (chatSession == null) {
            String displayName = TextUtils.isEmpty(friend.getNickname()) ? friend.getUsername() : friend.getNickname();
            chatSession = new ChatSession();
            chatSession.setTargetId(friend.getUid());
            chatSession.setTitle(displayName);
            chatSession.setLastMessage(msgText);
            chatSession.setAvatar(friend.getAvatar());
            chatSession.setUnread(1);
            chatSession.setLastTime(DateUtil.getCurrentTimestamp());
        } else {
            chatSession.setLastMessage(msgText);
            chatSession.setUnread(chatSession.getUnread() + 1);
            chatSession.setLastTime(DateUtil.getCurrentTimestamp());
        }
        chatSessionDao.insertOrReplace(chatSession);


        ChatItem chatItem = new ChatItem();
        chatItem.setChatSessionId(chatSession.getId());
        chatItem.setTargetId(userId);
        chatItem.setType(MessageType.CHAT_TEXT.name());
        chatItem.setFromId(friendId);
        chatItem.setContent(msgText);
        chatItem.setIsSelf(false);
        chatItem.setExtras(JsonUtil.toJson(msg.content));
        chatItem.setStatus(MessageStatus.RECEIVED.name());
        chatItem.setCreateTime(createTime);
        chatItemDao.insert(chatItem);

        // send event
        EventBus.getDefault().post(chatItem);

        // TODO 发送以接收命令

    }

    private void processFriendAddRequest(MessageVo msg) {
        String uid = (String) msg.content.get("id");
        String requestMsg = (String) msg.content.get("msg");
        String displayName = (String) msg.content.get("name");

        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(displayName)) {
            return;
        }

        FriendRequest friendRequest = friendRequestDao.queryBuilder().where(FriendRequestDao.Properties.Uid.eq(uid)).unique();
        if (friendRequest == null) {
            friendRequest = new FriendRequest();
            friendRequest.setUid(uid);
            friendRequest.setDisplayName(displayName);
            friendRequest.setReason(requestMsg);
            friendRequest.setStatus(FriendRequestStatus.UNTREATED.name());
            friendRequest.setIsRead(false);
            friendRequest.setCreateTime(DateUtil.getCurrentTimestamp());
            friendRequestDao.insert(friendRequest);
        } else {
            friendRequest.setDisplayName(displayName);
            friendRequest.setReason(requestMsg);
            friendRequest.setStatus(FriendRequestStatus.UNTREATED.name());
            friendRequest.setIsRead(false);
            friendRequest.setCreateTime(DateUtil.getCurrentTimestamp());
            friendRequestDao.update(friendRequest);
        }

        // notify all
        EventBus.getDefault().post(friendRequest);


    }


    public MessageService() {
        friendRequestDao = Application.getInstance().getDaoSession().getFriendRequestDao();
        chatItemDao = Application.getInstance().getDaoSession().getChatItemDao();
        chatSessionDao = Application.getInstance().getDaoSession().getChatSessionDao();
        friendDao = Application.getInstance().getDaoSession().getFriendDao();
    }

}
