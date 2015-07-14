package me.xiezefan.easyim.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.net.vo.UserVo;
import me.xiezefan.easyim.mvp.common.RoundedTransformation;
import me.xiezefan.easyim.util.DisplayUtil;

/**
 * Search Result Adapter
 * Created by XieZeFan on 2015/4/21 0021.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_SEARCH_RESULT = 2;
    private Context context;
    private LayoutInflater layoutInflater;

    private List<UserVo> dataSet;
    private int avatarSize;
    private RoundedTransformation roundedTransformation;
    private SearchResultState currentState = SearchResultState.LOADING;
    private Animation loadingAnimation;

    enum SearchResultState {
        LOADING, NO_RESULT, SUCCESS;
    }

    public SearchResultAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        this.roundedTransformation = new RoundedTransformation();
        this.avatarSize = DisplayUtil.dip2px(context, 48);
        this.loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.default_rotating);
    }

    @Override
    public int getItemViewType(int position) {
        switch (currentState) {
            case LOADING:
                return VIEW_TYPE_LOADING;
            case NO_RESULT:
                return VIEW_TYPE_TEXT;
            case SUCCESS:
                return VIEW_TYPE_SEARCH_RESULT;
            default:
                return VIEW_TYPE_SEARCH_RESULT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(layoutInflater.inflate(R.layout.item_loading, parent, false));
        } else if (viewType == VIEW_TYPE_TEXT) {
            return new TextViewHolder(layoutInflater.inflate(R.layout.item_text, parent, false));
        } else if (viewType == VIEW_TYPE_SEARCH_RESULT) {
            return new SearchResultViewHolder(layoutInflater.inflate(R.layout.item_friend_search_result, parent, false));
        } else {
            return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (viewType == VIEW_TYPE_LOADING) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.ivLoading.startAnimation(loadingAnimation);
        } else if (viewType == VIEW_TYPE_TEXT) {
            TextViewHolder viewHolder = (TextViewHolder) holder;
            viewHolder.tvHintText.setText("查找不到此人");
        } else if (viewType == VIEW_TYPE_SEARCH_RESULT) {
            UserVo item = dataSet.get(position);
            SearchResultViewHolder viewHolder = (SearchResultViewHolder) holder;
            viewHolder.tvUsername.setText(getDisplayName(item));
            if (!TextUtils.isEmpty(item.avatar)) {
                Picasso.with(context)
                        .load(item.avatar)
                        .resize(avatarSize, avatarSize)
                        .placeholder(context.getResources().getDrawable(R.drawable.default_user_profile))
                        .transform(roundedTransformation)
                        .into(viewHolder.ivAvatar);
            }
        }


    }

    public String getDisplayName(UserVo item) {
        return TextUtils.isEmpty(item.nickname) ? item.username : item.nickname;
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 1 : (dataSet.size() == 0 ? 1 : dataSet.size());
    }

    public void setDataSet(List<UserVo> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivAvatar)
        public ImageView ivAvatar;
        @InjectView(R.id.tvUsername)
        public TextView tvUsername;
        @InjectView(R.id.tvUntreated)
        public TextView tvUntreated;
        public SearchResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivLoading)
        public ImageView ivLoading;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tvHintText)
        public TextView tvHintText;
        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
