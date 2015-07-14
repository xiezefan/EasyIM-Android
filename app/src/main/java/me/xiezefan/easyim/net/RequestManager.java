package me.xiezefan.easyim.net;

import retrofit.RestAdapter;

/**
 * Http Request Manager
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class RequestManager {
//    private static final String EASY_IM_ENDPOINT = "http://192.168.0.3:8080/EasyIM-Server/";
    private static final String EASY_IM_ENDPOINT = "http://112.124.51.227:8080/easyim/";

    private RestAdapter easyImRestAdapter;
    private UserResource userResource;
    private FriendshipResource friendshipResource;
    private MessageResource messageResource;

    /*----Getter----*/
    public UserResource getUserResource() {
        return userResource;
    }

    public FriendshipResource getFriendshipResource() {
        return friendshipResource;
    }

    public MessageResource getMessageResource() {
        return messageResource;
    }



    private static RequestManager instance;
    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
            instance.easyImRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(EASY_IM_ENDPOINT)
                    .setRequestInterceptor(new AuthorizationRequestInterceptor())
                    .setErrorHandler(new DefaultErrorHandler())
                    .build();
            instance.userResource = instance.easyImRestAdapter.create(UserResource.class);
            instance.friendshipResource = instance.easyImRestAdapter.create(FriendshipResource.class);
            instance.messageResource = instance.easyImRestAdapter.create(MessageResource.class);
        }
        return instance;
    }
    private RequestManager() {}

}
