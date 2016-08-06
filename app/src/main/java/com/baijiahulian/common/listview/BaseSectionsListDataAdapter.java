package com.baijiahulian.common.listview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by yanglei on 15/11/25.
 * 
 * 带section的ListView
 */
public abstract class BaseSectionsListDataAdapter<K, V> extends
    RecyclerView.Adapter<BaseSectionsListDataAdapter.ViewHolder> implements MySectionIndexer, IAbsListDataAdapter {

    protected Data<K, V> mData;

    protected boolean mIsReloading;

    public void setData(Hashtable<K, List<V>> data) {
        mData = new Data<>(data);
        mIsReloading = false;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;

        try {
            BaseListCell cell = createItemViewCell(viewType);
            if (cell == null) {
                cell = createSectionCell(viewType);
            }
            if (cell == null) {
                return null;
            }
            View v = LayoutInflater.from(parent.getContext()).inflate(cell.getCellResource(), parent, false);

            cell.initialChildViews(v);
            holder = new ViewHolder(v, cell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData.isKeyInPosition(position)) {
            K key = mData.getKey(position);
            holder.listCell.setData(key, position);
        } else {
            V value = mData.getValue(position);
            holder.listCell.setData(value, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.isKeyInPosition(position)) {
            K key = mData.getKey(position);
            return getSectionType(key);
        } else {
            V value = mData.getValue(position);
            return getItemViewType(value);
        }
    }

    /**
     * 子类需要实现下面的方法，section和item的type不能重复
     */

    /**
     * 获取section的类型
     */
    protected abstract int getSectionType(K section);

    /**
     * 创建section实例
     */
    protected abstract BaseListCell createSectionCell(int type);

    /**
     * 获取item类型
     */
    protected abstract int getItemViewType(V item);

    /**
     * 创建item cell实例
     */
    protected abstract BaseListCell createItemViewCell(int type);

    @Override
    public int getItemCount() {
        return mData.count();
    }

    @Override
    public String[] getSections() {
        return getSectionsForKeys(mData.keys());
    }

    protected abstract String[] getSectionsForKeys(K[] keys);

    @Override
    public int getPositionForSection(int section) {
        return mData.getItemPositionForKey(section);
    }

    @Override
    public void setSelection(int position) {
    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public boolean isReloading() {
        return mIsReloading;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        BaseListCell listCell;

        public ViewHolder(View itemView, BaseListCell listCell) {
            super(itemView);
            this.listCell = listCell;
        }
    }

    protected final static class Data<K, V> {

        protected final static class Key<K> {
            K key;
            int index;
        }

        private Hashtable<Key<K>, List<V>> data;
        private K[] keys;

        private int count;

        private Data(Hashtable<K, List<V>> data) {
            calculateCount(data);
            if (data != null) {
                keys = (K[]) data.keySet().toArray();
            }
        }

        protected int count() {
            return count;
        }

        protected K[] keys() {
            return keys;
        }

        private void calculateCount(Hashtable<K, List<V>> data) {
            this.data = new Hashtable<>();

            count = 0;
            if (data == null || data.isEmpty()) {
                return;
            }

            Enumeration<K> enumeration = data.keys();
            while (enumeration.hasMoreElements()) {
                K _key = enumeration.nextElement();
                Key<K> key = new Key<>();
                key.key = _key;
                key.index = count;

                this.data.put(key, data.get(_key));

                count += data.get(_key).size();
                count += 1;
            }
            return;
        }

        protected int getItemPositionForKey(int keyIndex) {
            int position = -1;
            Set<Key<K>> keySet = this.data.keySet();
            for (Key<K> key : keySet) {
                if (keyIndex == key.index) {
                    position = key.index + 1;
                    break;
                }
            }

            if (position >= count) {
                position = -1;
            }

            return position;
        }

        protected boolean isKeyInPosition(int position) {
            Set<Key<K>> keySet = this.data.keySet();
            for (Key<K> key : keySet) {
                if (position == key.index) {
                    return true;
                }
            }
            return false;
        }

        protected K getKey(int position) {
            Set<Key<K>> keySet = this.data.keySet();
            for (Key<K> key : keySet) {
                if (position == key.index) {
                    return key.key;
                }
            }
            return null;
        }

        protected V getValue(int position) {
            Set<Key<K>> keySet = this.data.keySet();
            for (Key<K> key : keySet) {
                if (position > key.index && position < (key.index + this.data.get(key).size())) {
                    return this.data.get(key).get(position - key.index);
                }
            }
            return null;
        }
    }
}
