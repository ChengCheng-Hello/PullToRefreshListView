package com.cc.listview.base;

import java.util.List;

/**
 * Created by Cheng on 16/9/12.
 */
public interface TXBaseListViewDataProcessInterface<T> {

    // 首次or下拉刷新 添加数据
    void setAllData(List<T> listData);

    // 加载更多 添加数据
    void appendData(List<T> listData);

    // 添加到头部
    void insertDataToFront(List<T> listData);

    // 添加到尾部
    void appendData(T data);

    // 插入一条数据
    void insertData(T data, int position);

    // 替换一条数据
    void replaceData(T data, int position);

    // 移除数据
    void removeData(T data);

    // 判断数据是否为空
    boolean isEmpty();

    // 通知列表刷新
    void notifyDataChanged();
}
