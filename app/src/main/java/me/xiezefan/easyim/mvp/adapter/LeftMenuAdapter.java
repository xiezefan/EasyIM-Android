package me.xiezefan.easyim.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;

/**
 * Created by XieZeFan on 2015/3/17 0017.
 */
public class LeftMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int VIEW_TYPE_DIVIDER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnLeftMenuItemClickListener onLeftMenuItemClickListener;
    private String[] items = {"个人资料", "会话记录", "通讯录", "新的好友"};
    private int[] types = {VIEW_TYPE_ITEM, VIEW_TYPE_ITEM, VIEW_TYPE_ITEM, VIEW_TYPE_ITEM};
    public LeftMenuAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return types[position];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DIVIDER) {
            return new DividerViewHolder(layoutInflater.inflate(R.layout.item_left_menu_divider, parent, false));
        } else {
            return new LeftMenuViewHolder(layoutInflater.inflate(R.layout.item_left_menu, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnClickListener(this);

        if (types[i] != VIEW_TYPE_DIVIDER) {
            LeftMenuViewHolder holder = (LeftMenuViewHolder) viewHolder;
            holder.content.setText(items[i]);
        }

    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    @Override
    public void onClick(View v) {
        if (onLeftMenuItemClickListener != null) {
            switch ((Integer) v.getTag()) {
                case 0:
                    onLeftMenuItemClickListener.onMeInfoItemClick(v);
                    break;
                case 1:
                    onLeftMenuItemClickListener.onChatSessionItemClick(v);
                    break;
                case 2:
                    onLeftMenuItemClickListener.onContactItemClick(v);
                    break;
                case 3:
                    onLeftMenuItemClickListener.onNewFriendItemClick(v);
                    break;
                case 5:
                    onLeftMenuItemClickListener.onSettingItemClick(v);
                default:
                    // ignore
            }
        }
    }

    public void setOnLeftMenuItemClickListener(OnLeftMenuItemClickListener onLeftMenuItemClickListener) {
        this.onLeftMenuItemClickListener = onLeftMenuItemClickListener;
    }

    public static class LeftMenuViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.content)
        public TextView content;
        @InjectView(R.id.profile)
        public ImageView profile;
        public LeftMenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnLeftMenuItemClickListener {
        public void onMeInfoItemClick(View view);
        public void onChatSessionItemClick(View view);
        public void onContactItemClick(View view);
        public void onNewFriendItemClick(View view);
        public void onSettingItemClick(View view);
    }



}
