package com.cc.listview.ptrlv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.cc.listview.base.cell.TXBaseListCell;
import com.cc.listview.base.TXPTRAndLMBase;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnLoadingListener;
import com.cc.listview.base.listener.TXOnPullToRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by Cheng on 16/8/1.
 */
public class TXPtrListView<T> extends TXPTRAndLMBase<T> {

    private PullToRefreshListView mPtrLv;
    private MyAdapter<T> mAdapter;
    private ListView mLv;

    public TXPtrListView(Context context) {
        super(context);
    }

    public TXPtrListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TXPtrListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tx_layout_default_list_listview, this);
        mPtrLv = (PullToRefreshListView) view.findViewById(R.id.ptr);
        mLv = (ListView) view.findViewById(android.R.id.list);
        setPullToRefreshEnable(isEnablePullToRefresh());

        mAdapter = new MyAdapter<>(this);
        mAdapter.setLoadMoreEnable(isEnableLoadMore());
        mPtrLv.setAdapter(mAdapter);
        mPtrLv.setOnLastItemVisibleListener(mAdapter);

        mAdapter.setLoadingListener(new TXOnLoadingListener() {
            @Override
            public void onLoading(boolean canPtr) {
                if (!isEnablePullToRefresh()) {
                    return;
                }

                if (canPtr) {
                    mPtrLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    mPtrLv.setMode(PullToRefreshBase.Mode.DISABLED);
                }
            }
        });
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mPtrLv.setRefreshing(refreshing);
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
            mPtrLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            mPtrLv.setMode(PullToRefreshBase.Mode.DISABLED);
        }
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        super.setLoadMoreEnable(loadMoreEnable);

        mAdapter.setLoadMoreEnable(loadMoreEnable);
    }

    @Override
    public void pullToRefreshFinish(boolean hasMore) {
        mPtrLv.onRefreshComplete();
        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void scrollToPosition(int position) {
        mLv.setSelection(position);
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {
        mAdapter.setHasMore(hasMore);
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

    @Override
    public void loadError(long errorCode, String message) {
        mAdapter.loadError(errorCode, message);
    }

    @Override
    public void setOnPullToRefreshListener(TXOnPullToRefreshListener listener) {
        super.setOnPullToRefreshListener(listener);

        mPtrLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
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

    protected static class MyAdapter<T> extends TXPtrListViewAdapter<T> {
        private TXPtrListView<T> listView;

        public MyAdapter(TXPtrListView<T> listView) {
            super();
            this.listView = listView;
        }

        @Override
        protected View getDefView(final int position, View convertView, ViewGroup parent) {
            TXBaseListCell<T> txBaseListCell = listView.mOnCreateCellListener.onCreateCell(getItemViewType(position));
            int cellLayoutId = txBaseListCell.getCellLayoutId();

            View itemView = LayoutInflater.from(parent.getContext()).inflate(cellLayoutId, parent, false);
            txBaseListCell.initCellViews(itemView);

            final T data = mListData.get(position);
            txBaseListCell.setData(data, position);

            if (listView.mOnItemClickListener != null) {
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.mOnItemClickListener.onItemClick(data, v, position);
                    }
                });
            }

            if (listView.mOnItemLongClickListener != null) {
                itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return listView.mOnItemLongClickListener.onItemLongClick(data, v, position);
                    }
                });
            }

            return itemView;
        }

        @Override
        public View getEmptyView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getEmptyLayoutId(), null);

            TextView tvEmptyMsg = (TextView) view.findViewById(R.id.tx_ids_list_empty_msg);
            tvEmptyMsg.setText(listView.getEmptyMsg());

            setViewLayoutParams(view);
            return view;
        }

        @Override
        public View getErrorView(ViewGroup parent, long errorCode, String message) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getErrorLayoutId(), null);

            TextView tv = (TextView) view.findViewById(R.id.tx_ids_list_error_msg);
            if (tv != null) {
                tv.setText(message + ", " + errorCode);
            }

            View reloadView = view.findViewById(R.id.tx_ids_list_reload);
            if (reloadView != null) {
                reloadView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReload();

                        if (listView.mOnReloadClickListener != null) {
                            listView.mOnReloadClickListener.onReloadClick();
                        }
                    }
                });
            }

            setViewLayoutParams(view);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getLoadingLayoutId(), null);
            setViewLayoutParams(view);
            return view;
        }

        private void setViewLayoutParams(View view) {
            if (view.getLayoutParams() == null) {
                int width = listView.getWidth();
                int height = listView.getHeight();
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(width, height);
                view.setLayoutParams(layoutParams);
            }
        }
    }
}
