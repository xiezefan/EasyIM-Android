package me.xiezefan.easyim.mvp.new_friend;




import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendRequest;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.event.model.NewFriendEvent;

public class NewFriendPresenter implements FriendAddCallback {

    @Inject
    NewFriendInteractor newFriendInteractor;
    @Inject
    FriendRequestDao friendRequestDao;

    private NewFriendView newFriendView;
    private List<FriendRequest> friendRequests;

    public NewFriendPresenter() {
        this.friendRequests = new ArrayList<>();
    }

    public void setNewFriendView(NewFriendView newFriendView) {
        this.newFriendView = newFriendView;
    }

    public void onFriendRequestItemClick(int position) {
        newFriendView.showProgress();
        FriendRequest item = friendRequests.get(position);
        newFriendInteractor.addFriend(item, this);
    }

    public void cancelCurrentRequest() {}

    public void initFriendRequests() {
        loadFriendRequests(0, 15);
    }

    public void onLastVisibleItemChange(int lastVisiblePosition) {

    }

    private void loadFriendRequests(int start, int row) {
        List<FriendRequest> list = friendRequestDao.queryBuilder()
                .orderDesc(FriendRequestDao.Properties.CreateTime)
                .offset(start)
                .limit(row)
                .list();
        int oldSize = friendRequests.size();
        friendRequests.addAll(list);
        newFriendView.notifyFriendRequestsChange(friendRequests, oldSize, row);
    }

    public void onReceiveNewFriendRequest(FriendRequest request) {
        friendRequests.add(0, request);
        newFriendView.notifyFriendRequestsChange(friendRequests, 0, friendRequests.size());
    }


    /**
     * 添加好友成功的回调
     * @param friendRequest 添加好友的请求对象
     */
    @Override
    public void onAddFriendSuccess(FriendRequest friendRequest, Friend friend) {
        int position = friendRequests.indexOf(friendRequest);
        newFriendView.hideProgress();
        if (position >= 0) {
            newFriendView.notifyFriendRequestsChange(friendRequests, position, 0);
        }
        EventBus.getDefault().post(new NewFriendEvent(friend));

        newFriendView.startFriendHomeActivity(friend);
    }

    @Override
    public void onAddFriendFail() {
        newFriendView.hideProgress();
        newFriendView.showToast("添加好友失败, 请稍后重试");
    }
}
