package me.xiezefan.easyim.mvp.main;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.net.FriendshipResource;
import me.xiezefan.easyim.net.vo.UserVo;
import rx.schedulers.Schedulers;

public class MainInteractor {
    @Inject
    FriendshipResource friendshipResource;
    @Inject
    FriendDao friendDao;

    public void synchronizationContact() {
        Map<String, Friend> friendMapper = new HashMap<String, Friend>();
        List<Friend> localFriends = friendDao.loadAll();

        for (Friend friend : localFriends) {
            friendMapper.put(friend.getUid(), friend);
        }

        friendshipResource.list()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(list -> {
                    List<Friend> currentFriends = new ArrayList<>();
                    for (UserVo userVo : list) {
                        Friend friend = friendMapper.get(userVo.id);
                        if (friend == null) {
                            friend = userVo.generateFriend();
                        } else {
                            friend = userVo.fixFriend(friend);
                        }
                        currentFriends.add(friend);
                    }
                    friendDao.insertOrReplaceInTx(currentFriends);
                    Logger.d(String.format("Update %s friends", currentFriends.size()));
                }, error -> Logger.e("Update friends fail."));
    }
}
