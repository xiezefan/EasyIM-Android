package me.xiezefan.easyim.mvp.main;

import javax.inject.Inject;

public class MainPresenter {
    @Inject
    MainInteractor mainInteractor;

    private MainView mainView;

    public MainPresenter() {
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void syncData() {
        mainInteractor.synchronizationContact();
    }

}
