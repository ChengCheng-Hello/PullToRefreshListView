package com.baijiahulian.common.listview;

/**
 * Created by yanglei on 15/11/25.
 */
public interface IAbsListDataAdapter {

    /**
     * 是否在加载数据中
     */
    boolean isReloading();

    /**
     * 是否是空
     */
    boolean isEmpty();
}
