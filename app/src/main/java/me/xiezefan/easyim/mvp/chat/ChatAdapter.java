package me.xiezefan.easyim.mvp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.model.MessageType;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;

/**
 * Created by XieZeFan on 2015/3/21 0021.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LEFT_CHAT_ITEM = 0;
    private static final int VIEW_TYPE_RIGHT_CHAT_ITEM = 1;
    private static final int VIEW_TYPE_NOTIFICATION_ITEM = 2;
    private static final int VIEW_TYPE_LEFT_IMAGE_ITEM = 3;
    private static final int VIEW_TYPE_RIGHT_IMAGE_ITEM =4;

    private Context context;
    private LayoutInflater layoutInflater;

    private List<ChatItem> dataSet;
    private RoundedTransformation roundedTransformation;
    private int avatarSize;
    private ChatItemListener chatItemListener;
    private Map<String, String> avatarMap;

    public ChatAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        this.avatarSize = DisplayUtil.dip2px(context, 48);
        this.roundedTransformation = new RoundedTransformation();
        this.avatarMap = new HashMap<>();

    }


    @Override
    public int getItemViewType(int position) {
        ChatItem item = dataSet.get(position);
        MessageType msgType = MessageType.format(item.getType());
        switch (msgType) {
            case CHAT_TEXT:
                if (item.getIsSelf()) {
                    return VIEW_TYPE_RIGHT_CHAT_ITEM;
                } else {
                    return VIEW_TYPE_LEFT_CHAT_ITEM;
                }
            case CHAT_IMAGE:
                if (item.getIsSelf()) {
                    return VIEW_TYPE_RIGHT_IMAGE_ITEM;
                } else {
                    return VIEW_TYPE_LEFT_IMAGE_ITEM;
                }
            case CHAT_TIP:
                return VIEW_TYPE_NOTIFICATION_ITEM;
            default:
                return VIEW_TYPE_NOTIFICATION_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LEFT_CHAT_ITEM) {
            return new LeftChatItemViewHolder(layoutInflater.inflate(R.layout.item_chat_left, parent, false));
        } else if (viewType == VIEW_TYPE_RIGHT_CHAT_ITEM) {
            return new RightChatItemViewHolder(layoutInflater.inflate(R.layout.item_chat_right, parent, false));
        } else if (viewType == VIEW_TYPE_NOTIFICATION_ITEM) {
            return new NotificationItemViewHolder(layoutInflater.inflate(R.layout.item_chat_notification, parent, false));
        } else if (viewType == VIEW_TYPE_LEFT_IMAGE_ITEM) {
            return  new LeftImageChatItemViewHolder(layoutInflater.inflate(R.layout.item_chat_image_left, parent, false));
        } else if (viewType == VIEW_TYPE_RIGHT_IMAGE_ITEM) {
            return new RightImageChatItemViewHolder(layoutInflater.inflate(R.layout.item_chat_image_right, parent, false));
        }
        return new NotificationItemViewHolder(layoutInflater.inflate(R.layout.item_chat_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        ChatItem item = dataSet.get(position);


        if (viewType == VIEW_TYPE_LEFT_CHAT_ITEM) {
            LeftChatItemViewHolder viewHolder = (LeftChatItemViewHolder) holder;
            viewHolder.tvContent.setText(item.getContent());
            if (avatarMap.containsKey(item.getFromId())) {
                String targetAvatarUrl = avatarMap.get(item.getFromId());
                Picasso.with(context)
                        .load(targetAvatarUrl)
                        .resize(avatarSize, avatarSize)
                        .transform(roundedTransformation)
                        .placeholder(R.drawable.default_user_profile)
                        .into(viewHolder.ivAvatar);
            } else {
                viewHolder.ivAvatar.setImageResource(R.drawable.default_user_profile);
            }

        } else if (viewType == VIEW_TYPE_RIGHT_CHAT_ITEM) {
            RightChatItemViewHolder viewHolder = (RightChatItemViewHolder) holder;
            viewHolder.tvContent.setText(item.getContent());
            if (avatarMap.containsKey(item.getFromId())) {
                String targetAvatarUrl = avatarMap.get(item.getFromId());
                Picasso.with(context)
                        .load(targetAvatarUrl)
                        .resize(avatarSize, avatarSize)
                        .transform(roundedTransformation)
                        .placeholder(R.drawable.default_user_profile)
                        .into(viewHolder.ivAvatar);
            } else {
                viewHolder.ivAvatar.setImageResource(R.drawable.default_user_profile);
            }

        } else if (viewType == VIEW_TYPE_NOTIFICATION_ITEM) {
            NotificationItemViewHolder viewHolder = (NotificationItemViewHolder) holder;
            viewHolder.tvContent.setText(item.getContent());
        } else if (viewType == VIEW_TYPE_LEFT_IMAGE_ITEM) {
            LeftImageChatItemViewHolder viewHolder = (LeftImageChatItemViewHolder) holder;
            if (avatarMap.containsKey(item.getFromId())) {
                String targetAvatarUrl = avatarMap.get(item.getFromId());
                Picasso.with(context)
                        .load(targetAvatarUrl)
                        .resize(avatarSize, avatarSize)
                        .transform(roundedTransformation)
                        .placeholder(R.drawable.default_user_profile)
                        .into(viewHolder.ivAvatar);
            } else {
                viewHolder.ivAvatar.setImageResource(R.drawable.default_user_profile);
            }
            Picasso.with(context)
                    .load(item.getContent())
                    .resize(400, 400)
                    .placeholder(R.drawable.bg_default_gallery)
                    .into(viewHolder.tvContent);
        } else if (viewType == VIEW_TYPE_RIGHT_IMAGE_ITEM) {
            RightImageChatItemViewHolder viewHolder = (RightImageChatItemViewHolder) holder;
            if (avatarMap.containsKey(item.getFromId())) {
                String targetAvatarUrl = avatarMap.get(item.getFromId());
                Picasso.with(context)
                        .load(targetAvatarUrl)
                        .resize(avatarSize, avatarSize)
                        .transform(roundedTransformation)
                        .placeholder(R.drawable.default_user_profile)
                        .into(viewHolder.ivAvatar);
            } else {
                viewHolder.ivAvatar.setImageResource(R.drawable.default_user_profile);
            }
            Picasso.with(context)
                    .load(item.getContent())
                    .placeholder(R.drawable.bg_default_gallery)
                    .resize(400, 400)
                    .into(viewHolder.tvContent);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }


    public void setDataSet(List<ChatItem> dataSet) {
        this.dataSet = dataSet;
    }

    public List<ChatItem> getDataSet() {
        return dataSet;
    }

    public void setChatItemListener(ChatItemListener chatItemListener) {
        this.chatItemListener = chatItemListener;
    }

    public void setAvatarMap(Map<String, String> avatarMap) {
        this.avatarMap = avatarMap;
    }

    public static class LeftChatItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvContent)
        public TextView tvContent;

        public LeftChatItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class LeftImageChatItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvContent)
        public ImageView tvContent;

        public LeftImageChatItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class RightChatItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvContent)
        public TextView tvContent;

        public RightChatItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class RightImageChatItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvContent)
        public ImageView tvContent;

        public RightImageChatItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class NotificationItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tvContent)
        public TextView tvContent;

        public NotificationItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


    public interface ChatItemListener {
        public void onFirstLoadComplete();
    }
}
