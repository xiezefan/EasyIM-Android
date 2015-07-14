package me.xiezefan.easyim.net.vo;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class MessageVo {
    public String id;
    public String type;
    public Map<String, Object> content;
    public Long create_time = -1L;

    public boolean validate() {
        return !TextUtils.isEmpty(id)
                && !TextUtils.isEmpty(type)
                && content != null
                && create_time != -1L;
    }
}
