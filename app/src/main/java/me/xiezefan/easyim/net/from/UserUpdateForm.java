package me.xiezefan.easyim.net.from;

public class UserUpdateForm {
    public String nickname;
    public String avatar;
    public String sex;
    public String description;
    public String location;

    public UserUpdateForm(String key, String value) {
        if ("nickname".equals(key)) {
            this.nickname = value;
        } else if ("avatar".equals(key)) {
            this.avatar = value;
        } else if ("sex".equals(key)) {
            this.sex = value;
        } else if ("description".equals(key)) {
            this.description = value;
        } else if ("location".equals(key)) {
            this.location = value;
        }
    }
}
