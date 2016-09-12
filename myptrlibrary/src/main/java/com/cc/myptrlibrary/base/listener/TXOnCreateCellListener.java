package com.cc.myptrlibrary.base.listener;

import com.cc.myptrlibrary.base.TXBaseListCell;

/**
 * Created by Cheng on 16/7/26.
 */
public interface TXOnCreateCellListener<T> {
    TXBaseListCell<T> onCreateCell(int type);
}
