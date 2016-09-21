package com.cc.listview.base.listener;

/**
 * Created by Cheng on 16/7/26.
 */
public interface TXPullToRefreshLoadMoreListener<T> {

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

    // 设置结束下拉刷新
    void pullToRefreshFinish(boolean hasMore);

    // 设置结束加载更多
    void loadMoreFinish(boolean hasMore);

    // 设置加载错误信息
    void loadError(int code, String message);
}
