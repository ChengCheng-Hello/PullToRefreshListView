package com.cc.listview.base;

import android.view.View;
import android.view.ViewGroup;

import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnLoadingListener;


/**
 * Created by Cheng on 16/8/2.
 */
public interface TXBaseListViewAdapter<T> {

    int TYPE_LOAD_MORE = Integer.MAX_VALUE;
    int TYPE_LOAD_MORE_COMPLETE = Integer.MAX_VALUE - 1;
    int TYPE_LOADING = Integer.MAX_VALUE - 2;
    int TYPE_EMPTY = Integer.MAX_VALUE - 3;
    int TYPE_ERROR = Integer.MAX_VALUE - 4;
    int TYPE_HEADER = Integer.MAX_VALUE - 5;

    // 设置是否可以加载更多
    void setLoadMoreEnabled(boolean loadMoreEnabled);

    // 设置加载更多事件
    void setLoadMoreListener(TXOnLoadMoreListener<T> loadMoreListener);

    // 设置正在加载事件
    void setLoadingListener(TXOnLoadingListener listener);

    // 设置加载错误信息
    void loadError(long errorCode, String message);

    // 重新加载
    void onReload();

    void clearDataAndNotify();

    // 加载更多View
    View getLoadMoreView(ViewGroup parent);

    // 加载更多完成View
    View getLoadMoreCompleteView(ViewGroup parent);

    // 空View
    View getEmptyView(ViewGroup parent);

    // 正在加载View
    View getLoadingView(ViewGroup parent);

    // 出错View
    View getErrorView(ViewGroup parent, long errorCode, String message);

    View getHeaderView(ViewGroup parent);
}
