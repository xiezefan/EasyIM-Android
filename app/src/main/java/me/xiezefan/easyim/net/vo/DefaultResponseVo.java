package me.xiezefan.easyim.net.vo;

/**
 * Default Response Vo
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class DefaultResponseVo {
    public int code;
    public String message;

    public DefaultResponseVo() {
    }

    public DefaultResponseVo(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
