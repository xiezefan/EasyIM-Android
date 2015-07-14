package me.xiezefan.easyim.mvp.new_friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.FriendRequest;
import me.xiezefan.easyim.model.FriendRequestStatus;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;

/**
 * New Friend RecyclerView Adapter
 * Created by XieZeFan on 2015/3/22 0022.
 */
public class NewFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    private static final int VIEW_TYPE_MESSAGE = 3;
    public static final int FRIEND_REQUEST_ITEM_COUNT_OFFSET = 3;
    private Context context;
    private LayoutInflater layoutInflater;

    private int avatarSize;
    private RoundedTransformation roundedTransformation;
    private List<FriendRequest> friendRequests;
    private OnItemClickListener onItemClickListener;


    public NewFriendAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        this.avatarSize = DisplayUtil.dip2px(context, 48);
        this.roundedTransformation = new RoundedTransformation();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 || position == 1) {
            return VIEW_TYPE_TITLE;
        } else if (position == 2) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_MESSAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TITLE:
                return new TitleViewHolder(layoutInflater.inflate(R.layout.item_friend_title, parent, false));
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(layoutInflater.inflate(R.layout.item_friend_header, parent, false));
            case VIEW_TYPE_MESSAGE:
                return new MessageViewHolder(layoutInflater.inflate(R.layout.item_friend_message, parent, false));
           default:
                // can no be happen
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == VIEW_TYPE_MESSAGE) {
            // 好友请求消息
            FriendRequest item = friendRequests.get(position - FRIEND_REQUEST_ITEM_COUNT_OFFSET);
            MessageViewHolder viewHolder = (MessageViewHolder) holder;
            viewHolder.tvUsername.setText(item.getDisplayName());
            viewHolder.tvReason.setText(item.getReason());
            FriendRequestStatus status = FriendRequestStatus.valueOf(item.getStatus());
            if (status == FriendRequestStatus.UNTREATED) {
                viewHolder.tvAccept.setVisibility(View.GONE);
                viewHolder.tvUntreated.setVisibility(View.VISIBLE);
                viewHolder.tvUntreated.setTag(position);
                viewHolder.tvUntreated.setOnClickListener(this);
            } else {
                viewHolder.tvAccept.setVisibility(View.VISIBLE);
                viewHolder.tvUntreated.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getAvatar())) {
                Picasso.with(context)
                        .load(item.getAvatar())
                        .resize(avatarSize, avatarSize)
                        .placeholder(context.getResources().getDrawable(R.drawable.default_user_profile))
                        .transform(roundedTransformation)
                        .into(viewHolder.ivAvatar);
            }

        }  else if (viewType == VIEW_TYPE_TITLE) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            if (position == 0) {
                viewHolder.tvTitle.setText("添加朋友");
            } else {
                viewHolder.tvTitle.setText("用户广场");
            }
            viewHolder.llWrapper.setTag(position);
            viewHolder.llWrapper.setClickable(true);
            viewHolder.llWrapper.setOnClickListener(this);
        }
    }


    @Override
    public int getItemCount() {
        return friendRequests == null ? FRIEND_REQUEST_ITEM_COUNT_OFFSET : friendRequests.size() + FRIEND_REQUEST_ITEM_COUNT_OFFSET;
    }

    public void updateFriendRequests(List<FriendRequest> requests) {
        this.friendRequests = requests;
    }





    @Override
    public void onClick(View v) {
        if (onItemClickListener == null) {
            return;
        }
        int position = (int) v.getTag();
        if (position == 0) {
            onItemClickListener.onFriendSearchItemClick();
        } else if (position == 1) {
            onItemClickListener.onFriendSquareItemClick();
        } else if (position >= FRIEND_REQUEST_ITEM_COUNT_OFFSET) {
            position = position - FRIEND_REQUEST_ITEM_COUNT_OFFSET;
            onItemClickListener.onFriendRequestItemClick(position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.llWrapper)
        public LinearLayout llWrapper;
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvTitle)
        public TextView tvTitle;
        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvUsername)
        public TextView tvUsername;
        @InjectView(R.id.tvReason)
        public TextView tvReason;
        @InjectView(R.id.tvAccept)
        public TextView tvAccept;
        @InjectView(R.id.tvUntreated)
        public TextView tvUntreated;


        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnItemClickListener {
        public void onFriendSquareItemClick();
        public void onFriendSearchItemClick();
        public void onFriendRequestItemClick(int position);
    }


}
