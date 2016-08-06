package com.cc.myptrlibrary.listener;

import android.view.View;

/**
 * Created by Cheng on 16/7/26.
 */
public interface TXOnItemClickListener<T> {
    void onItemClick(T data, View view, int position);
}
