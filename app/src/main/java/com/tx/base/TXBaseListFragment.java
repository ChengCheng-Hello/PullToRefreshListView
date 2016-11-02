package com.tx.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.listview.R;
import com.cc.listview.base.cell.TXBaseListCell;
import com.cc.listview.base.listener.TXOnCreateCellListener;
import com.cc.listview.base.listener.TXOnGetItemViewTypeListener;
import com.cc.listview.base.listener.TXOnItemClickListener;
import com.cc.listview.base.listener.TXOnItemLongClickListener;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnPullToRefreshListener;
import com.cc.listview.base.listener.TXOnReloadClickListener;
import com.cc.listview.swiperv.TXPtrRecycleView;

/**
 * Created by Cheng on 16/9/13.
 */
public abstract class TXBaseListFragment<T> extends Fragment implements TXOnPullToRefreshListener, TXOnLoadMoreListener, TXOnCreateCellListener<T>, TXOnGetItemViewTypeListener, TXOnItemClickListener<T>, TXOnItemLongClickListener<T>, TXOnReloadClickListener {

    protected TXPtrRecycleView<T> mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

        mListView = (TXPtrRecycleView) getView().findViewById(getListViewId());

        if (mListView.isEnablePullToRefresh()) {
            mListView.setOnPullToRefreshListener(this);
//            mListView.setRefreshing(true);
        }

        if (mListView.isEnableLoadMore()) {
            mListView.setOnLoadMoreListener(this);
        }

        mListView.setOnCreateCellListener(this);
        mListView.setOnGetItemViewTypeListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnReloadClickListener(this);

        onRefresh();
    }

    /**
     * 子类需要在初始化list相关类之前初始其他数据的可以重载这个方法
     */
    protected void initData() {
    }

    /**
     * 有一个默认的layout，可以根据需求重写layout，默认布局中空、加载中、错误等都使用的默认的
     */
    protected int getLayoutId() {
        return R.layout.tx_activity_layout_listview;
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
