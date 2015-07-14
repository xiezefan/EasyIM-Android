package me.xiezefan.easyim.net;

import java.util.List;

import me.xiezefan.easyim.net.from.MessageSendForm;
import me.xiezefan.easyim.net.from.MessageStatusUpdateForm;
import me.xiezefan.easyim.net.vo.DefaultResponseVo;
import me.xiezefan.easyim.net.vo.MessageVo;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import rx.Observable;

/**
 * Message Resource
 * Created by XieZeFan on 2015/4/12 0012.
 */
public interface MessageResource {

    @POST("/messages/send")
    public Observable<MessageVo> send(@Body MessageSendForm dataForm);

    @GET("/messages/offline")
    public Observable<List<MessageVo>> getOffline();

    @PUT("/messages")
    public Observable<DefaultResponseVo> updateStatusBatch(@Body List<MessageStatusUpdateForm> dataForms);

    @PUT("/messages/{id}")
    public Observable<DefaultResponseVo> updateStatus(@Part("id") String id, @Body MessageStatusUpdateForm dataForm);
}
