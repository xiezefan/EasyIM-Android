package me.xiezefan.easyim.event.model;

import android.app.Activity;

/**
 * 返回按钮按下 事件
 * Created by xiezefan-pc on 15-4-28.
 */
public class BackPressedEvent {
    public Activity sourceActivity;

    public BackPressedEvent(Activity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }

    public boolean inSameActivity(Activity activity) {
        return sourceActivity == activity;
    }
}
