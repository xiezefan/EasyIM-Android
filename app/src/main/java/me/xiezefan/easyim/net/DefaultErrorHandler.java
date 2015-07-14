package me.xiezefan.easyim.net;

import me.xiezefan.easyim.net.vo.RequestFailError;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Process DefaultResponseVo ErrorHandler
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class DefaultErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError cause) {
        return new RequestFailError(cause);
    }
}
