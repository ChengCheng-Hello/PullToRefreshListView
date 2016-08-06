package com.tx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cc.gsxlistviewdemo.R;

/**
 * Created by Cheng on 16/7/23.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXListActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_list3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXListActivity3.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_list4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXListActivity4.launch(v.getContext());
            }
        });
    }
}
