package me.xiezefan.easyim.mvp.chat_session;

import java.util.List;

import me.xiezefan.easyim.dao.ChatSession;

public interface ChatSessionView {
    public void startChatActivity(String targetId);
    public void notifyChatSessionsChange(List<ChatSession> dataSet, int start, int row);
}
