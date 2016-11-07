package com.tx.cell;

import android.view.View;
import android.widget.TextView;

import com.cc.listview.R;
import com.cc.listview.base.cell.TXBaseListCellV2;

/**
 * Created by Cheng on 16/9/10.
 */
public class TestHHCell implements TXBaseListCellV2<String> {

    public TextView mTvPosition;
    public TextView mTvContent;

    @Override
    public void setData(String model, boolean isFirst) {
        mTvPosition.setText("hh isFirst " + isFirst);
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
