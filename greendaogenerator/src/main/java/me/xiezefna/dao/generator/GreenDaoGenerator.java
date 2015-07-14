package me.xiezefna.dao.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "me.xiezefan.easyim.dao");

        addFriend(schema);
        addFriendRequest(schema);
        addChatSession(schema);
        addChatItem(schema);
        generate(schema, args[0]);
    }

    private static void addFriend(Schema schema) {
        Entity friend = schema.addEntity("Friend");
        friend.addIdProperty();
        friend.addStringProperty("uid");
        friend.addStringProperty("username");
        friend.addStringProperty("nickname");
        friend.addStringProperty("avatar");
        friend.addStringProperty("description");
        friend.addStringProperty("sex");
        friend.addStringProperty("location");
    }

    public static void addFriendRequest(Schema schema) {
        Entity request = schema.addEntity("FriendRequest");
        request.addIdProperty();
        request.addStringProperty("uid");
        request.addStringProperty("username");
        request.addStringProperty("nickname");
        request.addStringProperty("displayName");
        request.addStringProperty("avatar");
        request.addStringProperty("reason");
        request.addStringProperty("status");
        request.addBooleanProperty("isRead");
        request.addLongProperty("createTime");
    }

    public static void addChatSession(Schema schema) {
        Entity chatSession = schema.addEntity("ChatSession");
        chatSession.addIdProperty();
        chatSession.addStringProperty("title");
        chatSession.addStringProperty("lastMessage");
        chatSession.addStringProperty("avatar");
        chatSession.addIntProperty("unread");
        chatSession.addLongProperty("lastTime");
        // 聊天对象Id，可能为单聊对象，可能为群聊对象
        chatSession.addStringProperty("targetId").index().unique();
    }


    public static void addChatItem(Schema schema) {
        Entity chatItem = schema.addEntity("ChatItem");
        chatItem.addIdProperty();
        // 与服务端对应的消息ID
        chatItem.addStringProperty("msgId").index();
        // Chat Session 的 ID
        chatItem.addLongProperty("chatSessionId");
        // 聊天对象Id，可能为单聊对象，可能为群聊对象
        chatItem.addStringProperty("targetId").index();
        chatItem.addStringProperty("type");
        // 消息发送者， 因为考虑到群聊消息， 所以必须多设置此项目
        chatItem.addStringProperty("fromId");
        chatItem.addStringProperty("content");
        chatItem.addStringProperty("extras");
        chatItem.addStringProperty("status");
        chatItem.addBooleanProperty("isSelf");
        chatItem.addLongProperty("createTime");

    }


    private static void generate(Schema schema, String path) throws Exception {
        de.greenrobot.daogenerator.DaoGenerator daoGenerator = new de.greenrobot.daogenerator.DaoGenerator();
        daoGenerator.generateAll(schema, path);
    }
}
