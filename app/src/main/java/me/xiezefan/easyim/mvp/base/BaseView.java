package me.xiezefan.easyim.mvp.base;

import android.content.Context;

/**
 * Base View Interface
 * Created by xiezefan on 15/5/17.
 */
public interface BaseView {
    public Context getContext();
    public void showToast(String text);
    public void showLoading(String text);
    public void hideLoading();
}
