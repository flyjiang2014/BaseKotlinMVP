package com.kotlin.mvp.http.response;

/**
 * Created by flyjiang on 2019/8/2.
 * 文件说明：网络请求返回数据
 */
public class HttpResponse<T> {
    private int success;
    private String message;
    private T data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
