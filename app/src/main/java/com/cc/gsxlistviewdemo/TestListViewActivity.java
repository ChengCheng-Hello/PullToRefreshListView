package com.cc.gsxlistviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baijiahulian.common.listview.AbsListView;
import com.swipe.SwipeLayout;


/**
 * Created by houlijiang on 15/12/18.
 * <p/>
 * 测试列表控件
 */
public class TestListViewActivity extends AppCompatActivity {

    private AbsListView mListView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_listview);

        myAdapter = new MyAdapter();
        // mRecyclerView = (RecyclerView)findViewById(R.id.test_listview_list_2);
        // mRecyclerView.setAdapter(adapter);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mListView = (AbsListView) findViewById(R.id.test_listview_list);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(myAdapter);
        mListView.setEnableLoadMore(true);

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Data[] data = new Data[5];
                for (int i = 0; i < data.length; i++) {
                    data[i] = new Data();
                    data[i].name = "这是测试文本，index：" + i;
                }

                myAdapter.addAll(data);
            }
        }, 5 * 1000);

        mListView.setOnLoadMoreListener(new AbsListView.IOnLoadMore() {
            @Override
            public void onLoadMore() {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Data[] data = new Data[5];
                        for (int i = 0; i < data.length; i++) {
                            data[i] = new Data();
                            data[i].name = "这是测试文本，index：" + i;
                        }

                        myAdapter.addAll(data);
//                        mListView.stopLoadMore();
                    }
                }, 5 * 1000);
            }
        });
    }

    public class MyAdapter extends TXBaseListSwipeAdapter<Data> {

        @Override
        protected TXBaseListSwipeCell<Data> createCell(int type) {
            return new ItemCell();
        }
    }

    public class ItemCell implements TXBaseListSwipeCell<Data>, View.OnClickListener {

        private TextView tv;
        private View btn1;
        private View btn2;
        private TextView tvDelete;

        @Override
        public void setData(Data model, int position) {
            tv.setText(model.name);
            btn1.setTag(model.name);
            btn2.setTag(model.name);
            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
            tvDelete.setTag("delete");
            tvDelete.setOnClickListener(this);
        }

        @Override
        public int getCellResource() {
            return R.layout.item_test_listview;
        }

        @Override
        public void initialChildViews(View view) {
            tvDelete = (TextView) view.findViewById(R.id.tv_delete);
            tv = (TextView) view.findViewById(R.id.item_test_listview_tv);
            btn1 = view.findViewById(R.id.item_test_listview_btn1);
            btn2 = view.findViewById(R.id.item_test_listview_btn2);
            view.findViewById(R.id.ll_item).setOnClickListener(this);
            SwipeLayout swipe = (SwipeLayout) view.findViewById(R.id.swipe);
            swipe.setClickToClose(false);
            view.setTag("onitemclick");
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String str = (String) view.getTag();
            Toast.makeText(view.getContext(), str, Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getSwipeLayoutResourceId() {
            return R.id.swipe;
        }
    }

    public class Data {
        public String name;
    }
}
