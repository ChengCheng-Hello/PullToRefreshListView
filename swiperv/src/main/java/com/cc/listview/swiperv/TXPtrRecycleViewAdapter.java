package com.cc.listview.swiperv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cc.listview.base.TXBaseListViewAdapter;
import com.cc.listview.base.TXBaseListViewDataProcessInterface;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/8/2.
 */
public abstract class TXPtrRecycleViewAdapter<T> extends RecyclerView.Adapter<TXPtrRecycleViewAdapter.TXBaseViewHolder> implements TXBaseListViewAdapter<T>, TXBaseListViewDataProcessInterface<T> {

    public static final int HANDLE_SET_ALL_DATA = 1;
    public static final int HANDLE_APPEND_ALL_DATA = 2;
    public static final int HANDLE_APPEND_TO_FRONT = 3;
    public static final int HANDLE_APPEND = 4;
    public static final int HANDLE_INSERT = 5;
    public static final int HANDLE_REPLACE = 6;
    public static final int HANDLE_REMOVE = 7;
    public static final int HANDLE_NO_DATA_CHANGED = 8;

    private static final int DEFAULT_MIN_PAGE_SIZE = 20;

    private boolean mHasHeader;

    private boolean mLoadMoreEnable;
    private boolean mHasMore;
    private boolean mIsLoadMoreShowing;
    protected List<T> mListData;
    private TXOnLoadMoreListener<T> mLoadMoreListener;

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

    public void setHasHeader(boolean hasHeader) {
        mHasHeader = hasHeader;
    }

    public boolean isHasHeader() {
        return mHasHeader;
    }

