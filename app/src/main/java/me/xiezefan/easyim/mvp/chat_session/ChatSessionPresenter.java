package me.xiezefan.easyim.mvp.chat_session;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatSession;

public class ChatSessionPresenter {

    @Inject
    ChatSessionInteractor chatSessionInteractor;

    private ChatSessionView chatSessionView;
    private List<ChatSession> chatSessionList;

    public ChatSessionPresenter() {
        chatSessionList = new ArrayList<>();
    }

    public void setChatSessionView(ChatSessionView chatSessionView) {
        this.chatSessionView = chatSessionView;
    }


    public void initChatSessionList() {
        loadChatSessionList(0, 15);
    }

    public void loadChatSessionList(int start, int row) {
        List<ChatSession> list = chatSessionInteractor.loadChatSession(start, row);
        int _start = chatSessionList.size();
        int _row = list.size();

        chatSessionList.addAll(list);
        chatSessionView.notifyChatSessionsChange(chatSessionList, _start, _row);
    }

    public void onChatItemClick(int position) {
        ChatSession item = chatSessionList.get(position);
        item.setUnread(0);
        chatSessionView.notifyChatSessionsChange(chatSessionList, position, 0);
        chatSessionView.startChatActivity(item.getTargetId());
    }

    public void onReceiveChatItem(ChatItem chatItem) {
        int index = -1;
        for (int i=0; i<chatSessionList.size(); i++) {
            ChatSession cs = chatSessionList.get(i);
            if (cs.getId().equals(chatItem.getChatSessionId())) {
                cs.setLastMessage(chatItem.getContent());
                cs.setLastTime(chatItem.getCreateTime());
                if (!chatItem.getIsSelf()) {
                    cs.setUnread(cs.getUnread() + 1);
                }
                index = i;
                break;
            }
        }
        if (index == 0) {
            chatSessionView.notifyChatSessionsChange(chatSessionList, 0, 0);
        } else if (index > 0) {
            ChatSession chatSession = chatSessionList.get(index);
            chatSessionList.remove(index);
            chatSessionList.add(0, chatSession);
            chatSessionView.notifyChatSessionsChange(chatSessionList, 0, chatSessionList.size());
        } else {
            ChatSession cs = chatSessionInteractor.getChatSession(chatItem);
            chatSessionList.add(cs);
            chatSessionView.notifyChatSessionsChange(chatSessionList, chatSessionList.size() - 1, 1);
        }
    }

}
