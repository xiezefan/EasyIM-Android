package me.xiezefan.easyim.mvp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.mvp.adapter.FriendSquareAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieZeFan on 2015/4/25 0025.
 */
public class FriendsSquareActivity extends BaseActivity {

    @InjectView(R.id.rvFriends)
    RecyclerView rvFriends;

    private FriendSquareAdapter friendSquareAdapter;
    private UserResource userResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_square);

        initRecyclerView();
        initData();

    }

    private void initRecyclerView() {
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        this.friendSquareAdapter = new FriendSquareAdapter(this);
        rvFriends.setAdapter(friendSquareAdapter);
    }

    private void initData() {
        userResource = RequestManager.getInstance().getUserResource();
        userResource.list(0, 15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> friendSquareAdapter.setDataSet(list));
    }


}
