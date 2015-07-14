package me.xiezefan.easyim.mvp.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiezefan.easyim.R;

/**
 * Created by xiezefan-pc on 15-3-17.
 */
public class BaseActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
        initToolBar();
    }

    private void initToolBar() {
        if (toolbar != null) {
            beforeInitToolbar();
            setSupportActionBar(toolbar);
            afterInitToolbar();
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void afterInitToolbar() {

    }

    public void beforeInitToolbar() {

    }
}
