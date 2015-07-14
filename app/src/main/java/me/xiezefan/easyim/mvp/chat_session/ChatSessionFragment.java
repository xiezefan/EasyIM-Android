package me.xiezefan.easyim.mvp.chat_session;


import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.ChatItem;
import me.xiezefan.easyim.dao.ChatSession;
import me.xiezefan.easyim.mvp.activity.BaseActivity;
import me.xiezefan.easyim.mvp.chat.ChatActivity;

/**
 * Created by XieZeFan on 2015/3/22 0022.
 */
public class ChatSessionFragment extends Fragment implements ChatSessionView, ChatSessionAdapter.ChatSessionItemClickListener{

    @InjectView(R.id.rvChatSession)
    RecyclerView rvChatSession;

    @Inject
    ChatSessionPresenter chatSessionPresenter;

    private ChatSessionAdapter chatSessionAdapter;
    private LinearLayoutManager chatSessionLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_session, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        ((Application) getActivity().getApplication()).getApplicationGraph().inject(this);
        chatSessionPresenter.setChatSessionView(this);
        chatSessionPresenter.initChatSessionList();

        EventBus.getDefault().register(this, 5);

    }

    private void initView() {
        // init toolbar
        BaseActivity _activity = (BaseActivity) getActivity();
        _activity.getToolbar().setTitle("EasyIM");

        // init recycler list
        chatSessionLayoutManager = new LinearLayoutManager(getActivity());
        chatSessionAdapter = new ChatSessionAdapter(getActivity());
        chatSessionAdapter.setChatSessionItemClickListener(this);
        rvChatSession.setLayoutManager(chatSessionLayoutManager);
        rvChatSession.setAdapter(chatSessionAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startChatActivity(String targetId) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_TARGET_ID, targetId);
        startActivity(intent);
    }

    @Override
    public void notifyChatSessionsChange(List<ChatSession> dataSet, int start, int row) {
        // row = 0 单点更新
        // row = 1 单点增加
        // row = dataSet.size() 全部更新
        chatSessionAdapter.setDataSet(dataSet);
        if (row == 0) {
            chatSessionAdapter.notifyItemChanged(start);
        } else if (row == 1) {
            chatSessionAdapter.notifyItemInserted(start);
        } else if (row == dataSet.size()) {
            chatSessionAdapter.notifyDataSetChanged();
        } else {
            chatSessionAdapter.notifyItemRangeInserted(start, row);
        }

    }

    @Override
    public void onChatSessionItemClick(int position) {
        chatSessionPresenter.onChatItemClick(position);
    }


    /*---- Evnet ----*/
    public void onEventMainThread(ChatItem chatItem) {
        chatSessionPresenter.onReceiveChatItem(chatItem);
    }
}
