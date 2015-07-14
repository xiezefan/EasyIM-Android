package me.xiezefan.easyim.mvp.friend_home;

import javax.inject.Inject;

import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;

public class FriendHomePresenter {

    @Inject
    FriendDao friendDao;

    private FriendHomeView friendHomeView;
    private Friend friend;

    public FriendHomePresenter() {

    }

    public void setFriendHomeView(FriendHomeView friendHomeView) {
        this.friendHomeView = friendHomeView;
    }

    public void onChatBtnClick() {
        friendHomeView.startChatActivity(friend.getUid());
    }

    public void loadFriend(String friendId) {
        friend = friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(friendId)).unique();
        friendHomeView.bindFriendData(friend);
    }

}

