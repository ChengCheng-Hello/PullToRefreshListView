package com.tx.ui;

import android.support.v4.app.Fragment;

import com.tx.base.TXBaseListFragment;
import com.tx.base.TXBaseViewPageFragment;

/**
 * Created by Cheng on 16/9/13.
 */
public class TXViewPageTestFragment extends TXBaseViewPageFragment {

    private TXBaseListFragment mFragmentStudent;
    private TXBaseListFragment mFragmentClass;


    @Override
    protected int getCount() {
        return 2;
    }

    @Override
    protected Fragment getFragment(int position) {
        switch (position) {
            case 0:
                if (mFragmentStudent == null) {
                    mFragmentStudent = new TXTab1ListFragment();

                }
                return mFragmentStudent;
            case 1:
            default:
                if (mFragmentClass == null) {
                    mFragmentClass = new TXTab1ListFragment();

                }
                return mFragmentClass;

        }
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        switch (position) {
            case 0:
                return "Tab1";
            case 1:
            default:
                return "Tab2";
        }
    }
}
