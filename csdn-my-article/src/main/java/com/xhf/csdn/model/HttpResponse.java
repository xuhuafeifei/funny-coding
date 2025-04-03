package com.xhf.csdn.model;



import java.util.LinkedList;
import java.util.List;

/**
 * @author feigebuge
 * @email 2508020102@qq.com
 */
public class HttpResponse {
    private int statusCode;

    private String body;

// 引入 javafx.util.Pair 类
private List<Pair<String, String>> Header = new LinkedList<>();
    private String msg;

    public HttpResponse() {}

    public HttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader(String key, String value) {
        this.Header.add(new Pair<>(key, value));
    }

    public List<Pair<String, String>> getHeader() {
        return this.Header;
    }

    public List<String> getHeader(String key) {
        List<String> list = new LinkedList<>();
        for (Pair<String, String> pair : this.Header) {
            if (pair.first.equals(key)) {
                list.add(pair.second);
            }
        }
        return list;
    }

    public void setMsg(String reasonPhrase) {
        this.msg = reasonPhrase;
    }

    public String getMsg() {
        return this.msg;
    }
}
