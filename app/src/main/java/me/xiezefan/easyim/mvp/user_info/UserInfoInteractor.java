package me.xiezefan.easyim.mvp.user_info;

import javax.inject.Inject;

import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.dao.ChatItemDao;
import me.xiezefan.easyim.dao.ChatSessionDao;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.mvp.base.SimpleRequestListener;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.net.from.UserUpdateForm;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserInfoInteractor {

    @Inject
    UserResource userResource;
    @Inject
    FriendDao friendDao;
    @Inject
    FriendRequestDao friendRequestDao;
    @Inject
    ChatSessionDao chatSessionDao;
    @Inject
    ChatItemDao chatItemDao;

    public void updateUserInfo(String key, String value, SimpleRequestListener listener) {
        UserUpdateForm dataForm = new UserUpdateForm(key, value);
        userResource.update(dataForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    listener.requestSuccess(key);
                    if ("nickname".equals(key)) {
                        SPHelper.setString(SPHelper.NICKNAME, value);
                    } else if ("avatar".equals(key)) {
                        SPHelper.setString(SPHelper.USER_AVATAR, value);
                    } else if ("sex".equals(key)) {
                        SPHelper.setString(SPHelper.USER_SEX, value);
                    } else if ("description".equals(key)) {
                        SPHelper.setString(SPHelper.USER_DESCRIPTION, value);
                    } else if ("location".equals(key)) {
                        SPHelper.setString(SPHelper.USER_LOCATION, value);
                    }
                }, error -> listener.requestFail(key));
    }


    public void cleanAllData() {
        friendDao.deleteAll();
        friendRequestDao.deleteAll();
        chatSessionDao.deleteAll();
        chatItemDao.deleteAll();
        SPHelper.clearAll();
    }

}
