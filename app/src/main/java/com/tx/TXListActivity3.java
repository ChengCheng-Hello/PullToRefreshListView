package com.tx;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cc.gsxlistviewdemo.R;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.tx.cell.TestCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/7/28.
 */
public class TXListActivity3 extends TXAbsListActivity {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_LM_ERROR = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_LM_EMPTY = 4;

    private int mType = TYPE_NORMAL;
    private List<String> list;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXListActivity3.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean bindContentView() {
        setContentView(R.layout.tx_activity_list3);
        return true;
    }

    @Override
    protected void initData() {
        initTitle();

        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("hh is " + i);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(TXListActivity3.this, "onRefresh ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_NORMAL:
                    case TYPE_LM_EMPTY:
                    case TYPE_LM_ERROR:
                        mListView.pullToRefreshFinish(true);
                        mListView.clearData();
                        mListView.addData(list);
                        break;
                    case TYPE_ERROR:
                        mListView.pullToRefreshFinish(false);
                        mListView.clearData();
                        mListView.loadError(12345, "error hh");
                        break;
                    case TYPE_EMPTY:
                        mListView.pullToRefreshFinish(false);
                        mListView.clearData();
                        mListView.addData(null);
                        break;
                }
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(TXListActivity3.this, "onLoadMore ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_NORMAL:
                    case TYPE_EMPTY:
                    case TYPE_ERROR:
                        mListView.loadMoreFinish(true);
                        mListView.addData(list);
                        break;
                    case TYPE_LM_ERROR:
                        mListView.loadMoreFinish(true);
                        mListView.loadError(1234, "error");
                        break;
                    case TYPE_LM_EMPTY:
                        mListView.pullToRefreshFinish(false);
                        mListView.addData(null);
                        break;
                }
            }
        }, 2000);
    }

    @Override
    public TXBaseListCell onCreateCell(int type) {
        return new TestCell();
    }

    @Override
    public void onItemClick(Object data, View view, int position) {
        Toast.makeText(view.getContext(), "data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(Object data, View view, int position) {
        Toast.makeText(view.getContext(), "long click data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onReloadClick() {
        Toast.makeText(TXListActivity3.this, "error ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.addData(list);
                mListView.loadMoreFinish(true);
            }
        }, 2000);
    }


    private void initTitle() {
        Toolbar tb = (Toolbar) findViewById(R.id.tr);
        tb.setTitle("RecycleView + SwipeRefreshLayout");
        tb.inflateMenu(R.menu.toolbar_menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuId = item.getItemId();
                switch (menuId) {
                    case R.id.action_normal:
                        mType = TYPE_NORMAL;
                        break;
                    case R.id.action_error:
                        mType = TYPE_ERROR;
                        break;
                    case R.id.action_loadmore_error:
                        mType = TYPE_LM_ERROR;
                        break;
                    case R.id.action_loadmore_empty:
                        mType = TYPE_LM_EMPTY;
                        break;
                    case R.id.action_empty:
                        mType = TYPE_EMPTY;
                        break;
                }
                return false;
            }
        });
    }
}
