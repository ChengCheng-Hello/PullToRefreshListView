package com.cc.gsxlistviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.baijiahulian.common.listview.BaseListCell;
import com.swipe.SwipeLayout;


/**
 * Created by houlijiang on 15/12/18.
 * <p/>
 * 测试列表控件
 */
public class TestListViewActivity3 extends AppCompatActivity {


    private View mLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_listview3);

        findViewById(R.id.tv_visible).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreView.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.tv_gone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreView.setVisibility(View.GONE);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());


        ViewStub more = (ViewStub) findViewById(R.id.gsx_list_abs_list_view_load_more);
        more.setLayoutResource(R.layout.layout_listview_loadmore);
        mLoadMoreView = more.inflate();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_listview, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.mTv.setText(position + ", hh");
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView mTv;

        public MyHolder(View itemView) {
            super(itemView);

            mTv = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    public class ItemCell implements BaseListCell<Data>, View.OnClickListener {

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
    }

    public class Data {
        public String name;
    }
}