    @Override
    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        mLoadMoreEnable = loadMoreEnabled;
    }

    @Override
    public void setLoadMoreListener(TXOnLoadMoreListener<T> loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    @Override
    public void setLoadingListener(TXOnLoadingListener listener) {
        mOnLoadingListener = listener;
    }

    @Override
    public void setAllData(List<T> listData) {
        mHandler.obtainMessage(HANDLE_SET_ALL_DATA, listData).sendToTarget();
    }

    @Override
    public void appendData(List<T> listData) {
        mHandler.obtainMessage(HANDLE_APPEND_ALL_DATA, listData).sendToTarget();
    }

    @Override
    public void insertDataToFront(List<T> listData) {
        mHandler.obtainMessage(HANDLE_APPEND_TO_FRONT, listData).sendToTarget();
    }

    @Override
    public void appendData(T data) {
        mHandler.obtainMessage(HANDLE_APPEND, data).sendToTarget();
    }

    @Override
    public void insertData(T data, int position) {
        mHandler.obtainMessage(HANDLE_INSERT, position, 0, data).sendToTarget();
    }

    @Override
    public void replaceData(T data, int position) {
        mHandler.obtainMessage(HANDLE_REPLACE, position, 0, data).sendToTarget();
    }

    @Override
    public void removeData(T data) {
        mHandler.obtainMessage(HANDLE_REMOVE, data).sendToTarget();
    }

    @Override
    public void notifyDataChanged() {
        mHandler.obtainMessage(HANDLE_NO_DATA_CHANGED).sendToTarget();
    }

    @Override
    public boolean isEmpty() {
        return mListData == null || mListData.size() == 0;
    }

    public int getCount() {
        int count = 0;

        if (mListData != null) {
            count += mListData.size();
        }

        if (mHasHeader) {
            count++;
        }

        return count;
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

        notifyDataChanged();
    }

    @Override
    public void clearDataAndNotify() {
        mListData.clear();
        mIsEmpty = true;
        mIsError = false;
        mIsLoading = true;
        onReload();
    }

    @Override
    public void loadError(long errorCode, String message) {
        mIsLoadMoreShowing = false;
        mIsLoading = false;
        mIsError = true;
        mIsEmpty = mListData.size() == 0;
        mErrorCode = errorCode;
        mErrorMsg = message;

        notifyDataChanged();
    }

    public boolean showFullWidth(int position) {
        int viewType = getItemViewType(position);
        return viewType >= TYPE_HEADER;
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

        if (mHasHeader && position == 0) {
            return TYPE_HEADER;
        }

        if (getPosition(position) == mListData.size()) {
            if (mLoadMoreEnable) {
                if (mIsError && mHasMore) {
                    return TYPE_LOAD_MORE;
                } else if (mHasMore) {
                    return TYPE_LOAD_MORE;
                } else {
                    return TYPE_LOAD_MORE_COMPLETE;
                }
            }
        }

        T data = null;
        synchronized (this) {
            int pos = getPosition(position);
            if (pos >= 0 && pos < mListData.size()) {
                data = mListData.get(pos);
            }
        }
        return getDefItemViewType(data);
    }

    @Override
    public int getItemCount() {
        int count = mListData.size();
        if (count == 0) {
            return 1;
        }

        if (mHasHeader) {
            count++;
        }

        if (mLoadMoreEnable || mIsError) {
            count++;
        }

        return count;
    }

    private int getPosition(int position) {
        if (mHasHeader) {
            position--;
        }
        return position;
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
            case TYPE_EMPTY:
                holder = new TXBaseViewHolder(getEmptyView(parent));
                break;
            case TYPE_ERROR:
                holder = new TXBaseViewHolder(getErrorView(parent, mErrorCode, mErrorMsg));
                break;
            case TYPE_LOADING:
                holder = new TXBaseViewHolder(getLoadingView(parent));
                break;
            case TYPE_HEADER:
                holder = new TXBaseViewHolder(getHeaderView(parent));
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
                    mLoadMoreListener.onLoadMore(mListData.get(mListData.size() - 1));
                }
                break;
            case TYPE_LOAD_MORE_COMPLETE:
//            case TYPE_LOAD_MORE_ERROR:
            case TYPE_EMPTY:
            case TYPE_LOADING:
            case TYPE_ERROR:
            case TYPE_HEADER:
                break;
            default:
                onDefBindViewHolder(holder, getPosition(position));
        }
    }

    protected int getDefItemViewType(T data) {
        return 0;
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
            switch (msg.what) {
                case TXPtrRecycleViewAdapter.HANDLE_SET_ALL_DATA: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        List listData = (List) msg.obj;
                        if (listData == null || listData.size() == 0) {
                            mAdapter.mIsEmpty = true;
                            mAdapter.mHasMore = false;
                            mAdapter.mListData.clear();
                        } else {
                            mAdapter.mIsEmpty = false;
                            mAdapter.mListData.clear();
                            mAdapter.mListData.addAll(listData);
                            mAdapter.mHasMore = mAdapter.mListData.size() >= DEFAULT_MIN_PAGE_SIZE;
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_APPEND_ALL_DATA: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        List listData = (List) msg.obj;
                        if (listData == null || listData.size() == 0) {
                            mAdapter.mHasMore = false;
                        } else {
                            mAdapter.mHasMore = true;
                            mAdapter.mListData.addAll(listData);
                        }

                        mAdapter.mIsEmpty = mAdapter.mListData.size() == 0;
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_APPEND_TO_FRONT: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        List listData = (List) msg.obj;
                        if (listData == null || listData.size() == 0) {
                            return;
                        }

                        mAdapter.mListData.addAll(0, listData);
                        mAdapter.mIsEmpty = mAdapter.mListData.size() == 0;
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_APPEND: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        Object obj = msg.obj;
                        if (obj == null) {
                            return;
                        }

                        mAdapter.mListData.add(obj);
                        int size = mAdapter.mListData.size();
                        mAdapter.mIsEmpty = size == 0;
                        mAdapter.notifyItemInserted(size - 1);
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_INSERT: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        Object obj = msg.obj;
                        int position = msg.arg1;
                        if (obj == null || position < 0 || position > mAdapter.mListData.size()) {
                            return;
                        }

                        mAdapter.mListData.add(position, obj);
                        mAdapter.mIsEmpty = mAdapter.mListData.size() == 0;
                        mAdapter.notifyItemInserted(position);
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_REPLACE: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        Object obj = msg.obj;
                        int position = msg.arg1;
                        if (obj == null || position < 0 || position > mAdapter.mListData.size()) {
                            return;
                        }

                        mAdapter.mListData.set(position, obj);
                        mAdapter.mIsEmpty = mAdapter.mListData.size() == 0;
                        mAdapter.notifyItemChanged(position);
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_REMOVE: {
                    synchronized (this) {
                        mAdapter.mIsLoading = false;
                        mAdapter.mIsError = false;
                        mAdapter.mIsLoadMoreShowing = false;

                        Object obj = msg.obj;
                        if (obj == null) {
                            return;
                        }

                        int position = mAdapter.mListData.indexOf(obj);
                        if (position < 0) {
                            return;
                        }

                        mAdapter.mListData.remove(position);
                        mAdapter.mIsEmpty = mAdapter.mListData.size() == 0;
                        mAdapter.notifyItemRemoved(position);
                    }
                    break;
                }
                case TXPtrRecycleViewAdapter.HANDLE_NO_DATA_CHANGED: {
                    synchronized (this) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }

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
