package com.tx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.base.listener.TXOnCreateCellListener;
import com.cc.myptrlibrary.base.listener.TXOnGetItemViewTypeListener;
import com.cc.myptrlibrary.base.listener.TXOnItemClickListener;
import com.cc.myptrlibrary.base.listener.TXOnItemLongClickListener;
import com.cc.myptrlibrary.base.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.base.listener.TXOnPullToRefreshListener;
import com.cc.myptrlibrary.base.listener.TXOnReloadClickListener;
import com.cc.myptrlibrary.rv.TXPtrRecycleView;
import com.cc.ptr.R;


/**
 * Created by Cheng on 16/9/10.
 */
public abstract class TXBaseRvListActivity<T> extends FragmentActivity implements TXOnPullToRefreshListener, TXOnLoadMoreListener, TXOnCreateCellListener<T>, TXOnGetItemViewTypeListener, TXOnItemClickListener<T>, TXOnItemLongClickListener<T>, TXOnReloadClickListener {

    protected TXPtrRecycleView<T> mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindContentView();

        initData();

        mListView = (TXPtrRecycleView) findViewById(getListViewId());

        if (mListView.isEnablePullToRefresh()) {
            mListView.setOnPullToRefreshListener(this);
            mListView.setRefreshing(true);
        }

        if (mListView.isEnableLoadMore()) {
            mListView.setOnLoadMoreListener(this);
        }

        mListView.setOnCreateCellListener(this);
        mListView.setOnGetItemViewTypeListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnReloadClickListener(this);
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

    @Override
    public abstract void onLoadMore();

    @Override
    public abstract void onRefresh();

    @Override
    public abstract void onReloadClick();

    @Override
    public abstract TXBaseListCell<T> onCreateCell(int type);

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onItemClick(T data, View view, int position) {
    }

    @Override
    public boolean onItemLongClick(T data, View view, int position) {
        return false;
    }
}
