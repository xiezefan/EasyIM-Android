package me.xiezefan.easyim.event.model;

import me.xiezefan.easyim.dao.Friend;

public class NewFriendEvent {
    public Friend friend;

    public NewFriendEvent(Friend friend) {
        this.friend = friend;
    }
}
