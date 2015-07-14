package me.xiezefan.easyim.mvp.new_friend;

import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendRequest;

/**
 * 添加好友监听器
 * Created by XieZeFan on 2015/4/28 0028.
 */
public interface FriendAddCallback {
    public void onAddFriendSuccess(FriendRequest request, Friend friend);
    public void onAddFriendFail();
}
