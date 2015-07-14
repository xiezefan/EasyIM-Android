package me.xiezefan.easyim.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.net.vo.UserVo;
import me.xiezefan.easyim.service.UserService;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;

/**
 * Friend Square Adapter
 * Created by XieZeFan on 2015/4/25 0025.
 */
public class FriendSquareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<UserVo> dataSet;
    private int avatarSize;
    private RoundedTransformation roundedTransformation;
    private UserService userService = UserService.getInstance();

    public FriendSquareAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        this.roundedTransformation = new RoundedTransformation();
        this.avatarSize = DisplayUtil.dip2px(context, 48);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendViewHolder(layoutInflater.inflate(R.layout.item_friend_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserVo item = dataSet.get(position);
        FriendViewHolder viewHolder = (FriendViewHolder) holder;

        viewHolder.tvUsername.setText(getDisplayName(item));
        if (!TextUtils.isEmpty(item.avatar)) {
            Picasso.with(context)
                    .load(item.avatar)
                    .resize(avatarSize, avatarSize)
                    .placeholder(context.getResources().getDrawable(R.drawable.default_user_profile))
                    .transform(roundedTransformation)
                    .into(viewHolder.ivAvatar);
        }
        viewHolder.tvUntreated.setOnClickListener(this);
        viewHolder.tvUntreated.setTag(position);
    }

    public String getDisplayName(UserVo item) {
        return TextUtils.isEmpty(item.nickname) ? item.username : item.nickname;
    }


    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void setDataSet(List<UserVo> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        UserVo item = dataSet.get(position);
        String friendName = getDisplayName(item);
        String myName = SPHelper.getString(SPHelper.USERNAME);


        View dialog = layoutInflater.inflate(R.layout.dialog_friend_request, (ViewGroup) v.getRootView(), false);
        InputDialogViewHolder viewHolder = new InputDialogViewHolder(dialog);
        viewHolder.position = position;
        viewHolder.etRequestMsg.setText("我是" + myName);
        int len = viewHolder.etRequestMsg.getText().toString().length();
        viewHolder.etRequestMsg.setSelection(len);
        new MaterialDialog.Builder(context)
                .title("添加" + friendName + "为好友")
                .customView(dialog, false)
                .positiveText("确定")
                .showListener(showView -> {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(viewHolder.etRequestMsg, InputMethodManager.SHOW_FORCED);
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        UserVo item = dataSet.get(viewHolder.position);
                        userService.requestFriend(item.id, viewHolder.etRequestMsg.getText().toString());

                    }
                })

                .show();
    }

    public static class InputDialogViewHolder {
        @InjectView(R.id.etRequestMsg)
        public EditText etRequestMsg;

        private int position;

        public InputDialogViewHolder(View v) {
            ButterKnife.inject(this, v);
        }


    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvUsername)
        public TextView tvUsername;
        @InjectView(R.id.tvUntreated)
        public TextView tvUntreated;
        public FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
