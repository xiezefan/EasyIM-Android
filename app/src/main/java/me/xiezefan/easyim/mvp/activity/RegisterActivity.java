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
import me.xiezefan.easyim.R;
import me.xiezefan.easyim.net.RequestManager;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.net.from.RegisterForm;
import me.xiezefan.easyim.net.vo.DefaultResponseVo;
import me.xiezefan.easyim.net.vo.RequestFailError;
import me.xiezefan.easyim.util.JsonUtil;
import me.xiezefan.easyim.util.StringUtil;
import rx.android.app.AppObservable;

/**
 * Created by XieZeFan on 2015/3/23 0023.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.etUsername)
    EditText etUsername;
    @InjectView(R.id.etPassword)
    EditText etPassword;


    private UserResource userResource;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initData();
    }

    private void initData() {
        userResource = RequestManager.getInstance().getUserResource();
    }

    @Override
    public void beforeInitToolbar() {
        toolbar.setTitle("注册");
    }

    @Override
    public void afterInitToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @OnClick(R.id.btnRegister)
    public void register(View view) {
        Logger.d("In Register");
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username) || username.trim().length() < 6) {
            Toast.makeText(this, "用户名不合规范", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password) || password.trim().length() < 6) {
            Toast.makeText(this, "密码不合规范", Toast.LENGTH_SHORT).show();
            return;
        }
        password = StringUtil.toMD5(password);
        MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .title("网络请求中")
                .content("正在注册")
                .progress(true, 0).show();

        RegisterForm dataForm = new RegisterForm(username, password);
        AppObservable.bindActivity(this, userResource.register(dataForm))
                .subscribe(defaultResponseVo -> {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra(LoginActivity.KEY_USERNAME, username);
                    setResult(RESULT_OK, data);
                    progressDialog.dismiss();
                    RegisterActivity.this.finish();
                }, error -> {
                    RequestFailError _error = (RequestFailError) error;
                    DefaultResponseVo response = _error.getResponse();
                    if (response.code == 1004) {
                        Toast.makeText(RegisterActivity.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "服务器开了小差, 请稍后再试", Toast.LENGTH_SHORT).show();
                        Logger.e("Response:" + JsonUtil.toJson(response));
                    }
                    progressDialog.dismiss();
                });

    }
}
