package me.xiezefan.easyim;

import android.database.sqlite.SQLiteDatabase;



import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import dagger.ObjectGraph;
import me.xiezefan.easyim.dao.DaoMaster;
import me.xiezefan.easyim.dao.DaoSession;
import me.xiezefan.easyim.event.NotificationEventManager;
import me.xiezefan.easyim.module.AndroidModule;

/**
 * EasyIM Application
 * Created by XieZeFan on 2015/4/11 0011.
 */
public class Application extends android.app.Application {
    private static final String DATABASE_NAME = "EasyImDB";
    private static Application instance;
    private DaoSession daoSession;
    private ObjectGraph applicationGraph;

    @Inject
    NotificationEventManager notificationEventManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        initJPush();
        applicationGraph = ObjectGraph.create(new AndroidModule(this));
        applicationGraph.inject(this);
        notificationEventManager.register();
    }


    private void initData() {
        instance = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }



    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Application getInstance() {
        return instance;
    }
}
