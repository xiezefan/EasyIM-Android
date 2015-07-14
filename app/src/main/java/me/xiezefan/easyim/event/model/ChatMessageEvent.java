package me.xiezefan.easyim.event.model;

import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatSession;

/**
 * 聊天消息事件
 * Created by xiezefan on 15/5/14.
 */
public class ChatMessageEvent {
    public Action action;
    public ChatItem chatItem;
    public ChatSession chatSession;

    public ChatMessageEvent(ChatSession chatSession, ChatItem chatItem, Action action) {
        this.chatSession = chatSession;
        this.action = action;
        this.chatItem = chatItem;
    }

    public ChatMessageEvent() {
    }

    public enum Action {
        RECEIVED, CREATE, UPDATE
    }
}
