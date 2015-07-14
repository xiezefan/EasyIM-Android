package me.xiezefan.easyim.mvp.contact;

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
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;

/**
 * Created by XieZeFan on 2015/3/21 0021.
 */
public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private LayoutInflater layoutInflater;

    private List<Friend> dataSet;
    private FriendItemListener friendItemListener;
    private int avatarSize;
    private RoundedTransformation roundedTransformation;


    public ContactAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        this.avatarSize = DisplayUtil.dip2px(context, 48);
        this.roundedTransformation = new RoundedTransformation();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new ContactViewHolder(layoutInflater.inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Friend item = dataSet.get(i);
        ContactViewHolder holder = (ContactViewHolder) viewHolder;
        holder.llWrapper.setTag(i);
        holder.llWrapper.setClickable(true);
        holder.llWrapper.setOnClickListener(this);

        holder.tvDisplayName.setText(getDisplayName(item));
        if (!TextUtils.isEmpty(item.getAvatar())) {
            Picasso.with(context)
                    .load(item.getAvatar())
                    .resize(avatarSize, avatarSize)
                    .placeholder(context.getResources().getDrawable(R.drawable.default_user_profile))
                    .transform(roundedTransformation)
                    .into(holder.ivAvatar);
        }
    }

    public String getDisplayName(Friend item) {
        return TextUtils.isEmpty(item.getNickname()) ? item.getUsername() : item.getNickname();
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void updateDataSet(List<Friend> dataSet) {
        if (this.dataSet != dataSet) {
            this.dataSet = dataSet;
        }
    }

    public List<Friend> getDataSet() {
        return dataSet;
    }

    @Override
    public void onClick(View v) {
        if (friendItemListener != null) {
            friendItemListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setFriendItemListener(FriendItemListener friendItemListener) {
        this.friendItemListener = friendItemListener;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.llWrapper)
        public LinearLayout llWrapper;
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvDisplayName)
        public TextView tvDisplayName;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface FriendItemListener {
        public void onItemClick(int position);
    }
}
