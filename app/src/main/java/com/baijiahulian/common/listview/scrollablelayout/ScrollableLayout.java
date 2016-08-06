package com.baijiahulian.common.listview.scrollablelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.cc.gsxlistviewdemo.R;


/**
 *
 * 主要可以通过设置两个接口对滚动事件进行处理，
 * 其中CanScrollVerticallyDelegate必须写，否则滚动有问题，因为默认都是返回false
 * OnScrollChangedListener可以不重写，除非想做些效果，比如滚动速度和滑动速度不一致，
 *
 */
public class ScrollableLayout extends FrameLayout {

    private static final String TAG = ScrollableLayout.class.getSimpleName();
    private static final int HANDLE_CLEAR_SCROLL_STATE = 1;

    private Scroller mScroller;
    private GestureDetector mScrollDetector;
    private GestureDetector mFlingDetector;

    private CanScrollVerticallyDelegate mCanScrollVerticallyDelegate;
    private OnScrollChangedListener mOnScrollChangedListener;

    private View mTopView;
    private View mBottomView;
    private int mTouchDownX;
    private int mTouchDownY;
    private int mTouchUpX;
    private int mTouchUpY;
    private int mMaxScrollY;
    private boolean mInit = false;

    private boolean mIsScrolling = false;
    private Handler mHandler;

