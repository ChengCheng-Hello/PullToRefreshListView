package com.tx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cc.listview.R;
import com.cc.listview.base.cell.TXBaseListCell;
import com.tx.base.TXBaseRvListActivity;
import com.tx.cell.TestCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/7/28.
 */
public class TXRvActivity extends TXBaseRvListActivity<String> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_LM_ERROR = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_LM_EMPTY = 4;

    private int mType = TYPE_NORMAL;
    private List<String> list;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXRvActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean bindContentView() {
        setContentView(R.layout.tx_activity_rv);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        onRefresh();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(TXRvActivity.this, "onRefresh ", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(TXRvActivity.this, "onLoadMore ", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(TXRvActivity.this, "error ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.addAll(list);
                mListView.loadMoreFinish(true);
            }
        }, 2000);
    }


    private void initTitle() {
        final Toolbar tb = (Toolbar) findViewById(R.id.tr);
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
                    case R.id.action_add_font:
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("this is add to front" + i);
                        }
                        mListView.addToFront(list);
                        break;
                    case R.id.action_add_one:
                        mListView.add("this is add one");
                        break;
                    case R.id.action_insert:
                        mListView.insert("this is insert to 5", 5);
                        break;
                    case R.id.action_replace:
                        mListView.replace("this is replace to 3", 3);
                        break;
                    case R.id.action_remove:
                        mListView.remove(0);
                        break;
                    case R.id.action_exchange:
                        mListView.exchange(2, 3);
                        break;
                    case R.id.action_refresh:
                        mListView.setRefreshing(true);
                        mListView.scrollToPosition(0);
                        break;
                    case R.id.action_show_refresh:
                        mListView.showPullToRefreshView();
                        break;
                    case R.id.action_hide_refresh:
                        mListView.hidePullToRefreshView();
                        break;
                }
                return false;
            }
        });
    }
}
