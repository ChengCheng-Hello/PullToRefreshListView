package com.baijiahulian.common.listview.scrollablelayout;

/**
 */
public interface OnScrollChangedListener {

    /**
     * This method will be invoked when scroll state
     * of {@link ScrollableLayout} has changed.
     * @see ScrollableLayout#setOnScrollChangedListener(OnScrollChangedListener)
     * @param y current scroll y
     * @param oldY previous scroll y
     * @param maxY maximum scroll y (helpful for calculating scroll ratio for e.g. for alpha to be applied)
     */
    void onScrollChanged(int y, int oldY, int maxY);

    /**
     * 滑动结束
     * @param direct 方向，小于0手指向上划，大于0手指向下滑
     * @param distanceX 横向的距离
     * @param distanceY 纵向的距离
     */
    void onScrollFinished(int direct, int distanceX, int distanceY);
}
