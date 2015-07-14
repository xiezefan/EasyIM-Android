package me.xiezefan.easyim.mvp.common;

import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;

import javax.inject.Inject;

import me.xiezefan.easyim.mvp.base.SimpleRequestListener;
import me.xiezefan.easyim.net.UploadTokenResource;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件(图片)上传业务方法
 * Created by xiezefan on 15/5/20.
 */
public class FileUploadInteractor {

    @Inject
    UploadTokenResource uploadTokenResource;

    public void upload(File data, String name, SimpleRequestListener listener) {
        uploadTokenResource.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadToken -> {
                    UploadManager uploadManager = new UploadManager();
                    uploadManager.put(data, name, uploadToken.token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            listener.requestSuccess("image_upload");
                        }
                    }, null);
                }, error -> {
                    Logger.d("Request Fail");
                    listener.requestFail("image_upload");
                });

    }

    public void upload(byte[] data, String name, SimpleRequestListener listener) {
        uploadTokenResource.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadToken -> {
                    UploadManager uploadManager = new UploadManager();
                    uploadManager.put(data, name, uploadToken.token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            listener.requestSuccess("image_upload");
                        }
                    }, null);
                }, error -> {
                    Logger.d("Request Fail");
                    listener.requestFail("image_upload");
                });
    }

    private Observable<String> getToken() {
        return null;

    }

}
