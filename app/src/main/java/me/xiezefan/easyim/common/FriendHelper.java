package me.xiezefan.easyim.common;

import android.text.TextUtils;

import me.xiezefan.easyim.dao.Friend;

/**
 * Friend Entity Helper
 * Created by xiezefan on 15/5/16.
 */
public class FriendHelper {
    public String getDisplayName(Friend friend) {
        if (TextUtils.isEmpty(friend.getNickname())) {
            return friend.getUsername();
        } else {
            return friend.getNickname();
        }
    }
}
