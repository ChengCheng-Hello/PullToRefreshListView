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

    // 设置是否可以加载更多
    void setLoadMoreEnable(boolean loadMoreEnable);

    // 设置加载更多事件
    void setLoadMoreListener(TXOnLoadMoreListener loadMoreListener);

    // 设置正在加载事件
    void setLoadingListener(TXOnLoadingListener listener);

    // 设置是否还有更多
    void setHasMore(boolean hasMore);

    // 设置加载错误信息
    void loadError(int errorCode, String message);

    // 重新加载
    void onReload();

    // 加载更多View
    View getLoadMoreView(ViewGroup parent);

    // 加载更多完成View
    View getLoadMoreCompleteView(ViewGroup parent);

    // 加载更多出错View
    View getLoadMoreErrorView(ViewGroup parent, int errorCode, String message);

    // 空View
    View getEmptyView(ViewGroup parent);

    // 正在加载View
    View getLoadingView(ViewGroup parent);

    // 出错View
    View getErrorView(ViewGroup parent, int errorCode, String message);

    // 添加数据
    void addData(List<T> listData);

    // 添加数据
    void clearData();
}
