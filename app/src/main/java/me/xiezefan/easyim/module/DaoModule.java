package me.xiezefan.easyim.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.xiezefan.easyim.dao.DaoMaster;
import me.xiezefan.easyim.dao.DaoSession;
import me.xiezefan.easyim.dao.FriendDao;
import me.xiezefan.easyim.dao.FriendRequestDao;

/**
 * GreenDao注入模块
 * Created by xiezefan-pc on 15-4-28.
 */
@Module(
        library = true)
public class DaoModule {
    private static final String DATABASE_NAME = "EasyImDB";
    private DaoSession daoSession;

    public DaoModule(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    @Provides
    @Singleton
    FriendDao provideFriend() {return daoSession.getFriendDao();}

    @Provides
    @Singleton
    FriendRequestDao provideFriendRequest() {return daoSession.getFriendRequestDao();}


}
