package me.xiezefan.easyim.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import cn.jpush.android.api.JPushInterface;
import me.xiezefan.easyim.mvp.main.MainActivity;
import me.xiezefan.easyim.service.UserService;

/**
 * Dispatch Activity
 * Created by XieZeFan on 2015/4/11 0011.
 */
public class DispatchActivity extends Activity {
    private UserService userService = UserService.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (userService.validateLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
