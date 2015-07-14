package me.xiezefan.easyim.model;

/**
 * Created by XieZeFan on 2015/4/25 0025.
 */
public enum  MessageType {
    // 好友添加请求消息
    FRIEND_ADD_REQUEST,
    FRIEND_ADD_ACCEPT,

    CHAT_TEXT,
    CHAT_TIP,
    CHAT_IMAGE,


    // 未知消息
    UN_KNOWN_TYPE;


    public static MessageType format(String name) {
        MessageType result;
        try {
            result = MessageType.valueOf(name);
        } catch (Exception e) {
            result = UN_KNOWN_TYPE;
        }
        return result;
    }

}
