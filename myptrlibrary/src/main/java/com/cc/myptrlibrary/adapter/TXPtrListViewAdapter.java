package com.cc.myptrlibrary.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnLoadingListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/8/2.
 */
public abstract class TXPtrListViewAdapter<T> extends BaseAdapter implements IBaseAdapter<T>, PullToRefreshBase.OnLastItemVisibleListener {

    private boolean mLoadMoreEnable;
    private boolean mHasMore;
    private boolean mIsLoadMoreShowing;
    protected List<T> mListData;
    private TXOnLoadMoreListener mLoadMoreListener;

    private boolean mIsLoading = true;
    private boolean mIsEmpty;
    private boolean mIsError;
    private int mErrorCode;
    private String mErrorMsg;

    private TXOnLoadingListener mOnLoadingListener;

    public TXPtrListViewAdapter() {
        mListData = new ArrayList<>();
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
    public void addData(List<T> listData) {
        mIsLoading = false;
        if (mOnLoadingListener != null) {
            mOnLoadingListener.onLoading(false);
        }
        mIsError = false;
        if (listData != null && listData.size() > 0) {
            mListData.addAll(listData);
        }
        mIsEmpty = mListData.size() == 0;
        mIsLoadMoreShowing = false;

        notifyDataSetChanged();
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
            if (mOnLoadingListener != null) {
                mOnLoadingListener.onLoading(true);
            }
        } else if (mIsError) {
            mIsError = false;
            mIsLoadMoreShowing = true;
        }

        notifyDataSetChanged();
    }

    @Override
    public void loadError(int errorCode, String message) {
        mIsLoadMoreShowing = false;
        mIsLoading = false;
        mIsError = true;
        mIsEmpty = mListData.size() == 0;
        if (mOnLoadingListener != null) {
            mOnLoadingListener.onLoading(mIsEmpty);
        }
        mErrorCode = errorCode;
        mErrorMsg = message;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
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
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_LOAD_MORE:
                convertView = getLoadMoreView(parent);
                break;
            case TYPE_LOAD_MORE_COMPLETE:
                convertView = getLoadMoreCompleteView(parent);
                break;
            case TYPE_LOAD_MORE_ERROR:
                convertView = getLoadMoreErrorView(parent, mErrorCode, mErrorMsg);
                break;
            case TYPE_EMPTY:
                convertView = getEmptyView(parent);
                break;
            case TYPE_ERROR:
                convertView = getErrorView(parent, mErrorCode, mErrorMsg);
                break;
            case TYPE_LOADING:
                convertView = getLoadingView(parent);
                break;
            default:
                convertView = getDefView(position, convertView, parent);
        }

        return convertView;
    }

    protected abstract View getDefView(int position, View convertView, ViewGroup parent);

    @Override
    public void onLastItemVisible() {
        if (mLoadMoreListener != null && mLoadMoreEnable && mHasMore && !mIsLoadMoreShowing && !mIsError) {
            mIsLoadMoreShowing = true;
            mLoadMoreListener.onLoadMore();
        }
    }
}
