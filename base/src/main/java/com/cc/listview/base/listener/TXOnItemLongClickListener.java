package com.cc.listview.base.listener;

import android.view.View;

/**
 * 长按事件
 * <p>
 * Created by Cheng on 16/7/26.
 */
public interface TXOnItemLongClickListener<T> {

    /**
     * 长按事件回调
     *
     * @param data 数据
     * @param view view
     * @return
     */
    boolean onItemLongClick(T data, View view);
}
