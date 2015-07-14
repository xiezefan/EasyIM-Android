package me.xiezefan.easyim.net.vo;

import me.xiezefan.easyim.dao.Friend;

/**
 * User View Model
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class UserVo {
    public String id;
    public String username;
    public String nickname;
    public String avatar;
    public String description;
    public String location;
    public String sex;
    
    
    public Friend generateFriend() {
        Friend friend = new Friend();
        friend.setUid(this.id);
        friend.setUsername(this.username);
        friend.setNickname(this.nickname);
        friend.setAvatar(this.avatar);
        friend.setDescription(this.description);
        friend.setSex(this.sex);
        friend.setLocation(this.location);
        return friend;
    }

    public Friend fixFriend(Friend friend) {
        friend.setUsername(this.username);
        friend.setNickname(this.nickname);
        friend.setAvatar(this.avatar);
        friend.setDescription(this.description);
        friend.setSex(this.sex);
        friend.setLocation(this.location);
        return friend;
    }
}
