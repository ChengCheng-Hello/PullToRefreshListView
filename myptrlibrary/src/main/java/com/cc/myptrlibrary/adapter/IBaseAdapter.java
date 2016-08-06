package com.cc.myptrlibrary.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnLoadingListener;

import java.util.List;


/**
 * Created by Cheng on 16/8/2.
 */
public interface IBaseAdapter<T> {

    int TYPE_LOAD_MORE = Integer.MAX_VALUE;
    int TYPE_LOAD_MORE_COMPLETE = Integer.MAX_VALUE - 1;
    int TYPE_LOAD_MORE_ERROR = Integer.MAX_VALUE - 2;
    int TYPE_LOADING = Integer.MAX_VALUE - 3;
    int TYPE_EMPTY = Integer.MAX_VALUE - 4;
    int TYPE_ERROR = Integer.MAX_VALUE - 5;

    void setLoadMoreEnable(boolean loadMoreEnable);

    void setLoadMoreListener(TXOnLoadMoreListener loadMoreListener);

    void setLoadingListener(TXOnLoadingListener listener);

    void addData(List<T> listData);

    void clearData();

    void setHasMore(boolean hasMore);

    void loadError(int errorCode, String message);

    void onReload();

    View getLoadMoreView(ViewGroup parent);

    View getLoadMoreCompleteView(ViewGroup parent);

    View getLoadMoreErrorView(ViewGroup parent, int errorCode, String message);

    View getEmptyView(ViewGroup parent);

    View getLoadingView(ViewGroup parent);

    View getErrorView(ViewGroup parent, int errorCode, String message);

}
