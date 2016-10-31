package com.cc.listview.ptrrv;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.listview.base.TXBaseListCell;
import com.cc.listview.base.TXPTRAndLMBase;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnLoadingListener;
import com.cc.listview.base.listener.TXOnPullToRefreshListener;
import com.cc.listview.base.listener.TXOnReloadClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;

import java.util.List;

/**
 * Created by Cheng on 16/7/28.
 */
public class TXPtrRecycleView<T> extends TXPTRAndLMBase<T> {

    private MyAdapter<T> mAdapter;
    private PullToRefreshRecyclerView mPtrRv;
    private RecyclerView mRv;

    public TXPtrRecycleView(Context context) {
        super(context);
    }

    public TXPtrRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TXPtrRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tx_layout_default_list_recycleview2, this);
        mPtrRv = (PullToRefreshRecyclerView) view.findViewById(R.id.ptr_rv);

        setPullToRefreshEnable(isEnablePullToRefresh());

        mRv = (RecyclerView) view.findViewById(R.id.recyclerview);
        int layoutType = getLayoutType();
        if (layoutType == LAYOUT_TYPE_LINEAR) {
            mRv.setLayoutManager(new LinearLayoutManager(context));
        } else if (layoutType == LAYOUT_TYPE_GRID) {
            mRv.setLayoutManager(new GridLayoutManager(context, getGridSpanCount()));
        }

        mAdapter = new MyAdapter<>(this);
        mAdapter.setLoadMoreEnable(isEnableLoadMore());
        mRv.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = mRv.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter.showFullWidth(position)) {
                        return getGridSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }

        mAdapter.setLoadingListener(new TXOnLoadingListener() {
            @Override
            public void onLoading(boolean canPtr) {
                if (!isEnablePullToRefresh()) {
                    return;
                }

                if (canPtr) {
                    mPtrRv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    mPtrRv.setMode(PullToRefreshBase.Mode.DISABLED);
                }
            }
        });
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        mPtrRv.setRefreshing(refreshing);
    }

    @Override
    public void showPullToRefreshView() {
        // TODO
    }

    @Override
    public void hidePullToRefreshView() {
        // TODO
    }

    @Override
    public void setPullToRefreshEnable(boolean pullToRefreshEnable) {
        super.setPullToRefreshEnable(pullToRefreshEnable);

        if (pullToRefreshEnable) {
            mPtrRv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            mPtrRv.setMode(PullToRefreshBase.Mode.DISABLED);
        }
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        super.setLoadMoreEnable(loadMoreEnable);

        mAdapter.setLoadMoreEnable(loadMoreEnable);
    }

    @Override
    public void pullToRefreshFinish(boolean hasMore) {
        mPtrRv.onRefreshComplete();

        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void scrollToPosition(int position) {
        mRv.scrollToPosition(position);
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.getItemCount() == 0;
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {
        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void loadError(long errorCode, String message) {
        mAdapter.loadError(errorCode, message);
    }

    @Override
    public void setOnPullToRefreshListener(TXOnPullToRefreshListener listener) {
        super.setOnPullToRefreshListener(listener);

        mPtrRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
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
    public void addToFront(List<T> listData) {
        mAdapter.addToFront(listData);
    }

    @Override
    public void addAll(List<T> listData) {
        mAdapter.addAll(listData);
    }

    @Override
    public void add(T data) {
        mAdapter.add(data);
    }

    @Override
    public void insert(T data, int position) {
        mAdapter.insert(data, position);
    }

    @Override
    public void replace(T data, int position) {
        mAdapter.replace(data, position);
    }

    @Override
    public void remove(int position) {
        mAdapter.remove(position);
    }

    @Override
    public void exchange(int i, int j) {
        mAdapter.exchange(i, j);
    }

    @Override
    public void clearData() {
        mAdapter.clearData();
    }

    private static class MyAdapter<T> extends TXPtrRecycleViewAdapter<T> {

        private TXPtrRecycleView<T> listView;

        public MyAdapter(TXPtrRecycleView<T> listView) {
            super();
            this.listView = listView;
        }

        @Override
        protected TXBaseViewHolder onDefCreateViewHolder(ViewGroup parent, int viewType) {
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
        protected void onDefBindViewHolder(TXBaseViewHolder holder, final int position) {
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
        public View getErrorView(ViewGroup parent, long errorCode, String message) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getErrorLayoutId(), parent, false);

            TextView tv = (TextView) view.findViewById(R.id.tx_ids_list_error_msg);
            if (tv != null) {
                tv.setText(message + ", " + errorCode);
            }

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
        public View getLoadMoreErrorView(ViewGroup parent, long errorCode, String message) {
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
            return LayoutInflater.from(parent.getContext()).inflate(listView.getLoadingLayoutId(), parent, false);
        }

        private class MyHolder extends TXBaseViewHolder {

            public TXBaseListCell<T> txBaseListCell;

            public MyHolder(View view, TXBaseListCell<T> txBaseListCell) {
                super(view);
                this.txBaseListCell = txBaseListCell;
            }
        }
    }
}
