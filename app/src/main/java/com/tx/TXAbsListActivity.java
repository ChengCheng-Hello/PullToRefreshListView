package com.tx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cc.gsxlistviewdemo.R;
import com.cc.myptrlibrary.TXListView3;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.listener.TXOnCreateCellListener;
import com.cc.myptrlibrary.listener.TXOnGetItemViewTypeListener;
import com.cc.myptrlibrary.listener.TXOnItemClickListener;
import com.cc.myptrlibrary.listener.TXOnItemLongClickListener;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;
import com.cc.myptrlibrary.listener.TXOnReloadClickListener;


/**
 * Created by Cheng on 16/9/10.
 */
public abstract class TXAbsListActivity extends FragmentActivity implements TXOnPullToRefreshListener, TXOnLoadMoreListener, TXOnCreateCellListener, TXOnGetItemViewTypeListener, TXOnItemClickListener, TXOnItemLongClickListener, TXOnReloadClickListener {

    protected TXListView3 mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView();

        initData();

        mListView = (TXListView3) findViewById(getListViewId());
        // TODO grid or layoutmanager

        if (isPullToRefreshEnabled()) {
            mListView.setOnPullToRefreshListener(this);
        }

        if (isLoadMoreEnable()) {
            mListView.setOnLoadMoreListener(this);
        }

        mListView.setOnCreateCellListener(this);
        mListView.setOnGetItemViewTypeListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnReloadClickListener(this);

        mListView.setRefreshing(true);
    }

    /**
     * 子类需要在初始化list相关类之前初始其他数据的可以重载这个方法
     */
    protected void initData() {
    }

    /**
     * 有一个默认的layout，可以根据需求重写layout，默认布局中空、加载中、错误等都使用的默认的
     */
    protected boolean bindContentView() {
        setContentView(R.layout.tx_activity_layout_listview);
        return false;
    }

    /**
     * 如果重写了layout文件则要重载这个方法，返回layout里的AbsListView id
     */
    protected int getListViewId() {
        return R.id.listView;
    }

    /**
     * 是否需要支持下拉刷新
     */
    protected boolean isPullToRefreshEnabled() {
        return true;
    }

    protected boolean isLoadMoreEnable() {
        return true;
    }

    @Override
    public abstract void onLoadMore();

    @Override
    public abstract void onRefresh();

    @Override
    public abstract void onReloadClick();

    @Override
    public abstract TXBaseListCell onCreateCell(int type);

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onItemClick(Object data, View view, int position) {

    }

    @Override
    public boolean onItemLongClick(Object data, View view, int position) {
        return false;
    }


}
