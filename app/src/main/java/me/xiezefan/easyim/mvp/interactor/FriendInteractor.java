package me.xiezefan.easyim.mvp.interactor;

import javax.inject.Inject;

import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;

/**
 * Created by xiezefan on 15/5/16.
 */
public class FriendInteractor {
    @Inject
    FriendDao friendDao;

    public Friend findByUID(String userId) {
        return friendDao.queryBuilder().where(FriendDao.Properties.Uid.eq(userId)).unique();
    }

}
