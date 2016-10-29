package com.cc.listview.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cc.listview.base.listener.TXOnCreateCellListener;
import com.cc.listview.base.listener.TXOnGetItemViewTypeListener;
import com.cc.listview.base.listener.TXOnItemClickListener;
import com.cc.listview.base.listener.TXOnItemLongClickListener;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnPullToRefreshListener;
import com.cc.listview.base.listener.TXOnReloadClickListener;
import com.cc.listview.base.listener.TXPullToRefreshLoadMoreListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 支持下拉刷新和加载更多的基类
 * <p/>
 * Created by Cheng on 16/7/26.
 */
public abstract class TXPTRAndLMBase<T> extends FrameLayout implements TXPullToRefreshLoadMoreListener, TXBasePtrProcessData<T> {

    // 下拉刷新事件
    protected TXOnPullToRefreshListener mPullToRefreshListener;
    // 加载更多事件
    protected TXOnLoadMoreListener mLoadMoreListener;
    // itemViewType
    protected TXOnGetItemViewTypeListener mItemViewTypeListener;
    // createCell
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

    private int mLayoutType;
    private int mGridSpanCount;

    public static final int LAYOUT_TYPE_LINEAR = 0;
    public static final int LAYOUT_TYPE_GRID = 1;

    @IntDef({LAYOUT_TYPE_LINEAR, LAYOUT_TYPE_GRID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LAYOUT_TYPE {
    }

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
            mEmptyLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutEmpty, R.layout.tx_layout_default_list_empty);
            mErrorLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutError, R.layout.tx_layout_default_list_error);
            mLoadingMoreLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadingMore, R.layout.tx_layout_default_list_load_more);
            mLoadMoreErrorLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadMoreError, R.layout.tx_layout_default_list_load_more_error);
            mLoadMoreCompleteLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadMoreComplete, R.layout.tx_layout_default_list_load_more_complete);

            mEnableLoadMore = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnableLoadMore, true);
            mEnablePullToRefresh = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnablePullToRefresh, true);

            int emptyMsgId = a.getResourceId(R.styleable.TXPTRAndLMBase_txEmptyMsg, R.string.tx_list_empty_msg);
            mEmptyMsg = context.getString(emptyMsgId);

            mLayoutType = a.getInt(R.styleable.TXPTRAndLMBase_txLayoutType, LAYOUT_TYPE_LINEAR);
            mGridSpanCount = a.getInt(R.styleable.TXPTRAndLMBase_txGridSpanCount, 1);

            a.recycle();
        }

        initView(context);
    }

    protected abstract void initView(Context context);

    @LAYOUT_TYPE
    public int getLayoutType() {
        return mLayoutType;
    }

    public int getGridSpanCount() {
        return mGridSpanCount;
    }

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

    public void setPullToRefreshEnable(boolean pullToRefreshEnable) {
        this.mEnablePullToRefresh = pullToRefreshEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.mEnableLoadMore = loadMoreEnable;
    }

    protected int getEmptyLayoutId() {
        return mEmptyLayoutId;
    }

    protected int getErrorLayoutId() {
        return mErrorLayoutId;
    }

    public boolean isEnableLoadMore() {
        return mEnableLoadMore;
    }

    public boolean isEnablePullToRefresh() {
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

    public abstract void scrollToPosition(int position);

    public abstract boolean isEmpty();
}
