package com.cc.listview.base.cell;

/**
 * Created by Cheng on 16/7/5.
 */
public interface TXBaseNewSwipeListCell<T> extends TXBaseListCell<T> {

    // SwipeLayout的总布局的id
    int getSwipeLayoutResourceId();

    // 内容区域的id
    int getContentLayoutResourceId();
}
