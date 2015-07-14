package me.xiezefan.easyim.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.common.SPHelper;
import me.xiezefan.easyim.mvp.main.MainActivity;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.net.from.LoginForm;
import me.xiezefan.easyim.net.vo.DefaultResponseVo;
import me.xiezefan.easyim.net.vo.RequestFailError;
import me.xiezefan.easyim.service.UserService;
import me.xiezefan.easyim.util.StringUtil;
import rx.android.app.AppObservable;

/**
 * LoginActivity
 * Created by XieZeFan on 2015/3/23 0023.
 */
public class LoginActivity extends BaseActivity {
    public static int RESULT_CODE_USERNAME = 1101;
    public static String KEY_USERNAME = "KEY_USERNAME";

    @InjectView(R.id.etUsername)
    EditText etUsername;
    @InjectView(R.id.etPassword)
    EditText etPassword;

    private UserResource userResource;
    private UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_login);
    }

    private void initData() {
        userResource = RequestManager.getInstance().getUserResource();
        userService = UserService.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE_USERNAME && resultCode == RESULT_OK) {
            String username = data.getStringExtra(KEY_USERNAME);
            etUsername.setText(username);
            etPassword.requestFocus();
        }
    }

    @Override
    public void beforeInitToolbar() {
        toolbar.setTitle("登录");
    }


    @OnClick(R.id.btnLogin)
    public void login(View view) {
        final String username = etUsername.getText().toString();
        String _password = etPassword.getText().toString();
        String deviceId = SPHelper.getString(SPHelper.REGISTER_ID);

        if (TextUtils.isEmpty(username) || username.trim().length() < 6) {
            Toast.makeText(this, "用户名不合规范", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(_password) || _password.trim().length() < 6) {
            Toast.makeText(this, "密码不合规范", Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = StringUtil.toMD5(_password);
        MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .title("网络请求中")
                .content("正在登录")
                .progress(true, 0).show();
        LoginForm dataForm = new LoginForm(deviceId, username, password);
        AppObservable.bindActivity(this, userResource.login(dataForm))
                .subscribe(userVo -> {
                    Logger.d("Login Success");
                    startActivity(new Intent(this, MainActivity.class));
                    userService.registerJPush(LoginActivity.this, userVo.id);
                    userService.persistenceUser(userVo, password);
                    progressDialog.dismiss();
                    finish();
                }, error -> {
                    Logger.d("Login Success");
                    RequestFailError _error = (RequestFailError)error;
                    DefaultResponseVo response = _error.getResponse();
                    if (response.code == 1001) {
                        Toast.makeText(LoginActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "未知错误:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }

    @OnClick(R.id.btnRegister)
    public void toRegister(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), RESULT_CODE_USERNAME);
    }


}
