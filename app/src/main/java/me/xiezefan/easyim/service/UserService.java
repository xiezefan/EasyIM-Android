package me.xiezefan.easyim.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.model.MessageType;
import me.xiezefan.easyim.net.MessageResource;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.from.MessageSendForm;
import me.xiezefan.easyim.net.vo.RequestFailError;
import me.xiezefan.easyim.net.vo.UserVo;
import me.xiezefan.easyim.util.CollectionUtil;
import me.xiezefan.easyim.util.JsonUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * User Service
 * Created by XieZeFan on 2015/4/17 0017.
 */
public class UserService {
    private MessageResource messageResource;

    public void persistenceUser(UserVo user, String password) {
        SPHelper.setString(SPHelper.USER_ID, user.id);
        SPHelper.setString(SPHelper.USERNAME, user.username);
        SPHelper.setString(SPHelper.NICKNAME, user.nickname);
        SPHelper.setString(SPHelper.USER_AVATAR, user.avatar);
        SPHelper.setString(SPHelper.USER_DESCRIPTION, user.description);
        SPHelper.setString(SPHelper.USER_LOCATION, user.location);
        SPHelper.setString(SPHelper.USER_SEX, user.sex);

        String authCode = user.username + ":" + password;
        authCode = "Basic " + Base64.encodeToString(authCode.getBytes(), Base64.DEFAULT);
        SPHelper.setString(SPHelper.AUTH_CODE, authCode);

    }

    public void registerJPush(Context content, String userId) {
        JPushInterface.setAlias(content, userId, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == 0) {
                    Logger.d("JPush Alias Register Success");
                } else {
                    Logger.e("JPush Alias Register Fail:" + s);
                }
            }
        });
    }



    public boolean validateLogin() {
        String authCode = SPHelper.getString(SPHelper.AUTH_CODE);
        return !TextUtils.isEmpty(authCode);
    }

    /**
     * 请求添加好友
     * @param friendId 好友Id
     * @param requestMsg 请求信息
     */
    public void requestFriend(String friendId, String requestMsg) {
        String id = SPHelper.getString(SPHelper.USER_ID);
        String username = SPHelper.getString(SPHelper.USERNAME);
        String nickname = SPHelper.getString(SPHelper.NICKNAME);
        String displayName = TextUtils.isEmpty(nickname) ? username : nickname;


        MessageSendForm dataForm = new MessageSendForm();
        dataForm.setTo(friendId);
        dataForm.setType(MessageType.FRIEND_ADD_REQUEST.toString());
        Map<String, Object> extras = CollectionUtil.map()
                .put("id", id)
                .put("msg", requestMsg)
                .put("name", displayName).build();
        dataForm.setContent(extras);


        messageResource.send(dataForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    // TODO show notification bar ticker
                    Logger.d("Friend Request Message Send Success");
                }, error -> {
                    // TODO show notification bar ticker
                    Logger.e("Friend Request Message Send Fail, e" + JsonUtil.toJson(((RequestFailError)error).getResponse()));
                });

    }




    private static UserService instance;

    public static  UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private UserService() {
        messageResource = RequestManager.getInstance().getMessageResource();
    }
}
