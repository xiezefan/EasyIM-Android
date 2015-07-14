package me.xiezefan.easyim.mvp.base;

/**
 * 通用的请求回调函数
 * Created by xiezefan on 15/5/18.
 */
public interface SimpleRequestListener {
    public void requestSuccess(String tag);
    public void requestFail(String tag);
}
