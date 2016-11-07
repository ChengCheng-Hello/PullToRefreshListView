package com.cc.listview.base.listener;

/**
 * 加载更多
 * <p>
 * Created by Cheng on 16/7/26.
 */
public interface TXOnLoadMoreListener<T> {

    /**
     * 加载更多回调
     *
     * @param lastData 最后一条数据
     */
    void onLoadMore(T lastData);
}
