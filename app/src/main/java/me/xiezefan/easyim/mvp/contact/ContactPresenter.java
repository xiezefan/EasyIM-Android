package me.xiezefan.easyim.mvp.contact;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;

/**
 * ContactFragment Presenter
 * Created by xiezefan on 15/5/4.
 */
public class ContactPresenter {
    private ContactView contactView;

    @Inject
    FriendDao friendDao;

    private List<Friend> friendSet;
    private List<Friend> searchResultSet;

    public ContactPresenter() {
        this.friendSet = new ArrayList<>();
    }

    public void setContactView(ContactView contactView) {
        this.contactView = contactView;
    }

    /**
     * 初始读取好友列表
     */
    public void initFriendList() {
        loadFriendList(0, 15);
    }

    /**
     * 读取好友列表
     * @param start 偏移值
     * @param row 行数
     */
    public void loadFriendList(int start, int row) {
        List<Friend> list = friendDao.queryBuilder()
                .orderAsc(FriendDao.Properties.Username)
                .offset(start)
                .limit(row)
                .list();
        friendSet.addAll(list);
        contactView.notifyContactListChange(friendSet, start, row);
    }

    /**
     * item 点击事件
     * @param position 下标
     */
    public void onContactItemClick(int position) {
        Friend item;
        if (contactView.getCurrentContactList() == friendSet) {
            item = friendSet.get(position);
        } else {
            item = searchResultSet.get(position);
        }
        contactView.startFriendHomeActivity(item);
    }

    /**
     * 搜索好友
     * @param text 搜索关键字
     */
    public void searchFriend(String text) {
        if (TextUtils.isEmpty(text)) {
            if (contactView.getCurrentContactList() != friendSet) {
                contactView.notifyContactListChange(friendSet, 0, searchResultSet.size());
            }
            return;
        }

        QueryBuilder<Friend> qb = friendDao.queryBuilder();
        WhereCondition whereCondition = qb.or(FriendDao.Properties.Nickname.like("%" + text + "%"), FriendDao.Properties.Username.like("%" + text + "%"));
        searchResultSet = qb.where(whereCondition).orderAsc(FriendDao.Properties.Username).limit(30).list();
        contactView.notifyContactListChange(searchResultSet, 0, searchResultSet.size());
    }

    public void onFriendSetLastItemChange(int lastItemPosition) {

    }

}
