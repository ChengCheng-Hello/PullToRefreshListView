package com.cc.listview.base.listener;


import com.cc.listview.base.cell.TXBaseListCell;

/**
 * Created by Cheng on 16/7/26.
 */
public interface TXOnCreateCellListener<T> {
    TXBaseListCell<T> onCreateCell(int type);
}
