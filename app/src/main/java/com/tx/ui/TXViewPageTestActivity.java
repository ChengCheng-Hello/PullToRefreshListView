package com.tx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.cc.ptr.R;

/**
 * Created by Cheng on 16/9/13.
 */
public class TXViewPageTestActivity extends FragmentActivity {

    private static final String TAG = "TXViewPageTestActivity";

    private Fragment mFragment;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXViewPageTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tx_activity_viewpage);

        initTitle();

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentByTag(TAG);
        if (mFragment == null) {
            mFragment = new TXViewPageTestFragment();
            fm.beginTransaction().add(R.id.fl_content, mFragment, TAG).commitAllowingStateLoss();
        }
    }

    private void initTitle() {
        Toolbar tb = (Toolbar) findViewById(R.id.tr);
        tb.setTitle("ViewPage+SwipeRefreshLayout");
        tb.inflateMenu(R.menu.toolbar_menu);
    }
}
