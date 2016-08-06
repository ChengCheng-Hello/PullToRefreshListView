package com.cc.myptrlibrary.listener;

import java.util.List;

/**
 * Created by Cheng on 16/7/26.
 */
public interface TXPullToRefreshLoadMoreListener<T> {

    void setRefreshing(final boolean refreshing);

    void setPullToRefreshEnable(boolean pullToRefreshEnable);

    void setLoadMoreEnable(boolean loadMoreEnable);

    void loadMoreFinish(boolean hasMore);

    void pullToRefreshFinish(boolean hasMore);

    void addData(List<T> listData);

    void clearData();

    void loadError(int code, String message);
}
