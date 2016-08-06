package com.cc.myptrlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cc.myptrlibrary.adapter.TXPtrListViewAdapter;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.base.TXPTRAndLMBase;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnLoadingListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by Cheng on 16/8/1.
 */
public class TXListView4<T> extends TXPTRAndLMBase<T> {

    private PullToRefreshListView mListView;
    private MyAdapter<T> mAdapter;

    public TXListView4(Context context) {
        super(context);
    }

    public TXListView4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TXListView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tx_layout_default_list_listview, this);
        mListView = (PullToRefreshListView) view.findViewById(R.id.ptr);
        setPullToRefreshEnable(isEnablePullToRefresh());

        mAdapter = new MyAdapter<>(this);
        mAdapter.setLoadMoreEnable(isEnableLoadMore());
        mListView.setAdapter(mAdapter);
        mListView.setOnLastItemVisibleListener(mAdapter);

        mAdapter.setLoadingListener(new TXOnLoadingListener() {
            @Override
            public void onLoading(boolean canPtr) {
                if (!isEnablePullToRefresh()) {
                    return;
                }

                boolean refreshing = mListView.isRefreshing();
                if (refreshing) {
                    mListView.setRefreshing(false);
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
    public void setRefreshing(boolean refreshing) {
        mListView.setRefreshing(refreshing);
    }

    @Override
    public void setPullToRefreshEnable(boolean pullToRefreshEnable) {
        if (pullToRefreshEnable) {
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        }
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mAdapter.setLoadMoreEnable(loadMoreEnable);
    }

    @Override
    public void pullToRefreshFinish(boolean hasMore) {
        mListView.onRefreshComplete();
        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {
        mAdapter.setHasMore(hasMore);
    }

    @Override
    public void addData(List<T> listData) {
        mAdapter.addData(listData);
    }

    @Override
    public void clearData() {
        mAdapter.clearData();
    }

    @Override
    public void loadError(int errorCode, String message) {
        mAdapter.loadError(errorCode, message);
    }

    @Override
    public void setOnPullToRefreshListener(TXOnPullToRefreshListener listener) {
        super.setOnPullToRefreshListener(listener);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
        private TXListView4<T> listView;

        public MyAdapter(TXListView4<T> listView) {
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
    }
}
