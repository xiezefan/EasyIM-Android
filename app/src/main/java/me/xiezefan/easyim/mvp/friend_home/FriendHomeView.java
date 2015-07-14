package me.xiezefan.easyim.mvp.friend_home;

import me.xiezefan.easyim.dao.Friend;

public interface FriendHomeView {

    public void startChatActivity(String targetId);

    public void bindFriendData(Friend friend);

}
