package me.xiezefan.easyim.mvp.chat_session;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.readystatesoftware.viewbadger.BadgeView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.ChatSession;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;
import me.xiezefan.easyim.util.JsonUtil;

/**
 * Created by xiezefan-pc on 15-3-17.
 */
public class ChatSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private LayoutInflater layoutInflater;
    private ChatSessionItemClickListener chatSessionItemClickListener;

    private List<ChatSession> dataSet;
    private int avatarSize;
    private RoundedTransformation roundedTransformation;

    public ChatSessionAdapter(Context context) {
        this.context = context;
        initData();
    }

    private void initData() {
        this.layoutInflater = LayoutInflater.from(context);
        this.avatarSize = DisplayUtil.dip2px(context, 48);
        this.roundedTransformation = new RoundedTransformation();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new ChatSessionViewHolder(layoutInflater.inflate(R.layout.item_chat_session, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChatSession item = dataSet.get(i);
        Logger.d("ChatSession:" + JsonUtil.toJson(item));
        ChatSessionViewHolder holder = (ChatSessionViewHolder) viewHolder;
        holder.llWrapper.setTag(i);
        holder.llWrapper.setOnClickListener(this);
        holder.tvTitle.setText(item.getTitle());
        holder.tvLastMessage.setText(item.getLastMessage());
        if (TextUtils.isEmpty(item.getAvatar())) {

            Picasso.with(context)
                    .load(item.getAvatar())
                    .resize(avatarSize, avatarSize)
                    .placeholder(R.drawable.default_user_profile)
                    .transform(roundedTransformation)
                    .into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.default_user_profile);
        }
        if (item.getUnread() > 0) {
            holder.showBadge(context, item.getUnread());
        } else {
            holder.hideBadge();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void setDataSet(List<ChatSession> dataSet) {
        if (this.dataSet != dataSet) {
            this.dataSet = dataSet;
        }
    }

    @Override
    public void onClick(View v) {
        if (chatSessionItemClickListener != null) {
            chatSessionItemClickListener.onChatSessionItemClick((Integer) v.getTag());
        }
    }

    public void setChatSessionItemClickListener(ChatSessionItemClickListener chatSessionItemClickListener) {
        this.chatSessionItemClickListener = chatSessionItemClickListener;
    }



    public static class ChatSessionViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.llWrapper)
        public LinearLayout llWrapper;
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvTitle)
        public TextView tvTitle;
        @InjectView(R.id.tvLastMessage)
        public TextView tvLastMessage;

        private BadgeView badgeView;

        public ChatSessionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void showBadge(Context context, int badge) {
            if (badgeView == null) {
                badgeView = new BadgeView(context, tvTitle);
            }
            badgeView.setText("" + badge);
            badgeView.setBadgeMargin(10);
            if (!badgeView.isShown()) {
                badgeView.show();
            }
        }
        public void hideBadge() {
            if (badgeView != null) {
                badgeView.hide();
            }
        }
    }

    public interface ChatSessionItemClickListener {
        public void onChatSessionItemClick(int position);
    }


}
