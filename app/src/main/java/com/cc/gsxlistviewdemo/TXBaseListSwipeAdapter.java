package com.cc.gsxlistviewdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baijiahulian.common.listview.BaseListDataAdapter;
import com.swipe.SwipeLayout;
import com.swipe.implments.SwipeItemMangerImpl;
import com.swipe.interfaces.SwipeAdapterInterface;
import com.swipe.interfaces.SwipeItemMangerInterface;
import com.swipe.util.Attributes;

import java.util.List;

/**
 * Created by Cheng on 16/7/5.
 */
public class TXBaseListSwipeAdapter<T> extends BaseListDataAdapter<T> implements SwipeItemMangerInterface, SwipeAdapterInterface {

    public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    protected static final int DEFAULT_CELL_TYPE = 0;
    private Class<? extends TXBaseListSwipeCell<T>> mDefaultCellClass;

    public TXBaseListSwipeAdapter() {
        super();
    }

    /**
     * 如果列表只有一种cell，则构造时直接指定，否则需要重载bindCellType
     *
     * @param defaultClass 默认cell
     */
    public TXBaseListSwipeAdapter(Class<? extends TXBaseListSwipeCell<T>> defaultClass) {
        super();
        mDefaultCellClass = defaultClass;
    }

    /**
     * 如果有多中类型则子类需要重载这个方法来绑定view类型，同时返回类型
     *
     * @return cell实例
     */
    protected TXBaseListSwipeCell<T> createCell(int type) {
        // 根据类型查找对应cell并创建cell实例
        try {
            if (type == DEFAULT_CELL_TYPE && mDefaultCellClass != null) {
                return mDefaultCellClass.newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setData(ViewHolder viewHolder, int position, T data) {
        TXBaseSwipeViewHolder holder = (TXBaseSwipeViewHolder) viewHolder;
        holder.listCell.setData(data, position);

        mItemManger.bind(holder.itemView, position, holder.swipeLayoutId);
    }

    @Override
    protected TXBaseSwipeViewHolder getItemViewHolder(ViewGroup viewGroup, int type) {
        TXBaseSwipeViewHolder holder = null;
        try {
            TXBaseListSwipeCell<T> listCell = createCell(type);
            int layoutId = listCell.getCellResource();

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            if (view == null) {
                return null;
            }
            listCell.initialChildViews(view);
            holder = new TXBaseSwipeViewHolder(view, listCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    protected class TXBaseSwipeViewHolder extends BaseViewHolder {

        protected TXBaseListSwipeCell<T> listCell;
        protected int swipeLayoutId;

        protected TXBaseSwipeViewHolder(View itemView, TXBaseListSwipeCell<T> listCell) {
            super(itemView, listCell);
            this.listCell = listCell;
            this.swipeLayoutId = listCell.getSwipeLayoutResourceId();
        }
    }

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {

    }
}
