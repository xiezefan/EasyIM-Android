package me.xiezefan.easyim.mvp.contact;

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
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.dao.Friend;
import me.xiezefan.easyim.mvp.activity.BaseActivity;
import me.xiezefan.easyim.mvp.friend_home.FriendHomeActivity;

/**
 * Created by XieZeFan on 2015/3/22 0022.
 */
public class ContactFragment extends Fragment implements ContactView, ContactAdapter.FriendItemListener {
    @InjectView(R.id.rvContact)
    RecyclerView rvContact;
    @Inject
    ContactPresenter contactPresenter;


    private ContactAdapter contactAdapter;
    private LinearLayoutManager contactLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initData() {
        ((Application) getActivity().getApplication()).getApplicationGraph().inject(this);
        contactPresenter.setContactView(this);
        contactPresenter.initFriendList();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        // init toolbar
        BaseActivity _activity = (BaseActivity) getActivity();
        _activity.getToolbar().setTitle("通讯录");

        // init contact list
        contactLayoutManager = new LinearLayoutManager(getActivity());
        contactAdapter = new ContactAdapter(getActivity());
        contactAdapter.setFriendItemListener(this);
        rvContact.setLayoutManager(contactLayoutManager);
        rvContact.setAdapter(contactAdapter);
        rvContact.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = contactLayoutManager.findLastVisibleItemPosition();
                contactPresenter.onFriendSetLastItemChange(position);
            }
        });

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
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    contactPresenter.searchFriend(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    contactPresenter.searchFriend(newText);
                    return false;
                }
            });
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void startFriendHomeActivity(Friend friend) {
        Intent intent = new Intent(getActivity(), FriendHomeActivity.class);
        intent.putExtra(FriendHomeActivity.KEY_FRIEND_ID, friend.getUid());
        getActivity().startActivity(intent);
    }

    @Override
    public void notifyContactListChange(List<Friend> dataSet, int start, int row) {
        boolean isDataSetChange = contactAdapter.getDataSet() != dataSet;
        if (isDataSetChange && contactAdapter.getDataSet()!= null) {
            contactAdapter.notifyItemRangeRemoved(0, contactAdapter.getDataSet().size());
        }
        contactAdapter.updateDataSet(dataSet);
        if (row == 0) {
            contactAdapter.notifyItemChanged(start);
        } else if (isDataSetChange) {
            contactAdapter.notifyDataSetChanged();
        } else {
            contactAdapter.notifyItemRangeChanged(start, row);
        }
    }

    @Override
    public List<Friend> getCurrentContactList() {
        return contactAdapter.getDataSet();
    }

    @Override
    public void onItemClick(int position) {
        contactPresenter.onContactItemClick(position);
    }
}
