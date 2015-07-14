package me.xiezefan.easyim.mvp.contact;

import java.util.List;

import me.xiezefan.easyim.dao.Friend;

/**
 * ContactFragment
 * Created by xiezefan on 15/5/4.
 */
public interface ContactView {

    public void startFriendHomeActivity(Friend friend);

    public void notifyContactListChange(List<Friend> dataSet, int start, int row);

    public List<Friend> getCurrentContactList();
}
