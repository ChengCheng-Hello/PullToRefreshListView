package com.cc.listview.base.listener;

import android.view.View;

/**
 * 点击事件
 * <p>
 * Created by Cheng on 16/7/26.
 */
public interface TXOnItemClickListener<T> {

    /**
     * 点击事件回调
     *
     * @param data 数据
     * @param view view
     * @return
     */
    void onItemClick(T data, View view);
}
