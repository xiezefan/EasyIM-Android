package me.xiezefan.easyim.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.xiezefan.easyim.Application;
import me.xiezefan.easyim.common.FriendHelper;
import me.xiezefan.easyim.common.PromptHelper;
import me.xiezefan.easyim.dao.ChatItemDao;
import me.xiezefan.easyim.dao.ChatSessionDao;
import me.xiezefan.easyim.dao.DaoMaster;
import me.xiezefan.easyim.dao.DaoSession;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequestDao;
import me.xiezefan.easyim.mvp.chat.ChatActivity;
import me.xiezefan.easyim.mvp.chat_session.ChatSessionFragment;
import me.xiezefan.easyim.mvp.friend_home.FriendHomeActivity;
import me.xiezefan.easyim.mvp.gallery.GalleryActivity;
import me.xiezefan.easyim.mvp.gallery.GalleryAdapter;
import me.xiezefan.easyim.mvp.gallery.GalleryInteractor;
import me.xiezefan.easyim.mvp.main.MainActivity;
import me.xiezefan.easyim.mvp.user_info.UserInfoFragment;
import me.xiezefan.easyim.net.AuthorizationRequestInterceptor;
import me.xiezefan.easyim.net.DefaultErrorHandler;
import me.xiezefan.easyim.net.FriendshipResource;
import me.xiezefan.easyim.net.MessageResource;
import me.xiezefan.easyim.net.UploadTokenResource;
import me.xiezefan.easyim.net.UserResource;
import me.xiezefan.easyim.mvp.contact.ContactFragment;
import me.xiezefan.easyim.mvp.new_friend.NewFriendFragment;
import retrofit.RestAdapter;

/**
 * Android System Module
 * Created by XieZeFan on 2015/4/26 0026.
 */
@Module(
        injects = {
                Application.class,
                NewFriendFragment.class,
                ContactFragment.class,
                MainActivity.class,
                FriendHomeActivity.class,
                ChatActivity.class,
                ChatSessionFragment.class,
                GalleryActivity.class,
                UserInfoFragment.class
        },
        library = true)
public class AndroidModule {
    private final Application application;
    private static final String DATABASE_NAME = "EasyImDB";
    private DaoSession daoSession;
    private static final String EASY_IM_ENDPOINT = "http://112.124.51.227:8080/easyim/";
    private RestAdapter easyImRestAdapter;

    public AndroidModule(Application application) {

        this.application = application;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        easyImRestAdapter = new RestAdapter.Builder()
                .setEndpoint(EASY_IM_ENDPOINT)
                .setRequestInterceptor(new AuthorizationRequestInterceptor())

                .setErrorHandler(new DefaultErrorHandler())
                .build();
    }

    // system
    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    PromptHelper providePromptHelper() {
        return new PromptHelper();
    }

    // dao
    @Provides
    @Singleton
    FriendDao provideFriendDao() {return daoSession.getFriendDao();}

    @Provides
    @Singleton
    FriendRequestDao provideFriendRequestDao() {return daoSession.getFriendRequestDao();}

    @Provides
    @Singleton
    ChatItemDao provideChatItemDao() {
        return daoSession.getChatItemDao();
    }

    @Provides
    @Singleton
    ChatSessionDao provideChatSessionDao() {
        return daoSession.getChatSessionDao();
    }


    // request
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
    public UploadTokenResource provideUploadTokenResource() {
        return easyImRestAdapter.create(UploadTokenResource.class);
    }


    // helper
    @Provides
    @Singleton
    public FriendHelper provideFriendHelper() {
        return new FriendHelper();
    }


    // interactor
    @Provides
    @Singleton
    public GalleryInteractor provideGalleryInteractor() {
        return new GalleryInteractor();
    }

}
