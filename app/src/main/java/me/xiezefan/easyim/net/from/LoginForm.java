package me.xiezefan.easyim.net.from;

/**
 * User Login Form
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class LoginForm {
    public String device_id;
    public String username;
    public String password;

    public LoginForm(String device_id, String username, String password) {
        this.device_id = device_id;
        this.username = username;
        this.password = password;
    }

    public LoginForm() {
    }
}
