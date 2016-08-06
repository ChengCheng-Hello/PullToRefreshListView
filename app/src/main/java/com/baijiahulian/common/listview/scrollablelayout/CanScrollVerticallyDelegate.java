package com.baijiahulian.common.listview.scrollablelayout;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 28.03.2015.
 */
public interface CanScrollVerticallyDelegate {

    /**
     * @param direction >0向上拽，<0向下拽 同android.view.View#canScrollVertically(int)
     * @param currentY 当前滚动条Y值
     * @return 是否超过这个高度
     */
    boolean canScrollVertically(int direction, int currentY);

    /**
     * list view高度是否超过这个高度
     * 
     * @param height 希望的高度
     * @return 是否超过这个高度
     */
    boolean isHeightEnough(int height);

}