    public ScrollableLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ScrollableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.GSXScrollableLayout);
        try {
            final boolean flyWheel = array.getBoolean(R.styleable.GSXScrollableLayout_gsx_scrollable_scrollerFlywheel, false);
            mScroller = initScroller(context, null, flyWheel);
        } finally {
            array.recycle();
        }
        setVerticalScrollBarEnabled(true);
        mScrollDetector = new GestureDetector(context, new ScrollGestureListener());
        mFlingDetector = new GestureDetector(context, new FlingGestureListener());

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLE_CLEAR_SCROLL_STATE: {
                        // 每次滑动时设个定时，超时没滑动就改下状态值
                        mIsScrolling = false;
                        break;
                    }
                }
            }
        };
    }

    /**
     * Override this method if you wish to create own {@link Scroller}
     *
     * @param context {@link Context}
     * @param interpolator {@link Interpolator}, the default implementation passes <code>null</code>
     * @param flywheel {@link Scroller#Scroller(Context, Interpolator, boolean)}
     * @return new instance of {@link Scroller} must not bu null
     */
    protected Scroller initScroller(Context context, Interpolator interpolator, boolean flywheel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return new Scroller(context, interpolator, flywheel);
        } else {
            return new Scroller(context, interpolator);
        }
    }

    /**
     * CanScrollVerticallyDelegate
     *
     * @param delegate which will be invoked when scroll state of scrollable children is needed
     */
    public void setCanScrollVerticallyDelegate(CanScrollVerticallyDelegate delegate) {
        this.mCanScrollVerticallyDelegate = delegate;
    }

    /**
     * Also can be set via xml attribute <code>scrollable_maxScroll</code>
     *
     * @param maxY the max scroll y available for this View.
     * @see #getMaxScrollY()
     */
    public void setMaxScrollY(int maxY) {
        this.mMaxScrollY = maxY;
    }

    /**
     * @return value which represents the max scroll distance to <code>this</code> View (aka <code>header</code> height)
     * @see #setMaxScrollY(int)
     */
    public int getMaxScrollY() {
        return mMaxScrollY;
    }

    /**
     * Pass an {@link OnScrollChangedListener} if you wish to get notifications when scroll state
     * of <code>this</code> View has changed.
     * It\'s helpful for implementing own logic which depends on scroll state (e.g. parallax, alpha, etc)
     *
     * @param listener to be invoked when {@link #onScrollChanged(int, int, int, int)} has been called.
     *            Might be <code>null</code> if you don\'t want to receive scroll notifications anymore
     */
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.mOnScrollChangedListener = listener;
    }

    /**
     * @see View#onScrollChanged(int, int, int, int)
     * @see OnScrollChangedListener#onScrollChanged(int, int, int)
     */
    @Override
    public void onScrollChanged(int l, int t, int oldL, int oldT) {
        mIsScrolling = true;
        // 先删除之前的事件
        mHandler.removeMessages(HANDLE_CLEAR_SCROLL_STATE);
        // 超时没滑动就清掉滑动标记
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HANDLE_CLEAR_SCROLL_STATE), 500);
        if (mBottomView != null) {
            // 随着滑动不断改变padding
            // Log.v(TAG, "set bottom view padding bottom when scroll");
            mBottomView.setPadding(mBottomView.getPaddingLeft(), mBottomView.getPaddingTop(),
                    mBottomView.getPaddingRight(), mMaxScrollY - t);
        }
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(t, oldT, mMaxScrollY);
        }
    }

    /**
     * @see View#scrollTo(int, int)
     * @see #setCanScrollVerticallyDelegate(CanScrollVerticallyDelegate)
     * @see #setMaxScrollY(int)
     */
    @Override
    public void scrollTo(int x, int y) {
        // Log.v(TAG, "want to scroll to " + y);
        final int newY = getNewY(y);
        // Log.v(TAG, "real scroll to " + newY);
        if (newY < 0) {
            return;
        }
        // Log.v(TAG, "scroll to " + newY);
        super.scrollTo(0, newY);
    }

    protected int getNewY(int y) {
        final int currentY = getScrollY();
        if (currentY == y) {
            // Log.v(TAG, "c==y");
            return -1;
        }
        // Log.v(TAG, "current:" + currentY + " y:" + y);
        final int direction = y - currentY;
        final boolean isDraggingUpDown = direction < 0;
        if (mCanScrollVerticallyDelegate != null) {
            int listHeight = getHeight() - 2 * mMaxScrollY + y;
            if (isDraggingUpDown) {
                // 如果下面view不在最上面就先让下面view滚动，这里不做操作
                if (mCanScrollVerticallyDelegate.canScrollVertically(direction, y)) {
                    // Log.v(TAG, "can not drag up down ");
                    return -1;
                }
            } else {
                // 如果view已经滚动到最上面了 或者 不能滚动并且list view实际高度小于显示高度
                if (currentY == mMaxScrollY
                        || (!mCanScrollVerticallyDelegate.canScrollVertically(direction, y) && mCanScrollVerticallyDelegate
                        .isHeightEnough(listHeight))) {
                    // Log.v(TAG, "has been at top");
                    return -1;
                }
            }
        }

        if (y < 0) {
            y = 0;
        } else if (y > mMaxScrollY) {
            y = mMaxScrollY;
        }

        return y;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 如果再滑动中则结果触屏事件，以免传给子view造成子view误操作
        return mIsScrolling;
    }

    @Override
    public boolean dispatchTouchEvent(@SuppressWarnings("NullableProblems") MotionEvent event) {
        final int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            mTouchDownX = (int) event.getX();
            mTouchDownY = (int) event.getY();
            mScroller.abortAnimation();
            removeCallbacks(mScrollRunnable);
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mTouchUpX = (int) event.getX();
            mTouchUpY = (int) event.getY();
            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScrollFinished(mTouchUpY - mTouchDownY, Math.abs(mTouchUpX - mTouchDownX),
                        Math.abs(mTouchUpY - mTouchDownY));
            }
        }

        mScrollDetector.onTouchEvent(event);
        mFlingDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();
        Rect r1 = new Rect();
        Rect r2 = new Rect();
        if (mTopView != null) {
            mTopView.getHitRect(r1);
        }
        if (mBottomView != null) {
            mBottomView.getHitRect(r2);
        }

        // if (r1.contains((int) x, (int) y)) {
        // Log.v("MonthCalendarView", "touch in top view");
        // return false;
        // }
        // boolean rst = super.dispatchTouchEvent(event);
        // Log.v("MonthCalendarView", "ScrollableLayout dispatchTouchEvent " + event.getAction() + " return:" + rst);
        // return rst;

        // 如果某个view内，主动调一下
        if (mTopView != null && r1.contains((int) x, (int) y) && mTopView instanceof OnTouchListener) {
            ((OnTouchListener) mTopView).onTouch(mTopView, event);
        } else if (mBottomView != null && r2.contains((int) x, (int) y) && mBottomView instanceof OnTouchListener) {
            ((OnTouchListener) mBottomView).onTouch(mBottomView, event);
        }
        return super.dispatchTouchEvent(event);
    }

    private final Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {

            if (mScroller.computeScrollOffset()) {
                final int y = mScroller.getCurrY();
                final int nowY = getScrollY();
                final int diff = y - nowY;
                if (diff != 0) {
                    scrollBy(0, diff);
                }
                post(this);
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int oldY = getScrollY();
            final int nowY = mScroller.getCurrY();
            scrollTo(0, nowY);
            if (oldY != nowY) {
                onScrollChanged(0, getScrollY(), 0, oldY);
            }
            postInvalidate();
        }
    }

    @Override
    protected int computeVerticalScrollRange() {
        return mMaxScrollY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int layoutWidth = getMeasuredWidth();
        // int layoutHeight = getMeasuredHeight();
        // int heightMode = MeasureSpec.getMode(heightMeasureSpec);// 得到模式
        // int specHeight = MeasureSpec.getSize(heightMeasureSpec);// 得到尺寸
        int tempHeight = getPaddingTop() + getPaddingBottom();
        int topViewHeight = 0;// 记录第一个view的高度，用于maxScrollY
        for (int i = 0; i < count; ++i) {
            tempHeight += (getChildAt(i).getMeasuredHeight());
            if (i == 0) {
                topViewHeight += (getChildAt(i).getMeasuredHeight());

            }
            ViewGroup.LayoutParams vlp = getChildAt(i).getLayoutParams();
            int marginTop = ((LayoutParams) vlp).topMargin;
            int marginBottom = ((LayoutParams) vlp).bottomMargin;
            tempHeight += marginTop + marginBottom;
            if (i == 0) {
                topViewHeight += marginTop + marginBottom;
            }
        }
        if (count > 0) {
            mTopView = getChildAt(0);
            mBottomView = getChildAt(count - 1);
        }
        setMaxScrollY(topViewHeight);
        setMeasuredDimension(layoutWidth, tempHeight);
        if (!mInit) {
            mInit = true;
            if (mBottomView != null) {
                // Log.v(TAG, "set bottom view padding bottom when on measure");
                // 改变padding
                mBottomView.setPadding(mBottomView.getPaddingLeft(), mBottomView.getPaddingTop(),
                        mBottomView.getPaddingRight(), mMaxScrollY);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childTop = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            ViewGroup.LayoutParams vlp = view.getLayoutParams();
            int marginTop = ((LayoutParams) vlp).topMargin;
            int marginBottom = ((LayoutParams) vlp).bottomMargin;
            childTop += marginTop;
            view.layout(left, childTop, right, childTop + view.getMeasuredHeight());
            childTop += view.getMeasuredHeight() + marginBottom;
        }
    }

    private class ScrollGestureListener extends GestureListenerAdapter {

        private final int mTouchSlop;
        {
            final ViewConfiguration vc = ViewConfiguration.get(getContext());
            mTouchSlop = vc.getScaledTouchSlop();
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            final float absX = Math.abs(distanceX);

            if (absX > Math.abs(distanceY) || absX > mTouchSlop) {
                return false;
            }

            scrollBy(0, (int) distanceY);

            return true;
        }
    }

    private class FlingGestureListener extends GestureListenerAdapter {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                return false;
            }

            final int nowY = getScrollY();
            if (nowY < 0 || nowY > mMaxScrollY) {
                return false;
            }

            removeCallbacks(mScrollRunnable);
            mScroller.fling(0, nowY, 0, -(int) (velocityY + .5F), 0, 0, 0, mMaxScrollY);
            post(mScrollRunnable);

            if (mScroller.computeScrollOffset()) {

                final int finalY = mScroller.getFinalY();
                final int newY = getNewY(finalY);

                return !(finalY == nowY || newY < 0);
            }

            return false;
        }
    }
}
