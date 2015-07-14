package me.xiezefan.easyim.mvp.user_info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;


public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int VIEW_TYPE_AVATAR = 0;
    private static final int VIEW_TYPE_USERNAME = 1;
    private static final int VIEW_TYPE_NICKNAME = 2;
    private static final int VIEW_TYPE_DESCRIPTION = 3;
    private static final int VIEW_TYPE_SEX = 4;
    private static final int VIEW_TYPE_LOGIN_OUT = 5;
    private static final int VIEW_TYPE_HEADER = 6;
    private static final int VIEW_TYPE_LOCATION = 7;
    private static final int VIEW_TYPE_DIVIDER = 8;

    private static final int[] VIEW_TYPE_ARRAY = {VIEW_TYPE_HEADER,  VIEW_TYPE_AVATAR, VIEW_TYPE_HEADER, VIEW_TYPE_USERNAME, VIEW_TYPE_DIVIDER, VIEW_TYPE_NICKNAME, VIEW_TYPE_HEADER, VIEW_TYPE_SEX, VIEW_TYPE_DIVIDER, VIEW_TYPE_LOCATION, VIEW_TYPE_DIVIDER, VIEW_TYPE_DESCRIPTION, VIEW_TYPE_HEADER, VIEW_TYPE_LOGIN_OUT};

    private Context context;
    private LayoutInflater layoutInflater;
    private UserInfoListener userInfoListener;

    private Map<String, String> dataSet;
    private int avatarSize;
    private RoundedTransformation roundedTransformation;

    public UserInfoAdapter(Context context) {
        this.context = context;
        initData();
    }

    private void initData() {
        this.layoutInflater = LayoutInflater.from(context);
        this.avatarSize = DisplayUtil.dip2px(context, 48);
        this.roundedTransformation = new RoundedTransformation();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ARRAY[position];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(layoutInflater.inflate(R.layout.item_user_info_header, parent, false));
            case VIEW_TYPE_AVATAR:
                return new ImageViewHolder(layoutInflater.inflate(R.layout.item_user_info_image, parent, false));
            case VIEW_TYPE_USERNAME:
            case VIEW_TYPE_NICKNAME:
            case VIEW_TYPE_DESCRIPTION:
            case VIEW_TYPE_SEX:
            case VIEW_TYPE_LOCATION:
                return new TextViewHolder(layoutInflater.inflate(R.layout.item_user_info_text, parent, false));
            case VIEW_TYPE_LOGIN_OUT:
                return new ButtonViewHolder(layoutInflater.inflate(R.layout.item_user_info_button, parent, false));
            case VIEW_TYPE_DIVIDER:
                return new DividerViewHolder(layoutInflater.inflate(R.layout.item_user_info_divider, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (dataSet == null) {
            return;
        }
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_AVATAR:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.tvLabel.setText("头像");
                String imageUrl = dataSet.get("avatar");
                if (!TextUtils.isEmpty(imageUrl)) {
                    Picasso.with(context)
                            .load(dataSet.get("avatar"))
                            .resize(avatarSize, avatarSize)
                            .placeholder(R.drawable.default_user_profile)
                            .transform(roundedTransformation)
                            .into(imageViewHolder.ivImage);
                }
                imageViewHolder.llWrapper.setTag(viewType);
                imageViewHolder.llWrapper.setOnClickListener(this);
                break;
            case VIEW_TYPE_USERNAME:
                TextViewHolder usernameViewHolder = (TextViewHolder) holder;
                usernameViewHolder.tvLabel.setText("登录名");
                usernameViewHolder.tvContent.setText(dataSet.get("username"));
                usernameViewHolder.llWrapper.setTag(viewType);
                usernameViewHolder.llWrapper.setOnClickListener(this);
                break;
            case VIEW_TYPE_NICKNAME:
                TextViewHolder nicknameViewHolder = (TextViewHolder) holder;
                nicknameViewHolder.tvLabel.setText("昵称");
                nicknameViewHolder.tvContent.setText(dataSet.get("nickname"));
                nicknameViewHolder.llWrapper.setTag(viewType);
                nicknameViewHolder.llWrapper.setOnClickListener(this);
                break;
            case VIEW_TYPE_DESCRIPTION:
                TextViewHolder descriptionViewHolder = (TextViewHolder) holder;
                descriptionViewHolder.tvLabel.setText("个性签名");
                descriptionViewHolder.tvContent.setText(dataSet.get("description"));
                descriptionViewHolder.llWrapper.setTag(viewType);
                descriptionViewHolder.llWrapper.setOnClickListener(this);
                break;
            case VIEW_TYPE_SEX:
                TextViewHolder sexViewHolder = (TextViewHolder) holder;
                sexViewHolder.tvLabel.setText("性别");
                sexViewHolder.tvContent.setText(dataSet.get("sex"));
                sexViewHolder.llWrapper.setTag(viewType);
                sexViewHolder.llWrapper.setOnClickListener(this);
                break;
            case VIEW_TYPE_LOCATION:
                TextViewHolder locationViewHolder = (TextViewHolder) holder;
                locationViewHolder.tvLabel.setText("所在地");
                locationViewHolder.tvContent.setText(dataSet.get("location"));
                locationViewHolder.llWrapper.setTag(viewType);
                locationViewHolder.llWrapper.setOnClickListener(this);
                break;
            case VIEW_TYPE_LOGIN_OUT:
                ButtonViewHolder loginOutViewHolder = (ButtonViewHolder) holder;
                loginOutViewHolder.btnLoginOut.setTag(viewType);
                loginOutViewHolder.btnLoginOut.setOnClickListener(this);
                break;
            default:
                // can't not be happen
        }
    }

    @Override
    public int getItemCount() {
        return VIEW_TYPE_ARRAY.length;
    }

    @Override
    public void onClick(View v) {
        if (userInfoListener != null) {
            int viewType = (int) v.getTag();
            switch (viewType) {
                case VIEW_TYPE_AVATAR:
                    userInfoListener.onAvatarItemClick();
                    break;
                case VIEW_TYPE_NICKNAME:
                    userInfoListener.onNicknameItemClick();
                    break;
                case VIEW_TYPE_DESCRIPTION:
                    userInfoListener.onDescriptionItemClick();
                    break;
                case VIEW_TYPE_SEX:
                    userInfoListener.onSexItemClick();
                    break;
                case VIEW_TYPE_LOCATION:
                    userInfoListener.onLocationItemClick();
                    break;
                case VIEW_TYPE_LOGIN_OUT:
                    userInfoListener.onLoginOutBtnClick();
                    break;
                default:
                    // can't not be happen
            }
        }
    }

    public void setDataSet(Map<String, String> dataSet) {
        this.dataSet = dataSet;
    }

    public void setUserInfoListener(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class DividerViewHolder extends RecyclerView.ViewHolder {

        public DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.llWrapper)
        public RelativeLayout llWrapper;
        @InjectView(R.id.ivImage)
        public ImageView ivImage;
        @InjectView(R.id.tvLabel)
        public TextView tvLabel;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.llWrapper)
        public RelativeLayout llWrapper;
        @InjectView(R.id.tvLabel)
        public TextView tvLabel;
        @InjectView(R.id.tvContent)
        public TextView tvContent;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.btnLoginOut)
        public Button btnLoginOut;
        public ButtonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface UserInfoListener {
        public void onAvatarItemClick();
        public void onNicknameItemClick();
        public void onDescriptionItemClick();
        public void onSexItemClick();
        public void onLocationItemClick();
        public void onLoginOutBtnClick();
    }

}
