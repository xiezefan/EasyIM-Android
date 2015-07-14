package me.xiezefan.easyim.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.net.AuthorizationRequestInterceptor;
import me.xiezefan.easyim.net.DefaultErrorHandler;
import me.xiezefan.easyim.net.FriendshipResource;
import me.xiezefan.easyim.net.MessageResource;
import me.xiezefan.easyim.net.UserResource;
import retrofit.RestAdapter;

/**
 * 网络请求注入模块
 * Created by xiezefan-pc on 15-4-28.
 */
@Module(library = true)
public class RequestModule {
    //private static final String EASY_IM_ENDPOINT = "http://192.168.0.3:8080/EasyIM-Server/";
    private static final String EASY_IM_ENDPOINT = "http://112.124.51.227:8080/easyim/";
    private RestAdapter easyImRestAdapter;

    public RequestModule() {
        easyImRestAdapter = new RestAdapter.Builder()
                .setEndpoint(EASY_IM_ENDPOINT)
                .setRequestInterceptor(new AuthorizationRequestInterceptor())
                .setErrorHandler(new DefaultErrorHandler())
                .build();
    }

    @Provides
    @Singleton
    public UserResource provideUserResource() {
        return easyImRestAdapter.create(UserResource.class);
    }

    @Provides
    @Singleton
    public FriendshipResource provideFriendshipResource() {
        return easyImRestAdapter.create(FriendshipResource.class);
    }

    @Provides
    @Singleton
    public MessageResource provideMessageResource() {
        return easyImRestAdapter.create(MessageResource.class);
    }

    @Provides
    @Singleton
    public FriendRequestDao provideRequestDao() {
        return easyImRestAdapter.create(FriendRequestDao.class);
    }


}
