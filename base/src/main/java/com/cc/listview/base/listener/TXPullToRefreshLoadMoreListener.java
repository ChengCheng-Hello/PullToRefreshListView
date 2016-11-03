package com.cc.listview.base.listener;

import android.content.Context;

/**
 * Created by Cheng on 16/7/26.
 */
public interface TXPullToRefreshLoadMoreListener {

    // 设置正在刷新
    void setRefreshing(boolean refreshing);

    // 显示下拉刷新view
    void showPullToRefreshView();

    // 隐藏下拉刷新view
    void hidePullToRefreshView();

    // 设置是否可以下拉刷新
    void setPullToRefreshEnable(boolean pullToRefreshEnable);

    // 设置是否可以加载更多
    void setLoadMoreEnable(boolean loadMoreEnable);

    // 设置加载错误信息
    void loadError(Context context, long code, String message);

    // 加载更多设置加载错误信息
    void loadMoreError(Context context, long code, String message);

    void refresh();
}
