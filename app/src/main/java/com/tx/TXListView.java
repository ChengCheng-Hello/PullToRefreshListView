package com.tx;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.baijiahulian.common.listview.AbsListView;
import com.baijiahulian.common.listview.BaseListCell;
import com.baijiahulian.common.listview.BaseListDataAdapter;
import com.cc.gsxlistviewdemo.R;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.base.TXPTRAndLMBase;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;

import java.util.List;

/**
 * Created by Cheng on 16/7/23.
 */
public class TXListView<T> extends TXPTRAndLMBase<T> {

    private AbsListView mAbsListView;
    private MyAdapter<T> mAdapter;

    public TXListView(Context context) {
        this(context, null);
    }

    public TXListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tx_layout_default_list_recycleview, this);
        mAbsListView = (AbsListView) view.findViewById(R.id.absListView);
        mAbsListView.getRecyclerView().setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new MyAdapter<>(this);
        mAbsListView.setAdapter(mAdapter);
    }

    @Override
    public void setRefreshing(boolean refreshing) {

    }

    @Override
    public void setPullToRefreshEnable(boolean pullToRefreshEnable) {
        mAbsListView.setEnableRefresh(pullToRefreshEnable);
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mAbsListView.setEnableLoadMore(loadMoreEnable);
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {

    }

    @Override
    public void pullToRefreshFinish(boolean hasMore) {

    }

    @Override
    public void addData(List<T> listData) {

    }

    public void pullRefreshFinish() {
        mAbsListView.stopRefresh();
    }

    public void loadMoreDataFinish(List<T> listData, boolean hasMore) {

    }

    public void pullRefreshFinish(List<T> listData, boolean hasMore) {

    }

    public void loadFinish(boolean hasMore) {
        mAbsListView.stopLoadMore();
        mAbsListView.setEnableLoadMore(hasMore);
    }

    @Override
    public void setOnLoadMoreListener(TXOnLoadMoreListener listener) {
        super.setOnLoadMoreListener(listener);

        mAbsListView.setOnLoadMoreListener(new AbsListView.IOnLoadMore() {
            @Override
            public void onLoadMore() {
                mLoadMoreListener.onLoadMore();
            }
        });
    }

    @Override
    public void setOnPullToRefreshListener(TXOnPullToRefreshListener listener) {
        super.setOnPullToRefreshListener(listener);

        mAbsListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshListener.onRefresh();
            }
        });
    }

    public void add(T data) {
        mAdapter.add(data);
    }

    public void addAll(T[] data) {
        mAdapter.addAll(data);
    }

    public void addToFont(T[] data) {
        mAdapter.addToFront(data);
    }

    public void insert(T data, int position) {
        mAdapter.insert(data, position);
    }

    public void replace(T data, int position) {
        mAdapter.replace(data, position);
    }

    public void remove(int position) {
        mAdapter.remove(position);
    }

    public void exchange(int i, int j) {
        mAdapter.exchange(i, j);
    }

    public void noDataChanged() {
        mAdapter.noDataChanged();
    }

    public void clearData() {
        mAdapter.clearData();
    }

    @Override
    public void loadError(int code, String message) {

    }

    public void clear() {
        mAdapter.clear();
    }

    public void stopPullRefresh() {
        mAbsListView.stopRefresh();
    }

    public void stopLoadMore() {
        mAbsListView.stopLoadMore();
    }

    public T getData(int position) {
        return mAdapter.getData(position);
    }

    public List<T> getAllData() {
        return mAdapter.getAllData();
    }

    public void showErrorView() {
        mAbsListView.showErrorView();
    }

    public View getErrorView() {
        return mAbsListView.getErrorView();
    }

    public View getEmptyView() {
        return mAbsListView.getEmptyView();
    }

    private static class MyAdapter<T> extends BaseListDataAdapter<T> {

        private TXListView<T> listView;

        public MyAdapter(TXListView<T> listView) {
            super();
            this.listView = listView;
        }

        @Override
        protected BaseListCell<T> createCell(int type) {
            if (listView.mOnCreateCellListener != null) {
                final TXBaseListCell<T> txBaseListCell = listView.mOnCreateCellListener.onCreateCell(type);
                return new BaseListCell<T>() {
                    @Override
                    public void setData(T model, int position) {
                        txBaseListCell.setData(model, position);
                    }

                    @Override
                    public int getCellResource() {
                        return txBaseListCell.getCellLayoutId();
                    }

                    @Override
                    public void initialChildViews(View view) {
                        txBaseListCell.initCellViews(view);
                    }
                };
            }
            return super.createCell(type);
        }

        @Override
        public int getItemViewType(int position) {
            if (listView.mItemViewTypeListener != null) {
                return listView.mItemViewTypeListener.getItemViewType(position);
            }
            return super.getItemViewType(position);
        }

        @Override
        protected void setData(ViewHolder viewHolder, final int position, final T data) {
            super.setData(viewHolder, position, data);

            if (listView.mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.mOnItemClickListener.onItemClick(data, v, position);
                    }
                });
            }

            if (listView.mOnItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return listView.mOnItemLongClickListener.onItemLongClick(data, v, position);
                    }
                });
            }
        }
    }


}
