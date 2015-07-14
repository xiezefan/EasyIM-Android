package me.xiezefan.easyim.net;


import java.util.List;

import me.xiezefan.easyim.net.from.LoginForm;
import me.xiezefan.easyim.net.from.RegisterForm;
import me.xiezefan.easyim.net.from.UserUpdateForm;
import me.xiezefan.easyim.net.vo.DefaultResponseVo;
import me.xiezefan.easyim.net.vo.UserVo;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * User Resource
 * Created by XieZeFan on 2015/4/12 0012.
 */
public interface UserResource {

    @POST("/users/register")
    public Observable<DefaultResponseVo> register(@Body RegisterForm dataForm);

    @POST("/users/login")
    public Observable<UserVo> login(@Body LoginForm dataForm);

    @GET("/users/search")
    public Observable<List<UserVo>> search(@Query("q")String searchText);

    @GET("/users")
    public Observable<List<UserVo>> list(@Query("start")int start, @Query("row")int row);

    @GET("/users/{user_id}")
    public Observable<UserVo> getUserInfo(@Path("user_id")String userId);

    @PUT("/users/self")
    public Observable<DefaultResponseVo> update(@Body UserUpdateForm dataForm);
}
