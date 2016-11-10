package com.tx.cell;

import android.view.View;
import android.widget.TextView;

import com.cc.listview.R;
import com.cc.listview.base.cell.TXBaseSwipeListCellV2;
import com.tx.model.TXDataModel;
import com.tx.model.TXStudentModel;

/**
 * Created by Cheng on 16/9/10.
 */
public class TestCell implements TXBaseSwipeListCellV2<TXDataModel> {

    public TextView mTvPosition;
    public TextView mTvContent;

    @Override
    public void setData(TXDataModel model, boolean isFirst) {
        if (model != null && model instanceof TXStudentModel) {
            TXStudentModel studentModel = (TXStudentModel) model;
            mTvPosition.setText("isFirst " + isFirst);
            mTvContent.setText("content " + studentModel.name);
        }
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

    @Override
    public int getSwipeLayoutResourceId() {
        return R.id.swipeLayout;
    }

    @Override
    public int getContentLayoutResourceId() {
        return R.id.ll_content;
    }
}
