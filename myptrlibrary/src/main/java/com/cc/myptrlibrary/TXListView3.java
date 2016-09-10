package com.cc.myptrlibrary;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.myptrlibrary.adapter.TXPtrRecycleViewAdapter;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.base.TXPTRAndLMBase;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnLoadingListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;
import com.cc.myptrlibrary.listener.TXOnReloadClickListener;

import java.util.List;

/**
 * Created by Cheng on 16/7/28.
 */
public class TXListView3<T> extends TXPTRAndLMBase<T> {

    public static final int ERROR_NETWORK_DISCONNECT = Integer.MAX_VALUE;
    public static final int ERROR_NETWORK_400 = Integer.MAX_VALUE - 1;

    private MyAdapter<T> mAdapter;
    private SwipeRefreshLayout mPullToRefreshView;

    public TXListView3(Context context) {
        super(context);
    }

    public TXListView3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TXListView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tx_layout_default_list_recycleview, this);
        mPullToRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        setPullToRefreshEnable(isEnablePullToRefresh());

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.rv_list);
        listView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new MyAdapter<>(this);
        mAdapter.setLoadMoreEnable(isEnableLoadMore());
        listView.setAdapter(mAdapter);

        mAdapter.setLoadingListener(new TXOnLoadingListener() {
            @Override
            public void onLoading(boolean canPtr) {
                if (!isEnablePullToRefresh()) {
                    return;
                }

                boolean refreshing = mPullToRefreshView.isRefreshing();
                if (refreshing) {
                    mPullToRefreshView.setRefreshing(false);
                }
                if (canPtr) {
                    setPullToRefreshEnable(false);
                } else {
                    setPullToRefreshEnable(true);
                }
            }
        });
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        mPullToRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.setRefreshing(refreshing);
                if (refreshing) {
                    mPullToRefreshListener.onRefresh();
                }
            }
        });
    }

    @Override
    public void setPullToRefreshEnable(boolean pullToRefreshEnable) {
        mPullToRefreshView.setEnabled(pullToRefreshEnable);
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mAdapter.setLoadMoreEnable(loadMoreEnable);
    }

    @Override
    public void pullToRefreshFinish(boolean hasMore) {
        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {
        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void loadError(int errorCode, String message) {
        mAdapter.loadError(errorCode, message);
    }

    @Override
    public void setOnPullToRefreshListener(TXOnPullToRefreshListener listener) {
        super.setOnPullToRefreshListener(listener);

        mPullToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshListener.onRefresh();
            }
        });
    }

    @Override
    public void setOnLoadMoreListener(TXOnLoadMoreListener listener) {
        super.setOnLoadMoreListener(listener);

        mAdapter.setLoadMoreListener(new TXOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mLoadMoreListener.onLoadMore();
            }
        });
    }

    @Override
    public void setOnReloadClickListener(TXOnReloadClickListener listener) {
        super.setOnReloadClickListener(listener);
    }

    @Override
    public void addData(List<T> listData) {
        mAdapter.addData(listData);
    }

    @Override
    public void clearData() {
        mAdapter.clearData();
    }

    private static class MyAdapter<T> extends TXPtrRecycleViewAdapter<T> {

        private TXListView3<T> listView;

        public MyAdapter(TXListView3<T> listView) {
            super();
            this.listView = listView;
        }

        @Override
        protected TXPtrRecycleViewAdapter.TXBaseViewHolder onDefCreateViewHolder(ViewGroup parent, int viewType) {
            if (listView.mOnCreateCellListener != null) {
                TXBaseListCell<T> txBaseListCell = listView.mOnCreateCellListener.onCreateCell(viewType);
                int cellLayoutId = txBaseListCell.getCellLayoutId();

                View itemView = LayoutInflater.from(parent.getContext()).inflate(cellLayoutId, parent, false);
                txBaseListCell.initCellViews(itemView);

                return new MyHolder(itemView, txBaseListCell);
            }
            return null;
        }

        @Override
        protected int getDefItemViewType(int position) {
            if (listView.mItemViewTypeListener != null) {
                return listView.mItemViewTypeListener.getItemViewType(position);
            }
            return super.getDefItemViewType(position);
        }

        @Override
        protected void onDefBindViewHolder(TXPtrRecycleViewAdapter.TXBaseViewHolder holder, final int position) {
            final T data = mListData.get(position);

            if (listView.mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.mOnItemClickListener.onItemClick(data, v, position);
                    }
                });
            }

            if (listView.mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return listView.mOnItemLongClickListener.onItemLongClick(data, v, position);
                    }
                });
            }

            MyHolder myHolder = (MyHolder) holder;
            myHolder.txBaseListCell.setData(data, position);
        }

        @Override
        public View getEmptyView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getEmptyLayoutId(), parent, false);

            TextView tvEmptyMsg = (TextView) view.findViewById(R.id.tx_ids_list_empty_msg);
            tvEmptyMsg.setText(listView.getEmptyMsg());

            return view;
        }

        @Override
        public View getErrorView(ViewGroup parent, int errorCode, String message) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getErrorLayoutId(), parent, false);

            TextView tv = (TextView) view.findViewById(R.id.tx_ids_list_error_msg);
            tv.setText(message + ", " + errorCode);

            view.findViewById(R.id.tx_ids_list_reload).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReload();

                    if (listView.mOnReloadClickListener != null) {
                        listView.mOnReloadClickListener.onReloadClick();
                    }
                }
            });
            return view;
        }

        @Override
        public View getLoadMoreView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(listView.getLoadingMoreLayoutId(), parent, false);
        }

        @Override
        public View getLoadMoreErrorView(ViewGroup parent, int errorCode, String message) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getLoadMoreErrorLayoutId(), parent, false);

            view.findViewById(R.id.tx_ids_list_load_more_error).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReload();

                    if (listView.mOnReloadClickListener != null) {
                        listView.mOnReloadClickListener.onReloadClick();
                    }
                }
            });
            return view;
        }

        @Override
        public View getLoadMoreCompleteView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(listView.getLoadMoreCompleteLayoutId(), parent, false);
        }

        @Override
        public View getLoadingView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.tx_layout_default_list_loading, parent, false);
        }

        private class MyHolder extends TXPtrRecycleViewAdapter.TXBaseViewHolder {

            public TXBaseListCell<T> txBaseListCell;

            public MyHolder(View view, TXBaseListCell<T> txBaseListCell) {
                super(view);
                this.txBaseListCell = txBaseListCell;
            }
        }
    }
}
