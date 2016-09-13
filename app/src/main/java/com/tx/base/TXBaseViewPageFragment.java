package com.tx.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.ptr.R;
import com.tx.views.indicator.IconPagerAdapter;
import com.tx.views.indicator.PageIndicator;

/**
 * Created by Cheng on 16/9/13.
 */
public abstract class TXBaseViewPageFragment extends Fragment implements ViewPager.OnPageChangeListener {

    protected ViewPager mViewPager = null;
    protected PageIndicator mIndicator = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getContentLayoutId() <= 0 ? R.layout.tx_fragment_viewpager_with_title : getContentLayoutId();
        View view = inflater.inflate(layoutId, container, false);

        mIndicator = (PageIndicator) view.findViewById(R.id.tx_viewpager_indicator);
        if (getIndicatorWith() > 0) {
            view.findViewById(R.id.tx_viewpager_indicator).getLayoutParams().width = getIndicatorWith();
        }
        mViewPager = (ViewPager) view.findViewById(R.id.tx_viewpager_vp);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getAdapterFragmentManager()));
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 如果是indicator类型的title可以自己设定宽度
     *
     * @return 宽度，默认是0，表示match_parent
     */
    protected int getIndicatorWith() {
        return 0;
    }

    /**
     * 获取页面资源id,返回<=0 则使用默认的
     */
    @LayoutRes
    protected int getContentLayoutId() {
        return 0;
    }

    protected FragmentManager getAdapterFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    /**
     * 刷新标题
     */
    public void notifyUpdateTitle() {
        if (mIndicator != null) {
            mIndicator.notifyDataSetChanged();
        }
    }

    /**
     * 获取定制的title样式ID，默认返回0，表示除了tab没有其他元素
     */
    protected int getCustomTitleId() {
        return 0;
    }

    /**
     * 能否左右滑动切换tab，默认是可以，子类可以重写
     */
    protected boolean canScroll() {
        return true;
    }

    /**
     * @return fragment 页数
     */
    protected abstract int getCount();

    /**
     * @param position 第几页
     * @return 当前页的 fragment 实例
     */
    protected abstract Fragment getFragment(int position);

    /**
     * @return 当前页的标题
     */
    protected abstract CharSequence getFragmentTitle(int position);

    /**
     * 自定义的title view
     */
    protected View getFragmentTabView(int position) {
        return LayoutInflater.from(this.getActivity()).inflate(R.layout.tx_layout_viewpager_tab_item, null);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

        public SampleFragmentPagerAdapter() {
            super(getActivity().getSupportFragmentManager());
        }

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TXBaseViewPageFragment.this.getCount();
        }

        @Override
        public Fragment getItem(int position) {
            return TXBaseViewPageFragment.this.getFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TXBaseViewPageFragment.this.getFragmentTitle(position);
        }

        @Override
        public View getTabView(int index) {
            return TXBaseViewPageFragment.this.getFragmentTabView(index);
        }
    }
}
