package me.xiezefan.easyim.mvp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.InjectView;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.mvp.adapter.SearchResultAdapter;

/**
 * Search Result Activity
 * Created by XieZeFan on 2015/4/22 0022.
 */
public class SearchResultActivity extends BaseActivity {
    public static final String KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT";

    @InjectView(R.id.rvSearchResult)
    RecyclerView rvSearchResult;

    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_search_result);
        initSearchResult();

    }

    private void initData() {
        searchText = getIntent().getStringExtra(KEY_SEARCH_TEXT);
    }

    @Override
    public void beforeInitToolbar() {
        toolbar.setTitle("搜索 " + searchText);
    }

    @Override
    public void afterInitToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
    }

    private void initSearchResult() {
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResult.setAdapter(new SearchResultAdapter(this));
    }
}
