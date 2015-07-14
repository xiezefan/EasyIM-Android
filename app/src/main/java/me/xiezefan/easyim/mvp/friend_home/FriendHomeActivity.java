package me.xiezefan.easyim.mvp.friend_home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.mvp.activity.BaseActivity;
import me.xiezefan.easyim.mvp.chat.ChatActivity;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;

/**
 * 好友主页
 * Created by xiezefan on 15/4/30.
 */
public class FriendHomeActivity extends BaseActivity implements FriendHomeView {
    public static final String KEY_FRIEND_ID = "KEY_FRIEND_ID";

    @InjectView(R.id.ivAvatar)
    ImageView ivAvatar;
    @InjectView(R.id.tvUsername)
    TextView tvUsername;
    @InjectView(R.id.tvDescription)
    TextView tvDescription;

    @Inject
    FriendHomePresenter friendHomePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_home);
        initData();
    }

    private void initData() {
        ((Application) getApplication()).getApplicationGraph().inject(this);
        friendHomePresenter.setFriendHomeView(this);


        Intent intent = getIntent();
        String  friendId = intent.getStringExtra(KEY_FRIEND_ID);
        friendHomePresenter.loadFriend(friendId);
    }


    @Override
    public void beforeInitToolbar() {
        toolbar.setTitle("用户信息");
    }

    @Override
    public void afterInitToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick(R.id.btnChat)
    public void btnChat(View view) {
        friendHomePresenter.onChatBtnClick();
    }

    @Override
    public void startChatActivity(String targetId) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_TARGET_ID, targetId);
        startActivity(intent);
    }

    @Override
    public void bindFriendData(Friend friend) {
        int avatarSize = DisplayUtil.dip2px(this, 48);
        String displayName = TextUtils.isEmpty(friend.getNickname()) ? friend.getUsername() : friend.getNickname();
        String description = TextUtils.isEmpty(friend.getDescription()) ? "该同学好懒，什么都没留下" : friend.getDescription();
        tvUsername.setText(displayName);
        tvDescription.setText(description);
        if (!TextUtils.isEmpty(friend.getAvatar())) {
            Picasso.with(this)
                    .load(friend.getAvatar())
                    .resize(avatarSize, avatarSize)
                    .placeholder(getResources().getDrawable(R.drawable.default_user_profile))
                    .transform(new RoundedTransformation())
                    .into(ivAvatar);
        }
    }
}
