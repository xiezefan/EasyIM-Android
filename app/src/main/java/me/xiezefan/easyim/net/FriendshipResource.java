package me.xiezefan.easyim.net;


import java.util.List;

import me.xiezefan.easyim.net.from.FriendshipAddForm;
import me.xiezefan.easyim.net.vo.DefaultResponseVo;
import me.xiezefan.easyim.net.vo.UserVo;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;


/**
 * FriendShip Resource
 * Created by XieZeFan on 2015/4/12 0012.
 */
public interface FriendshipResource {

    @POST("/friends")
    public Observable<DefaultResponseVo> save(@Body FriendshipAddForm dataForm);

    @DELETE("/friends/{id}")
    public Observable<DefaultResponseVo> delete(@Path("id") String id);

    @GET("/friends")
    public Observable<List<UserVo>> list();

}
