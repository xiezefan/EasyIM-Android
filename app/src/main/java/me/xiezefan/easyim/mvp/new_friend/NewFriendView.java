package me.xiezefan.easyim.mvp.new_friend;

import java.util.List;

import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendRequest;

public interface NewFriendView {

    public void showProgress();

    public void hideProgress();

    public void showToast(String text);

    public void notifyFriendRequestsChange(List<FriendRequest> dataSet, int start, int end);

    public void startFriendHomeActivity(Friend friend);
}
