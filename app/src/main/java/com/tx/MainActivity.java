package com.tx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cc.listview.R;
import com.tx.ui.TXRvActivity;
import com.tx.ui.TXViewPageTestActivity;


/**
 * Created by Cheng on 16/7/23.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_list3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXRvActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_list4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TXLvActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_list5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TestActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_list6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXViewPageTestActivity.launch(v.getContext());
            }
        });
    }
}
