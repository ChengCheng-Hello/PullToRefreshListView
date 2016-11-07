package com.cc.listview.base.listener;

import android.content.Context;


/**
 * Created by Cheng on 16/7/26.
 */
public interface TXPullToRefreshLoadMoreListener {

    // 设置正在刷新
//    void setRefreshing(boolean refreshing);

    // 显示下拉刷新view
//    void showPullToRefreshView();

    // 隐藏下拉刷新view
//    void hidePullToRefreshView();

    // 设置是否可以下拉刷新
    void setPullToRefreshEnabled(boolean pullToRefreshEnable);

    // 设置是否可以加载更多
    void setLoadMoreEnabled(boolean loadMoreEnable);

    // 设置加载错误信息
    void showRefreshError(Context context, long code, String message);

    // 加载更多设置加载错误信息
    void showLoadMoreError(Context context, long code, String message);

    // 刷新列表
    void refresh();

    // 清除数据并刷新列表
    void clearDataAndNotify();
}
