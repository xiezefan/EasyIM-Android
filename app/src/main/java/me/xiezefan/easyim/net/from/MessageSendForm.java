package me.xiezefan.easyim.net.from;

import java.util.Map;

/**
 * Message Send Form
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class MessageSendForm {
    private String to;
    private String type;
    private Map<String, Object> content;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
