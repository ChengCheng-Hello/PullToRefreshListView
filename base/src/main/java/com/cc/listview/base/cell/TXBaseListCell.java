package com.cc.listview.base.cell;

import android.view.View;

/**
 * Created by Cheng on 16/7/25.
 */
public interface TXBaseListCell<T> {

    /**
     * 处理设置数据被回调
     *
     * @param model    数据
     * @param position 数据位置
     */
    void setData(T model, int position);

    /**
     * @return 当前 cell 的布局文件 id
     */
    int getCellLayoutId();

    /**
     * 初始化 cell 的各个子 view。
     *
     * @param view 根view
     */
    void initCellViews(View view);

}
