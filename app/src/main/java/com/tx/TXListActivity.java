package com.tx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.gsxlistviewdemo.R;
import com.cc.myptrlibrary.base.TXBaseListCell;
import com.cc.myptrlibrary.listener.TXOnCreateCellListener;
import com.cc.myptrlibrary.listener.TXOnGetItemViewTypeListener;
import com.cc.myptrlibrary.listener.TXOnItemClickListener;
import com.cc.myptrlibrary.listener.TXOnItemLongClickListener;
import com.cc.myptrlibrary.listener.TXOnLoadMoreListener;
import com.cc.myptrlibrary.listener.TXOnPullToRefreshListener;

/**
 * Created by Cheng on 16/7/23.
 */
public class TXListActivity extends FragmentActivity {

    private TXListView<String> mListView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tx_activity_list);

        mListView = (TXListView) findViewById(R.id.listView);
        mListView.stopLoadMore();
        mListView.setOnCreateCellListener(new TXOnCreateCellListener<String>() {
            @Override
            public TXBaseListCell<String> onCreateCell(int type) {
                if (type == 0) {
                    return new TestCell();
                }
                return new TestHHCell();
            }
        });
        mListView.setOnGetItemViewTypeListener(new TXOnGetItemViewTypeListener() {
            @Override
            public int getItemViewType(int position) {
                if (position % 2 == 0) {
                    return 0;
                }
                return 1;
            }
        });
        mListView.setOnItemClickListener(new TXOnItemClickListener<String>() {
            @Override
            public void onItemClick(String data, View view, int position) {
                Toast.makeText(view.getContext(), "data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setOnItemLongClickListener(new TXOnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(String data, View view, int position) {
                Toast.makeText(view.getContext(), "long click data is " + data + " , position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mListView.setOnPullToRefreshListener(new TXOnPullToRefreshListener() {
            @Override
            public void onRefresh() {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.clearData();
                        mListView.addAll(new String[]{"11", "22", "33", "44"});
                        mListView.loadFinish(true);
                    }
                }, 2000);
            }
        });
        mListView.setOnLoadMoreListener(new TXOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.addAll(new String[]{"aa", "bb"});
                        mListView.loadFinish(true);
                    }
                }, 2000);
            }
        });

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mListView.loadMoreFinish(true);
                mListView.addAll(new String[]{"hh", "kk", "ll", "ee"});
            }
        }, 2000);
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
}
