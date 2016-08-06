package com.tx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.gsxlistviewdemo.R;
import com.cc.myptrlibrary.TXListView3;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.listener.TXOnCreateCellListener;
import com.cc.myptrlibrary.listener.TXOnGetItemViewTypeListener;
import com.cc.myptrlibrary.listener.TXOnItemClickListener;
import com.cc.myptrlibrary.listener.TXOnItemLongClickListener;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;
import com.cc.myptrlibrary.listener.TXOnReloadClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/7/28.
 */
public class TXListActivity3 extends FragmentActivity {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_LM_ERROR = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_LM_EMPTY = 4;

    private int mType = TYPE_NORMAL;
    private TXListView3<String> listView;
    private List<String> list;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXListActivity3.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tx_activity_list3);

        initTitle();

        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("hh is " + i);
        }

        listView = (TXListView3) findViewById(R.id.listView);
        listView.setPullToRefreshEnable(true);
        listView.setLoadMoreEnable(true);

        setLv();

        listView.setOnCreateCellListener(new TXOnCreateCellListener<String>() {
            @Override
            public TXBaseListCell<String> onCreateCell(int type) {
                if (type == 0) {
                    return new TestCell();
                }
                return new TestHHCell();
            }
        });
        listView.setOnGetItemViewTypeListener(new TXOnGetItemViewTypeListener() {
            @Override
            public int getItemViewType(int position) {
                if (position % 2 == 0) {
                    return 0;
                }
                return 1;
            }
        });
        listView.setOnItemClickListener(new TXOnItemClickListener<String>() {
            @Override
            public void onItemClick(String data, View view, int position) {
                Toast.makeText(view.getContext(), "data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new TXOnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(String data, View view, int position) {
                Toast.makeText(view.getContext(), "long click data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnReloadClickListener(new TXOnReloadClickListener() {
            @Override
            public void onReloadClick() {
                Toast.makeText(TXListActivity3.this, "error ", Toast.LENGTH_SHORT).show();

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.addData(list);
                        listView.loadMoreFinish(true);
                    }
                }, 2000);
            }
        });

        listView.setRefreshing(true);
    }

    private void setLv() {
        listView.setOnPullToRefreshListener(new TXOnPullToRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(TXListActivity3.this, "onRefresh ", Toast.LENGTH_SHORT).show();

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (mType) {
                            case TYPE_NORMAL:
                            case TYPE_LM_EMPTY:
                            case TYPE_LM_ERROR:
                                listView.pullToRefreshFinish(true);
                                listView.clearData();
                                listView.addData(list);
                                break;
                            case TYPE_ERROR:
                                listView.pullToRefreshFinish(false);
                                listView.clearData();
                                listView.loadError(TXListView3.ERROR_NETWORK_DISCONNECT, "error hh");
                                break;
                            case TYPE_EMPTY:
                                listView.pullToRefreshFinish(false);
                                listView.clearData();
                                listView.addData(null);
                                break;
                        }
                    }
                }, 2000);

            }
        });

        listView.setOnLoadMoreListener(new TXOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Toast.makeText(TXListActivity3.this, "onLoadMore ", Toast.LENGTH_SHORT).show();

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (mType) {
                            case TYPE_NORMAL:
                            case TYPE_EMPTY:
                            case TYPE_ERROR:
                                listView.loadMoreFinish(true);
                                listView.addData(list);
                                break;
                            case TYPE_LM_ERROR:
                                listView.loadMoreFinish(true);
                                listView.loadError(TXListView3.ERROR_NETWORK_DISCONNECT, "error");
                                break;
                            case TYPE_LM_EMPTY:
                                listView.pullToRefreshFinish(false);
                                listView.addData(null);
                                break;
                        }
                    }
                }, 2000);
            }
        });
    }

    public static class TestCell implements TXBaseListCell<String> {

        public TextView mTvPosition;
        public TextView mTvContent;

        @Override
        public void setData(String model, int position) {
            mTvPosition.setText("position " + position);
            mTvContent.setText("content " + model);
        }

        @Override
        public int getCellLayoutId() {
            return R.layout.tx_item_list;
        }

        @Override
        public void initCellViews(View view) {
            mTvPosition = (TextView) view.findViewById(R.id.tv_position);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }

    public static class TestHHCell implements TXBaseListCell<String> {

        public TextView mTvPosition;
        public TextView mTvContent;

        @Override
        public void setData(String model, int position) {
            mTvPosition.setText("hh position " + position);
            mTvContent.setText("hh content " + model);
        }

        @Override
        public int getCellLayoutId() {
            return R.layout.tx_item_list;
        }

        @Override
        public void initCellViews(View view) {
            mTvPosition = (TextView) view.findViewById(R.id.tv_position);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
        }
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
                        setLv();
                        break;
                    case R.id.action_error:
                        mType = TYPE_ERROR;
                        setLv();
                        break;
                    case R.id.action_loadmore_error:
                        mType = TYPE_LM_ERROR;
                        setLv();
                        break;
                    case R.id.action_loadmore_empty:
                        mType = TYPE_LM_EMPTY;
                        setLv();
                        break;
                    case R.id.action_empty:
                        mType = TYPE_EMPTY;
                        setLv();
                        break;
                }
                return false;
            }
        });
    }
}
