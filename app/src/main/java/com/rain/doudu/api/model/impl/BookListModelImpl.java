package com.rain.doudu.api.model.impl;



import com.rain.doudu.api.ApiCompleteListener;
import com.rain.doudu.api.common.ServiceFactory;
import com.rain.doudu.api.common.service.IBookListService;
import com.rain.doudu.api.model.IBookListModel;
import com.rain.doudu.bean.http.douban.BaseResponse;
import com.rain.doudu.bean.http.douban.BookListResponse;
import com.rain.doudu.common.URL;

import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class BookListModelImpl implements IBookListModel {

    /**
     * 获取图书列表
     */
    @Override
    public void loadBookList(String q, final String tag, int start, int count, String fields, final ApiCompleteListener listener) {
        IBookListService iBookListService = ServiceFactory.createService(URL.HOST_URL_DOUBAN, IBookListService.class);
        iBookListService.getBookList(q, tag, start, count, fields)
                .subscribeOn(Schedulers.io())    //请求在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<Response<BookListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listener.onFailed(null);
                            return;
                        }
                        listener.onFailed(new BaseResponse(404, e.getMessage()));
                    }

                    @Override
                    public void onNext(Response<BookListResponse> bookListResponse) {
                        if (bookListResponse.isSuccessful()) {
                            listener.onComplected(bookListResponse.body());
                        } else {
                            listener.onFailed(new BaseResponse(bookListResponse.code(), bookListResponse.message()));
                        }

                    }
                });
    }

    @Override
    public void cancelLoading() {

    }
}
