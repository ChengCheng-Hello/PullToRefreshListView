package com.cc.listview.base.listener;


import com.cc.listview.base.cell.TXBaseListCellV2;

/**
 * 创建cell
 * <p>
 * Created by Cheng on 16/7/26.
 */
public interface TXOnCreateCellListener<T> {

    /**
     * 创建cell回调
     *
     * @param viewType 类型
     * @return
     */
    TXBaseListCellV2<T> onCreateCell(int viewType);
}
