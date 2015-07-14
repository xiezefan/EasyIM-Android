package me.xiezefan.easyim.net;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import me.xiezefan.easyim.common.SPHelper;
import retrofit.RequestInterceptor;

/**
 * Authorization Request Interceptor
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class AuthorizationRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", "EasyIM-V1-Android");
        String authCode = SPHelper.getString(SPHelper.AUTH_CODE);
        if (!TextUtils.isEmpty(authCode)) {
            request.addHeader("Authorization", authCode);
        }

    }
}
