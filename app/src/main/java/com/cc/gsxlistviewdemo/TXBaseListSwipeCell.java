package com.cc.gsxlistviewdemo;

import com.baijiahulian.common.listview.BaseListCell;

/**
 * Created by Cheng on 16/7/5.
 */
public interface TXBaseListSwipeCell<T> extends BaseListCell<T> {

    int getSwipeLayoutResourceId();
}
