package me.xiezefan.easyim.mvp.chat_session;

import java.util.List;

import javax.inject.Inject;

import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatSession;
import me.xiezefan.easyim.dao.ChatSessionDao;

public class ChatSessionInteractor {
    @Inject
    ChatSessionDao chatSessionDao;

    public List<ChatSession> loadChatSession(int start, int row) {
        return chatSessionDao
                .queryBuilder()
                .orderDesc(ChatSessionDao.Properties.LastTime)
                .offset(start)
                .limit(row)
                .list();
    }

    public ChatSession getChatSession(ChatItem chatItem) {
        return chatSessionDao.queryBuilder().where(ChatSessionDao.Properties.Id.eq(chatItem.getChatSessionId())).unique();
    }




}
