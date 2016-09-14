package com.tx.ui;

import android.view.View;
import android.widget.Toast;

import com.cc.listview.base.TXBaseListCell;
import com.tx.base.TXBaseListFragment;
import com.tx.cell.TestCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/9/13.
 */
public class TXTab1ListFragment extends TXBaseListFragment<String> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_LM_ERROR = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_LM_EMPTY = 4;

    private int mType = TYPE_NORMAL;

    private List<String> list;

    @Override
    protected void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("hh is " + i);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "onRefresh ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_NORMAL:
                    case TYPE_LM_EMPTY:
                    case TYPE_LM_ERROR:
                        mListView.pullToRefreshFinish(true);
                        mListView.clearData();
                        mListView.addAll(list);
                        break;
                    case TYPE_ERROR:
                        mListView.pullToRefreshFinish(false);
                        mListView.clearData();
                        mListView.loadError(12345, "error hh");
                        break;
                    case TYPE_EMPTY:
                        mListView.pullToRefreshFinish(false);
                        mListView.clearData();
                        mListView.addAll(null);
                        break;
                }
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(getContext(), "onLoadMore ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_NORMAL:
                    case TYPE_EMPTY:
                    case TYPE_ERROR:
                        mListView.loadMoreFinish(true);
                        mListView.addAll(list);
                        break;
                    case TYPE_LM_ERROR:
                        mListView.loadMoreFinish(true);
                        mListView.loadError(1234, "error");
                        break;
                    case TYPE_LM_EMPTY:
                        mListView.pullToRefreshFinish(false);
                        mListView.addAll(null);
                        break;
                }
            }
        }, 2000);
    }

    @Override
    public TXBaseListCell<String> onCreateCell(int type) {
        return new TestCell();
    }

    @Override
    public void onItemClick(String data, View view, int position) {
        Toast.makeText(view.getContext(), "data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(String data, View view, int position) {
        Toast.makeText(view.getContext(), "long click data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onReloadClick() {
        Toast.makeText(getContext(), "error ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.addAll(list);
                mListView.loadMoreFinish(true);
            }
        }, 2000);
    }
}
