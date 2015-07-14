package me.xiezefan.easyim.service;

import android.text.TextUtils;

import javax.inject.Inject;

import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequest;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.model.FriendRequestStatus;
import me.xiezefan.easyim.net.FriendshipResource;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.from.FriendshipAddForm;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieZeFan on 2015/4/17 0017.
 */
public class FriendshipService {
    @Inject
    FriendshipResource friendshipResource;
    @Inject
    FriendDao friendDao;
    @Inject
    FriendRequestDao friendRequestDao;

    public void synchronizationRemote() {
        friendshipResource.list()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap(list -> Observable.from(list))
                .subscribe(friendVo -> {
                    Friend friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(friendVo.id)).unique();
                    if (friend == null) {
                        friend = new Friend();
                        friend.setUid(friendVo.id);
                        friend.setUsername(friendVo.username);
                        friend.setNickname(friendVo.nickname);
                        friend.setAvatar(friendVo.avatar);
                    } else {
                        friend.setUsername(friendVo.username);
                        friend.setNickname(friendVo.nickname);
                        friend.setAvatar(friendVo.avatar);
                    }
                    friendDao.insertOrReplace(friend);
                });
    }

    public void addFriend(final FriendRequest friendRequest) {
        FriendshipAddForm dataForm = new FriendshipAddForm();
        dataForm.friend_id = friendRequest.getUid();

        friendshipResource.save(dataForm)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    // TODO show add friend Tip

                    Friend friend = new Friend();
                    friend.setUid(friendRequest.getUid());
                    friend.setUsername(friendRequest.getUsername());
                    friend.setNickname(TextUtils.isEmpty(friendRequest.getNickname()) ? friendRequest.getDisplayName() : friendRequest.getNickname());
                    friend.setAvatar(friendRequest.getAvatar());
                    friendDao.insert(friend);

                }, error -> {
                    // TODO show error info
                });


        // update friendRequest
        friendRequest.setStatus(FriendRequestStatus.ACCEPT.name());
        friendRequestDao.update(friendRequest);
    }



}
