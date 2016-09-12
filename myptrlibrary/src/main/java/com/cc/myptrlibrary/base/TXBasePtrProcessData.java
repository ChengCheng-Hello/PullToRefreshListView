package com.cc.myptrlibrary.base;

import java.util.List;

/**
 * Created by Cheng on 16/9/12.
 */
public interface TXBasePtrProcessData<T> {

    void addToFront(List<T> listData);

    // 添加数据
    void addAll(List<T> listData);

    void add(T data);

    void insert(T data, int position);

    void replace(T data, int position);

    void remove(int position);

    void exchange(int i, int j);

    void clearData();
}
