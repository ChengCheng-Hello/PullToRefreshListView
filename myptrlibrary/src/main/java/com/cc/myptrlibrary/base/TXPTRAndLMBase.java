package com.cc.myptrlibrary.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cc.myptrlibrary.R;
import com.cc.myptrlibrary.listener.TXOnCreateCellListener;
import com.cc.myptrlibrary.listener.TXOnGetItemViewTypeListener;
import com.cc.myptrlibrary.listener.TXOnItemClickListener;
import com.cc.myptrlibrary.listener.TXOnItemLongClickListener;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;
import com.cc.myptrlibrary.listener.TXOnReloadClickListener;
import com.cc.myptrlibrary.listener.TXPullToRefreshLoadMoreListener;


/**
 * 支持下拉刷新和加载更多的基类
 * <p/>
 * Created by Cheng on 16/7/26.
 */
public abstract class TXPTRAndLMBase<T> extends FrameLayout implements TXPullToRefreshLoadMoreListener<T> {

    // 下拉刷新事件
    protected TXOnPullToRefreshListener mPullToRefreshListener;
    // 加载更多事件
    protected TXOnLoadMoreListener mLoadMoreListener;

    protected TXOnGetItemViewTypeListener mItemViewTypeListener;

    protected TXOnCreateCellListener<T> mOnCreateCellListener;
    // item点击事件
    protected TXOnItemClickListener<T> mOnItemClickListener;
    // item长按事件
    protected TXOnItemLongClickListener<T> mOnItemLongClickListener;
    // 出错重新加载事件
    protected TXOnReloadClickListener mOnReloadClickListener;

    private int mEmptyLayoutId;
    private int mErrorLayoutId;
    private boolean mEnableLoadMore;
    private boolean mEnablePullToRefresh;
    private int mLoadingMoreLayoutId;
    private int mLoadMoreErrorLayoutId;
    private int mLoadMoreCompleteLayoutId;

    private String mEmptyMsg;

    public TXPTRAndLMBase(Context context) {
        this(context, null);
    }

    public TXPTRAndLMBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXPTRAndLMBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TXPTRAndLMBase);
        if (a != null) {
            mEmptyLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_layoutEmpty, R.layout.tx_layout_default_list_empty);
            mErrorLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_layoutError, R.layout.tx_layout_default_list_error);
            mLoadingMoreLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_layoutLoadingMore, R.layout.tx_layout_default_list_load_more);
            mLoadMoreErrorLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_layoutLoadMoreError, R.layout.tx_layout_default_list_load_more_error);
            mLoadMoreCompleteLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_layoutLoadMoreComplete, R.layout.tx_layout_default_list_load_more_complete);

            mEnableLoadMore = a.getBoolean(R.styleable.TXPTRAndLMBase_enableLoadMore, true);
            mEnablePullToRefresh = a.getBoolean(R.styleable.TXPTRAndLMBase_enablePullToRefresh, true);

            int emptyMsgId = a.getResourceId(R.styleable.TXPTRAndLMBase_emptyMsg, R.string.tx_list_empty_msg);
            mEmptyMsg = context.getString(emptyMsgId);

            a.recycle();
        }

        initView(context);
    }

    protected abstract void initView(Context context);

    public void setOnPullToRefreshListener(TXOnPullToRefreshListener listener) {
        mPullToRefreshListener = listener;
    }

    public void setOnLoadMoreListener(TXOnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void setOnGetItemViewTypeListener(TXOnGetItemViewTypeListener listener) {
        mItemViewTypeListener = listener;
    }

    public void setOnCreateCellListener(TXOnCreateCellListener<T> listener) {
        mOnCreateCellListener = listener;
    }

    public void setOnItemClickListener(TXOnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(TXOnItemLongClickListener<T> listener) {
        mOnItemLongClickListener = listener;
    }

    public void setOnReloadClickListener(TXOnReloadClickListener listener) {
        mOnReloadClickListener = listener;
    }

    protected int getEmptyLayoutId() {
        return mEmptyLayoutId;
    }

    protected int getErrorLayoutId() {
        return mErrorLayoutId;
    }

    protected boolean isEnableLoadMore() {
        return mEnableLoadMore;
    }

    protected boolean isEnablePullToRefresh() {
        return mEnablePullToRefresh;
    }

    protected int getLoadingMoreLayoutId() {
        return mLoadingMoreLayoutId;
    }

    protected int getLoadMoreErrorLayoutId() {
        return mLoadMoreErrorLayoutId;
    }

    protected int getLoadMoreCompleteLayoutId() {
        return mLoadMoreCompleteLayoutId;
    }

    protected String getEmptyMsg() {
        return mEmptyMsg;
    }

    public void setEmptyMsg(String message) {
        this.mEmptyMsg = message;
    }
}
