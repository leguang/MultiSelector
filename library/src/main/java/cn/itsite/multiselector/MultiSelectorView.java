package cn.itsite.multiselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Created by leguang on 2017/7/12 0012.
 * Email：langmanleguang@qq.com
 * <p>
 * 一个级联多选择控件，适合地区选择等场景。
 */

public class MultiSelectorView extends LinearLayout {
    public static final String TAG = MultiSelectorView.class.getSimpleName();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private OnItemClickListener mOnItemClickListener;
    private OnSelectedListener mOnSelectedListener;
    private List<PagerHolder> mPagerHolders = new ArrayList<>();
    private VPAdapter pagerAdapter;
    private int mMode;
    private int selectedColor = 0xFFFF0000;
    private int nomalColor = 0xFF222222;
    private int mIndicatorColor = 0xFFFF0000;
    private boolean tabVisible;
    private int level = 1;

    @RestrictTo(LIBRARY_GROUP)
    @IntDef(value = {TabLayout.MODE_SCROLLABLE, TabLayout.MODE_FIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public MultiSelectorView(Context context) {
        this(context, null);
    }

    public MultiSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultiSelector, 0, 0);
        try {
            if (a.hasValue(R.styleable.MultiSelector_nomalColor)) {
                nomalColor = a.getColor(R.styleable.MultiSelector_nomalColor, 0);
            }
            if (a.hasValue(R.styleable.MultiSelector_selectedColor)) {
                selectedColor = a.getColor(R.styleable.MultiSelector_selectedColor, 0);
            }
            if (a.hasValue(R.styleable.MultiSelector_indicatorColor)) {
                mIndicatorColor = a.getColor(R.styleable.MultiSelector_indicatorColor, 0);
            }
            tabVisible = a.getBoolean(R.styleable.MultiSelector_tabVisible, true);
            mMode = a.getInt(R.styleable.MultiSelector_mode, TabLayout.MODE_FIXED);
            inflate(context, R.layout.view_selector, this);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initTabLayout();
        initViewPager();
    }

    public MultiSelectorView setTabTextColors(int normalColor, int selectedColor) {
        if (mTabLayout != null) {
            mTabLayout.setTabTextColors(normalColor, selectedColor);
        }
        return this;
    }

    public MultiSelectorView setIndicatorColor(@ColorInt int color) {
        if (mTabLayout != null) {
            mTabLayout.setSelectedTabIndicatorColor(mIndicatorColor = color);
        }
        return this;
    }

    public MultiSelectorView setTabMode(@Mode int mode) {
        if (mTabLayout != null) {
            mTabLayout.setTabMode(mMode = mode);
        }
        return this;
    }

    public MultiSelectorView setLevel(int level) {
        this.level = level;
        mViewPager.setOffscreenPageLimit(level);
        return this;
    }

    public int getLevel() {
        return level;
    }

    public void notifyDataSetChanged(List<String> date) {
        PagerHolder pagerHolder = mPagerHolders.get(pagerAdapter.getCount() - 1);
        if (pagerHolder != null) {
            pagerHolder.mData = date;
            pagerHolder.recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void initTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setVisibility(tabVisible ? VISIBLE : GONE);
        setTabMode(mMode);
        setTabTextColors(nomalColor, selectedColor);
        setIndicatorColor(mIndicatorColor);
        mPagerHolders.add(new PagerHolder(0));
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new VPAdapter();
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mViewPager.getChildAt(position) instanceof RecyclerView) {
                    ((RecyclerView) mViewPager.getChildAt(position))
                            .scrollToPosition(mPagerHolders.get(position).optionPosition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public MultiSelectorView setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public MultiSelectorView setOnSelectedListener(@Nullable OnSelectedListener listener) {
        mOnSelectedListener = listener;
        return this;
    }

    private class VPAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            KLog.e(TAG, "instantiateItem------------->" + position);
            PagerHolder mPagerHolder = mPagerHolders.get(position);
            if (mPagerHolder.recyclerView == null) {
                mPagerHolder.recyclerView = new RecyclerView(container.getContext());
                mPagerHolder.recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
                mPagerHolder.recyclerView.setAdapter(new RVAdapter(mPagerHolder));
            }
            container.addView(mPagerHolder.recyclerView);
            return mPagerHolder.recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mPagerHolders != null ? mPagerHolders.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerHolders.get(position).option;
        }
    }

    private class RVAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        private PagerHolder mPagerHolder;

        RVAdapter(PagerHolder pagerHolder) {
            mPagerHolder = pagerHolder;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_selector, parent, false));
        }

        @Override
        public void onBindViewHolder(final BaseViewHolder holder, final int position) {
            holder.mTextView.setText(mPagerHolder.mData.get(position));
            holder.mTextView.setTextColor(mPagerHolder.optionPosition == position ? selectedColor : nomalColor);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPagerHolder.optionPosition = position;
                    notifyDataSetChanged();
                    mPagerHolder.option = mPagerHolder.mData.get(position);
                    for (int i = mPagerHolders.size() - 1; i > mPagerHolder.position; i--) {
                        mPagerHolders.remove(i);
                    }

                    if (pagerAdapter.getCount() < level) {
                        mPagerHolders.add(new PagerHolder(mPagerHolder.position + 1));
                        pagerAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(pagerAdapter.getCount(), true);
                    } else {
                        List<String> select = new ArrayList<>();
                        for (PagerHolder pagerHolder : mPagerHolders) {
                            select.add(pagerHolder.option);
                        }
                        if (mOnSelectedListener != null) {
                            mOnSelectedListener.onSelect(select);
                        }
                    }

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mPagerHolder.position, mPagerHolder.optionPosition, mPagerHolder.option);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mPagerHolder != null && mPagerHolder.mData != null) {
                return mPagerHolder.mData.size();
            }
            return 0;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pagerPosition, int optionPosition, String option);
    }

    public interface OnSelectedListener {
        void onSelect(List<String> select);
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        BaseViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv);
        }
    }

    private class PagerHolder {
        int position;
        int optionPosition = -1;
        List<String> mData = new ArrayList<>();
        RecyclerView recyclerView;
        String option = "请选择";

        public PagerHolder(int position) {
            this.position = position;
        }
    }
}
