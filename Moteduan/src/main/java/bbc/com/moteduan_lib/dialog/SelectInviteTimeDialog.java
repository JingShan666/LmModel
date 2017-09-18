package bbc.com.moteduan_lib.dialog;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.bean.SimpleDateBean;
import bbc.com.moteduan_lib.tools.DateUtils;
import bbc.com.moteduan_lib.tools.ToastUtils;
import wei.toolkit.widget.VViewPager;


/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class SelectInviteTimeDialog extends DialogFragment {
    private RadioGroup mRadioGroup;
    private VViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private VpAdapter mVpAdapter;
    private TextView mYearTv;
    private TextView mMonthTv;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3;
    private DateFragment mDateFragment1;
    private DateFragment mDateFragment2;
    private DateFragment mDateFragment3;
    private int mIndex;
    private static ItemBean[] mItemBeen;
    private List<SimpleDateBean> mSimpleDateBeanList;
    private List<ItemBean> mItemBeanList;
    private Button mSureBt;
    private OnSelectedListener mOnSelectedListener;

    @Override
    public void onDestroy() {
        if (mItemBeen != null) {
            mItemBeen = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_invite_time, null);
        mItemBeen = new ItemBean[6];
        mSimpleDateBeanList = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mItemBeanList = new ArrayList<>();/*全局使用，主要在确定按钮点击时用于判断。*/
        for (int i = 0; i < 15; i++) {
            ItemBean bean = new ItemBean();
            bean.setPosition(i);
            int h = 10 + i;
            bean.setHour(h);
            mItemBeanList.add(bean);
        }

        mSureBt = (Button) view.findViewById(R.id.dialog_select_invite_time_sure);
        mYearTv = (TextView) view.findViewById(R.id.dialog_select_invite_time_year);
        mMonthTv = (TextView) view.findViewById(R.id.dialog_select_invite_time_month);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.dialog_select_invite_time_rg);
        mViewPager = (VViewPager) view.findViewById(R.id.dialog_select_invite_time_rb_vp);
        mRadioButton1 = (RadioButton) view.findViewById(R.id.dialog_select_invite_time_rb_one);
        mRadioButton2 = (RadioButton) view.findViewById(R.id.dialog_select_invite_time_rb_two);
        mRadioButton3 = (RadioButton) view.findViewById(R.id.dialog_select_invite_time_rb_three);

        SimpleDateBean bean1 = DateUtils.getOtherDayInfo(0);
        SimpleDateBean bean2 = DateUtils.getOtherDayInfo(1);
        SimpleDateBean bean3 = DateUtils.getOtherDayInfo(2);
        mDateFragment1 = new DateFragment(0, bean1);
        mDateFragment2 = new DateFragment(1, bean2);
        mDateFragment3 = new DateFragment(2, bean3);
        mSimpleDateBeanList.clear();
        mSimpleDateBeanList.add(bean1);
        mSimpleDateBeanList.add(bean2);
        mSimpleDateBeanList.add(bean3);
        mRadioButton1.setText(bean1.getMonth() + "月" + bean1.getDay() + "日" + " " + DateUtils.weekDaysName[bean1.getWeek()]);
        mRadioButton2.setText(bean2.getMonth() + "月" + bean2.getDay() + "日" + " " + DateUtils.weekDaysName[bean2.getWeek()]);
        mRadioButton3.setText(bean3.getMonth() + "月" + bean3.getDay() + "日" + " " + DateUtils.weekDaysName[bean3.getWeek()]);
        mFragmentList.clear();
        mFragmentList.add(mDateFragment1);
        mFragmentList.add(mDateFragment2);
        mFragmentList.add(mDateFragment3);
        mVpAdapter = new VpAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.setAdapter(mVpAdapter);
        initEvent();
        return view;
    }

    private void initEvent() {
        mSureBt.setOnClickListener(mOnSureListener);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dialog_select_invite_time_rb_one) {
                    mViewPager.setCurrentItem(0);
                    mYearTv.setText(mSimpleDateBeanList.get(0).getYear() + "");
                    mMonthTv.setText(mSimpleDateBeanList.get(0).getMonth() + "月");

                } else if (checkedId == R.id.dialog_select_invite_time_rb_two) {
                    mViewPager.setCurrentItem(1);
                    mYearTv.setText(mSimpleDateBeanList.get(1).getYear() + "");
                    mMonthTv.setText(mSimpleDateBeanList.get(1).getMonth() + "月");

                } else if (checkedId == R.id.dialog_select_invite_time_rb_three) {
                    mViewPager.setCurrentItem(2);
                    mYearTv.setText(mSimpleDateBeanList.get(2).getYear() + "");
                    mMonthTv.setText(mSimpleDateBeanList.get(2).getMonth() + "月");

                } else {
                }

            }
        });

        selectIndex(mIndex);
    }

    private void selectIndex(int index) {
        switch (index) {
            case 0:
                mRadioGroup.check(R.id.dialog_select_invite_time_rb_one);
                break;
            case 1:
                mRadioGroup.check(R.id.dialog_select_invite_time_rb_two);
                break;
            case 2:
                mRadioGroup.check(R.id.dialog_select_invite_time_rb_three);
                break;
        }
    }

    public void show(FragmentManager manager, int index) {
        this.mIndex = index;
        show(manager, "");

    }

    private View.OnClickListener mOnSureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnSelectedListener != null) {
                int len = mItemBeen.length;
                for (int i = 0; i < len; i++) {
//                    ItemBean ib = mItemBeen[i];
//                    if (ib == null) {
//                        if (i % 2 == 0) {
//                            ItemBean itemBean = mItemBeen[i + 1];
//                            if (itemBean == null) {
//                                continue;
//                            } else {
//                                mItemBeen[i] = itemBean;
//                                mItemBeen[i + 1] = mItemBeanList.get(mItemBeen[i].getPosition() + 1);
//                            }
//                        } else {
//                            ItemBean itemBean = mItemBeen[i - 1];
//                            if (itemBean == null) {
//                                continue;
//                            } else {
//                                mItemBeen[i] = mItemBeanList.get(itemBean.getPosition() + 1);
//                            }
//                        }
//                    }
                    //---------------------------------------------提示用户必须选择大于2小时的档期------------------------------------------------------------
                    int diff;
                    ItemBean ib1, ib2;
                    boolean isPass = true;
                    switch (i) {
                        case 1:
                            ib1 = mItemBeen[0];
                            ib2 = mItemBeen[1];
                            if (ib1 == null && ib2 == null) {
                                break;
                            } else if (ib1 == null || ib2 == null) {
                                isPass = false;
                            } else {
                                diff = ib2.getHour() - ib1.getHour();
                                if (diff < 2) {
                                    isPass = false;
                                }
                            }
                            if (!isPass) {
                                ToastUtils.getInstance(getContext()).showText("档期时间不能低于2小时");
                                selectIndex(0);
                                return;
                            }
                            break;
                        case 3:
                            ib1 = mItemBeen[2];
                            ib2 = mItemBeen[3];
                            if (ib1 == null && ib2 == null) {
                                break;
                            } else if (ib1 == null || ib2 == null) {
                                isPass = false;
                            } else {
                                diff = ib2.getHour() - ib1.getHour();
                                if (diff < 2) {
                                    isPass = false;
                                }
                            }
                            if (!isPass) {
                                ToastUtils.getInstance(getContext()).showText("档期时间不能低于2小时");
                                selectIndex(1);
                                return;
                            }
                            break;
                        case 5:
                            ib1 = mItemBeen[4];
                            ib2 = mItemBeen[5];
                            if (ib1 == null && ib2 == null) {
                                break;
                            } else if (ib1 == null || ib2 == null) {
                                isPass = false;
                            } else {
                                diff = ib2.getHour() - ib1.getHour();
                                if (diff < 2) {
                                    isPass = false;
                                }
                            }
                            if (!isPass) {
                                ToastUtils.getInstance(getContext()).showText("档期时间不能低于2小时");
                                selectIndex(2);
                                return;
                            }
                            break;
                        default:
                            break;
                    }
                }

                ItemBean[] temps = mItemBeen.clone();
                mOnSelectedListener.result(temps, SelectInviteTimeDialog.this);
            }
        }
    };

    private class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    public static class DateFragment extends Fragment {
        private RecyclerView mRv;
        private RvAdapter mAdapter;
        private int mOfIndex;// 在ViewPager中的位置
        private SimpleDateBean mSimpleDateBean; //当天时间
        private List<ItemBean> itemBeanList;

        public DateFragment(int ofIndex, SimpleDateBean simpleDateBean) {
            this.mOfIndex = ofIndex;
            this.mSimpleDateBean = simpleDateBean;
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_date_select, null);
            itemBeanList = new ArrayList<>();
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            for (int i = 0; i < 15; i++) {
                ItemBean bean = new ItemBean();
                bean.setPosition(i);
                int h = 10 + i;
                bean.setHour(h);
                if (mOfIndex == 0) {
                    if (hour >= h) {
                        bean.setEnable(false);
                    }
                }
                itemBeanList.add(bean);
            }
            mRv = (RecyclerView) view.findViewById(R.id.fragment_date_select_rv);
            GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
            mRv.setLayoutManager(manager);
            mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
                RecyclerView.LayoutManager manager;
                int spanCount;
                View view = new View(getContext());
                Drawable drawable = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.text_gray));

                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                }

                @Override
                public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    int childCount = parent.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childView = parent.getChildAt(i);
                        if (i + 1 % spanCount != 0) {
                            drawable.setBounds(childView.getRight(), childView.getTop(), childView.getRight() + 1, childView.getBottom());
                            drawable.draw(c);
                            drawable.setBounds(childView.getLeft(), childView.getBottom(), childView.getRight(), childView.getBottom() + 1);
                            drawable.draw(c);
                        } else {
                            drawable.setBounds(childView.getLeft(), childView.getBottom(), childView.getRight(), childView.getBottom() + 1);
                            drawable.draw(c);
                        }
                    }
                }

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (manager == null) {
                        manager = parent.getLayoutManager();
                    }
                    if (manager instanceof GridLayoutManager) {
                        spanCount = ((GridLayoutManager) manager).getSpanCount();
                        int position = manager.getPosition(view);
                        if (position + 1 % spanCount != 0) {
                            outRect.right = 1;
                            outRect.bottom = 1;
                        } else {
                            outRect.bottom = 1;
                        }
                    }
                }
            });
            mAdapter = new RvAdapter();
            mRv.setAdapter(mAdapter);
            return view;
        }


        private class RvAdapter extends RecyclerView.Adapter {
            int selectCount;

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, null);
                RvHolder holder = new RvHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                RvHolder h = (RvHolder) holder;
                final ItemBean bean = itemBeanList.get(position);
                h.mTextView.setText(bean.getHour() + ":00");
                h.mTextView.setEnabled(bean.isEnable);
                h.mTextView.setSelected(bean.isSelected());
                h.mRl.setEnabled(bean.isEnable);
                h.mRl.setSelected(bean.isSelected());
                if (bean.isSelected()) {
                    int len = mItemBeen.length;
                    for (int i = 0; i < len; i++) {
                        ItemBean b = mItemBeen[i];
                        if (b == null) {
                            continue;
                        }
                        if (b == bean) {
                            if (i % 2 == 0) {
                                h.mTextViewTag.setText("开始");
                            } else {
                                ItemBean tempb = mItemBeen[i - 1];
                                if (tempb == null) {
                                    h.mTextViewTag.setText("开始");
                                } else {
                                    h.mTextViewTag.setText("结束");
                                }
                            }
                        }
                    }
                } else {
                    h.mTextViewTag.setText("");
                }

                h.mRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.isSelected()) {
                            --selectCount;
                            bean.setSelected(false);
                            int iblen = mItemBeen.length;
                            for (int i = 0; i < iblen; i++) {
                                ItemBean ib = mItemBeen[i];
                                if (ib == null) {
                                    continue;
                                }
                                if (ib == bean) {
                                    mItemBeen[i] = null;
                                }
                            }
                        } else {
                            if (selectCount == 2) {
                                return;
                            }
                            ++selectCount;
                            bean.setSelected(true);

                            switch (mOfIndex) {
                                case 0:
                                    ItemBean b0 = mItemBeen[0];
                                    if (b0 == null) {
                                        b0 = bean;
                                        mItemBeen[0] = b0;
                                        ItemBean b1 = mItemBeen[1];
                                        if (b1 != null) {
                                            if (b0.getPosition() > b1.getPosition()) {
                                                mItemBeen[0] = b1;
                                                mItemBeen[1] = b0;
                                            }
                                        }
                                    } else {
                                        if (bean.getPosition() < b0.getPosition()) {
                                            mItemBeen[0] = bean;
                                            mItemBeen[1] = b0;
                                        } else {
                                            mItemBeen[1] = bean;
                                        }
                                    }
                                    break;
                                case 1:
                                    ItemBean b2 = mItemBeen[2];
                                    if (b2 == null) {
                                        b2 = bean;
                                        mItemBeen[2] = b2;
                                        ItemBean b3 = mItemBeen[3];
                                        if (b3 != null) {
                                            if (b2.getPosition() > b3.getPosition()) {
                                                mItemBeen[2] = b3;
                                                mItemBeen[3] = b2;
                                            }
                                        }
                                    } else {
                                        if (bean.getPosition() < b2.getPosition()) {
                                            mItemBeen[2] = bean;
                                            mItemBeen[3] = b2;
                                        } else {
                                            mItemBeen[3] = bean;
                                        }
                                    }
                                    break;
                                case 2:
                                    ItemBean b4 = mItemBeen[4];
                                    if (b4 == null) {
                                        b4 = bean;
                                        mItemBeen[4] = b4;
                                        ItemBean b5 = mItemBeen[5];
                                        if (b5 != null) {
                                            if (b4.getPosition() > b5.getPosition()) {
                                                mItemBeen[4] = b5;
                                                mItemBeen[5] = b4;
                                            }
                                        }
                                    } else {
                                        if (bean.getPosition() < b4.getPosition()) {
                                            mItemBeen[4] = bean;
                                            mItemBeen[5] = b4;
                                        } else {
                                            mItemBeen[5] = bean;
                                        }
                                    }
                                    break;
                            }
                        }
                        notifyDataSetChanged();


                    }
                });
            }

            @Override
            public int getItemCount() {
                return itemBeanList.size();
            }
        }

        private class RvHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            TextView mTextViewTag;
            RelativeLayout mRl;

            public RvHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.item_time_tv);
                mRl = (RelativeLayout) itemView.findViewById(R.id.item_time_rl);
                mTextViewTag = (TextView) itemView.findViewById(R.id.item_time_tv_tag);
            }
        }
    }


    public static class ItemBean {
        private int position;
        private int hour;
        private boolean isSelected;
        private boolean isEnable = true;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean enable) {
            isEnable = enable;
        }
    }

    public static interface OnSelectedListener {
        public void result(ItemBean[] itemBeens, DialogFragment dialogFragment);
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

}
