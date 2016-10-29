package com.cc.listview.ptrrv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cc.listview.base.TXBasePtrAdapter;
import com.cc.listview.base.TXBasePtrProcessData;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/8/2.
 */
public abstract class TXPtrRecycleViewAdapter<T> extends RecyclerView.Adapter<TXPtrRecycleViewAdapter.TXBaseViewHolder> implements TXBasePtrAdapter, TXBasePtrProcessData<T> {

    private boolean mLoadMoreEnable;
    private boolean mHasMore;
    private boolean mIsLoadMoreShowing;
    protected List<T> mListData;
    private TXOnLoadMoreListener mLoadMoreListener;

    private boolean mIsLoading = true;
    private boolean mIsEmpty;
    private boolean mIsError;
    private long mErrorCode;
    private String mErrorMsg;

    private MyHandler mHandler;
    private TXOnLoadingListener mOnLoadingListener;

    public TXPtrRecycleViewAdapter() {
        mListData = new ArrayList<>();
        mHandler = new MyHandler(this);
    }

    @Override
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mLoadMoreEnable = loadMoreEnable;
    }

    @Override
    public void setLoadMoreListener(TXOnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    @Override
    public void setLoadingListener(TXOnLoadingListener listener) {
        mOnLoadingListener = listener;
    }

    @Override
    public void addToFront(List<T> listData) {
        mIsLoading = false;
        mIsError = false;
        if (listData != null) {
            mListData.addAll(0, listData);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void addAll(List<T> listData) {
        mIsLoading = false;
        mIsError = false;
        if (listData != null) {
            mListData.addAll(listData);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void add(T data) {
        mIsLoading = false;
        mIsError = false;
        if (data != null) {
            mListData.add(data);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void insert(T data, int position) {
        mIsLoading = false;
        mIsError = false;
        if (data != null) {
            mListData.add(position, data);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void replace(T data, int position) {
        mIsLoading = false;
        mIsError = false;
        if (data != null && position >= 0 && position <= mListData.size()) {
            mListData.set(position, data);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void exchange(int i, int j) {
        mIsLoading = false;
        mIsError = false;

        int len = mListData.size();
        if (i >= 0 && i < len && j >= 0 && j < len) {
            T data = mListData.get(i);
            mListData.set(i, mListData.get(j));
            mListData.set(j, data);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void remove(int position) {
        mIsLoading = false;
        mIsError = false;
        mListData.remove(position);
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void clearData() {
        mListData.clear();
    }

    @Override
    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }

    @Override
    public void onReload() {
        if (mIsEmpty) {
            mIsError = false;
            mIsLoading = true;
        } else if (mIsError) {
            mIsError = false;
            mIsLoadMoreShowing = true;
        }

        mHandler.obtainMessage().sendToTarget();
    }

    @Override
    public void loadError(long errorCode, String message) {
        mIsLoadMoreShowing = false;
        mIsLoading = false;
        mIsError = true;
        mIsEmpty = mListData.size() == 0;
        mErrorCode = errorCode;
        mErrorMsg = message;

        mHandler.obtainMessage().sendToTarget();
    }

    public boolean showFullWidth(int position) {
        int viewType = getItemViewType(position);
        return viewType >= TYPE_ERROR;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListData.size() == 0) {
            if (mIsLoading) {
                return TYPE_LOADING;
            } else if (mIsError) {
                return TYPE_ERROR;
            } else if (mIsEmpty) {
                return TYPE_EMPTY;
            }
        }

        if (position == mListData.size()) {
            if (mLoadMoreEnable) {
                if (mIsError) {
                    return TYPE_LOAD_MORE_ERROR;
                } else if (mHasMore) {
                    return TYPE_LOAD_MORE;
                } else {
                    return TYPE_LOAD_MORE_COMPLETE;
                }
            }
        }

        return getDefItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int count = mListData.size();
        if (count == 0) {
            return 1;
        }

        if (mLoadMoreEnable || mIsError) {
            count++;
        }

        return count;
    }

    @Override
    public TXBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TXBaseViewHolder holder;
        switch (viewType) {
            case TYPE_LOAD_MORE:
                holder = new TXBaseViewHolder(getLoadMoreView(parent));
                break;
            case TYPE_LOAD_MORE_COMPLETE:
                holder = new TXBaseViewHolder(getLoadMoreCompleteView(parent));
                break;
            case TYPE_LOAD_MORE_ERROR:
                holder = new TXBaseViewHolder(getLoadMoreErrorView(parent, mErrorCode, mErrorMsg));
                break;
            case TYPE_EMPTY:
                holder = new TXBaseViewHolder(getEmptyView(parent));
                break;
            case TYPE_ERROR:
                holder = new TXBaseViewHolder(getErrorView(parent, mErrorCode, mErrorMsg));
                break;
            case TYPE_LOADING:
                holder = new TXBaseViewHolder(getLoadingView(parent));
                break;
            default:
                holder = onDefCreateViewHolder(parent, viewType);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(TXBaseViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case TYPE_LOAD_MORE:
                if (mLoadMoreListener != null && !mIsLoadMoreShowing) {
                    mIsLoadMoreShowing = true;
                    mLoadMoreListener.onLoadMore();
                }
                break;
            case TYPE_LOAD_MORE_COMPLETE:
            case TYPE_LOAD_MORE_ERROR:
            case TYPE_EMPTY:
            case TYPE_LOADING:
            case TYPE_ERROR:
                break;
            default:
                onDefBindViewHolder(holder, position);
        }
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected abstract TXBaseViewHolder onDefCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onDefBindViewHolder(TXBaseViewHolder holder, int position);

    private static class MyHandler extends Handler {

        private TXPtrRecycleViewAdapter mAdapter;

        public MyHandler(TXPtrRecycleViewAdapter adapter) {
            super();
            mAdapter = adapter;
        }

        @Override
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();

            if (mAdapter.mOnLoadingListener != null) {
                mAdapter.mOnLoadingListener.onLoading(mAdapter.mListData.size() != 0);
            }
        }
    }

    public static class TXBaseViewHolder extends RecyclerView.ViewHolder {

        public TXBaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
