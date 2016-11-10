package com.cc.listview.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cc.listview.base.listener.TXOnCreateCellListener;
import com.cc.listview.base.listener.TXOnCreateEmptyViewListener;
import com.cc.listview.base.listener.TXOnCreateErrorViewListener;
import com.cc.listview.base.listener.TXOnCreateHeaderViewListener;
import com.cc.listview.base.listener.TXOnGetItemViewTypeListener;
import com.cc.listview.base.listener.TXOnItemClickListener;
import com.cc.listview.base.listener.TXOnItemLongClickListener;
import com.cc.listview.base.listener.TXOnLoadMoreListener;
import com.cc.listview.base.listener.TXOnRefreshListener;
import com.cc.listview.base.listener.TXOnReloadClickListener;
import com.cc.listview.base.listener.TXPullToRefreshLoadMoreListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 支持下拉刷新和加载更多的基类
 * <p/>
 * Created by Cheng on 16/7/26.
 */
public abstract class TXPTRAndLMBase<T> extends FrameLayout implements TXPullToRefreshLoadMoreListener, TXBaseListViewDataProcessInterface<T> {

    // 下拉刷新事件
    protected TXOnRefreshListener mRefreshListener;
    // 加载更多事件
    protected TXOnLoadMoreListener<T> mLoadMoreListener;
    // itemViewType
    protected TXOnGetItemViewTypeListener<T> mItemViewTypeListener;
    // createCell
    protected TXOnCreateCellListener<T> mOnCreateCellListener;
    // item点击事件
    protected TXOnItemClickListener<T> mOnItemClickListener;
    // item长按事件
    protected TXOnItemLongClickListener<T> mOnItemLongClickListener;
    // 出错重新加载事件
    protected TXOnReloadClickListener mOnReloadClickListener;
    // 创建空布局事件
    protected TXOnCreateEmptyViewListener mOnCreateEmptyViewListener;
    // 创建错误布局事件
    protected TXOnCreateErrorViewListener mOnCreateErrorViewListener;
    // 创建头布局事件
    protected TXOnCreateHeaderViewListener mOnCreateHeaderViewListener;

    private int mLoadingLayoutId;
    private int mEmptyLayoutId;
    private int mErrorLayoutId;
    private boolean mEnableLoadMore;
    private boolean mEnablePullToRefresh;
    private int mLoadingMoreLayoutId;
    private int mLoadMoreCompleteLayoutId;
    private int mHeaderLayoutId;

    private String mEmptyMsg;

    private int mLayoutType;
    private int mGridSpanCount;

    private boolean mEnableSwipe;

    @ColorInt
    private int mPtrLoadingColor;
    private boolean mClipToPadding;
    private int mPadding;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mOverScroll;

    public static final int LAYOUT_TYPE_LINEAR = 0;
    public static final int LAYOUT_TYPE_GRID = 1;

    @IntDef({LAYOUT_TYPE_LINEAR, LAYOUT_TYPE_GRID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LAYOUT_TYPE {
    }

    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;

    @IntDef({OVER_SCROLL_ALWAYS, OVER_SCROLL_IF_CONTENT_SCROLLS, OVER_SCROLL_NEVER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OVER_SCROLL {
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
            mLoadingLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoading, R.layout.tx_layout_default_list_loading);
            mEmptyLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutEmpty, R.layout.tx_layout_default_list_empty);
            mErrorLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutError, R.layout.tx_layout_default_list_error);
            mLoadingMoreLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadingMore, R.layout.tx_layout_default_list_load_more);
            mLoadMoreCompleteLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadMoreComplete, R.layout.tx_layout_default_list_load_more_complete);
            mHeaderLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutHeader, 0);

            mEnableLoadMore = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnableLoadMore, true);
            mEnablePullToRefresh = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnablePullToRefresh, true);

            int emptyMsgId = a.getResourceId(R.styleable.TXPTRAndLMBase_txEmptyMsg, R.string.tx_list_empty_msg);
            mEmptyMsg = context.getString(emptyMsgId);

            mLayoutType = a.getInt(R.styleable.TXPTRAndLMBase_txLayoutType, LAYOUT_TYPE_LINEAR);
            mGridSpanCount = a.getInt(R.styleable.TXPTRAndLMBase_txGridSpanCount, 1);

            mEnableSwipe = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnableSwipe, false);

            mPtrLoadingColor = a.getColor(R.styleable.TXPTRAndLMBase_txPtrLoadingColor, ContextCompat.getColor(context, R.color.colorPrimary));
            mClipToPadding = a.getBoolean(R.styleable.TXPTRAndLMBase_txClipToPadding, true);
            mPadding = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPadding, -1);
            mPaddingTop = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingTop, 0);
            mPaddingBottom = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingBottom, 0);
            mPaddingRight = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingRight, 0);
            mPaddingLeft = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingLeft, 0);
            mOverScroll = a.getInt(R.styleable.TXPTRAndLMBase_txOverScrollMode, OVER_SCROLL_ALWAYS);

            a.recycle();
        }

        initView(context);
    }

    protected abstract void initView(Context context);

    @OVER_SCROLL
    public int getOverScroll() {
        return mOverScroll;
    }

    @LAYOUT_TYPE
    public int getLayoutType() {
        return mLayoutType;
    }

    public int getGridSpanCount() {
        return mGridSpanCount;
    }

    public void setOnPullToRefreshListener(TXOnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public void setOnLoadMoreListener(TXOnLoadMoreListener<T> listener) {
        mLoadMoreListener = listener;
    }

    public void setOnGetItemViewTypeListener(TXOnGetItemViewTypeListener<T> listener) {
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

    public void setPullToRefreshEnabled(boolean pullToRefreshEnabled) {
        this.mEnablePullToRefresh = pullToRefreshEnabled;
    }

    public void setOnCreateEmptyViewListener(TXOnCreateEmptyViewListener listener) {
        this.mOnCreateEmptyViewListener = listener;
    }

    public void setOnCreateErrorViewListener(TXOnCreateErrorViewListener listener) {
        this.mOnCreateErrorViewListener = listener;
    }

    public void setOnCreateHeaderViewListener(TXOnCreateHeaderViewListener listener) {
        this.mOnCreateHeaderViewListener = listener;
    }

    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        this.mEnableLoadMore = loadMoreEnabled;
    }

    protected int getLoadingLayoutId() {
        return mLoadingLayoutId;
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

    protected int getLoadMoreCompleteLayoutId() {
        return mLoadMoreCompleteLayoutId;
    }

    protected int getHeaderLayoutId() {
        return mHeaderLayoutId;
    }

    protected String getEmptyMsg() {
        return mEmptyMsg;
    }

    public boolean isEnableSwipe() {
        return mEnableSwipe;
    }

    @ColorInt
    public int getLoadingColor() {
        return mPtrLoadingColor;
    }

    public boolean getClipToPadding() {
        return mClipToPadding;
    }

    public int getPadding() {
        return mPadding;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public abstract void scrollToPosition(int position);
}
