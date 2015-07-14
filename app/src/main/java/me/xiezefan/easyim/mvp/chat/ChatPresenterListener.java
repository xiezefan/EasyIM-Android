package me.xiezefan.easyim.mvp.chat;

import me.xiezefan.easyim.dao.ChatItem;

/**
 * Created by xiezefan on 15/5/9.
 */
public interface ChatPresenterListener {
    public void onChatItemSend(ChatItem chatItem);
    public void onChatItemStatusChange(ChatItem chatItem);
}
