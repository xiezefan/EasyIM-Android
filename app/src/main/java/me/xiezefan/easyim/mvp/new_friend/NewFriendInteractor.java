package me.xiezefan.easyim.mvp.new_friend;




import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import me.xiezefan.easyim.common.FriendHelper;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequest;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.model.FriendRequestStatus;
import me.xiezefan.easyim.model.MessageType;
import me.xiezefan.easyim.net.FriendshipResource;
import me.xiezefan.easyim.net.MessageResource;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.net.from.FriendshipAddForm;
import me.xiezefan.easyim.net.from.MessageSendForm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewFriendInteractor {
    @Inject
    FriendshipResource friendshipResource;
    @Inject
    UserResource userResource;
    @Inject
    MessageResource messageResource;
    @Inject
    FriendDao friendDao;
    @Inject
    FriendRequestDao friendRequestDao;



    public void addFriend(FriendRequest friendRequest, FriendAddCallback callback) {
        FriendshipAddForm dataForm = new FriendshipAddForm();
        dataForm.friend_id = friendRequest.getUid();

        MessageSendForm msgSendForm = new MessageSendForm();
        msgSendForm.setTo(friendRequest.getUid());
        msgSendForm.setType(MessageType.FRIEND_ADD_ACCEPT.name());
        Map<String, Object> content = new HashMap<>();
        content.put("uid", SPHelper.getString(SPHelper.USER_ID));
        msgSendForm.setContent(content);

        Observable.zip(friendshipResource.save(dataForm),
                userResource.getUserInfo(friendRequest.getUid()),
                messageResource.send(msgSendForm),
                (response, userVo, msgVo) -> {
                    Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(friendRequest.getUid())).unique();
                    if (friend == null) {
                        friend = new Friend();
                        friend.setUid(friendRequest.getUid());
                        friend.setUsername(userVo.username);
                        friend.setNickname(userVo.nickname);
                        friend.setAvatar(userVo.avatar);
                        friendDao.insert(friend);
                    }
                    // update friendRequest
                    friendRequest.setStatus(FriendRequestStatus.ACCEPT.name());
                    friendRequestDao.update(friendRequest);
                    return friend;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friend->callback.onAddFriendSuccess(friendRequest, friend),
                        error -> {
                            Logger.d(error.getMessage());
                            callback.onAddFriendFail();
                        });

    }


}

