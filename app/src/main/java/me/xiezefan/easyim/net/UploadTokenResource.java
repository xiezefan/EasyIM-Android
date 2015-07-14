package me.xiezefan.easyim.net;

import me.xiezefan.easyim.net.vo.UploadTokenVo;
import retrofit.http.GET;
import rx.Observable;

/**
 * 获取七牛云上传token
 * Created by xiezefan on 15/5/20.
 */
public interface UploadTokenResource {
    @GET("/tokens/generate")
    public Observable<UploadTokenVo> getToken();

}