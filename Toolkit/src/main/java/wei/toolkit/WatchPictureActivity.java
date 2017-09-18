package wei.toolkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.VViewPager;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class WatchPictureActivity extends AppCompatActivity {
    private ImageView mBack;
    private TextView mDelete;
    private TextView mLabel;
    private VViewPager mVp;
    private int mShowPosition;
    private List<String> mPhotoBeens;
    private boolean isDelete;
    private int mOldListSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_watch);
        mBack = (ImageView) findViewById(R.id.activity_picture_watch_back);
        mLabel = (TextView) findViewById(R.id.activity_picture_watch_label);
        mDelete = (TextView) findViewById(R.id.activity_picture_watch_sure);
        mVp = (VViewPager) findViewById(R.id.lm_activity_watch_picture_vp);

        mPhotoBeens = getIntent().getStringArrayListExtra("data");
        if (mPhotoBeens == null || mPhotoBeens.size() < 1) {
            ToastUtil.show(this, "暂无照片");
            finish();
        }
        mOldListSize = mPhotoBeens.size();
        mShowPosition = getIntent().getIntExtra("position", 0);
        isDelete = getIntent().getBooleanExtra("delete", false);
        if (isDelete) {
            mDelete.setEnabled(true);
            mDelete.setText("删除");
            mDelete.setOnClickListener(deleteListenet);
        } else {
            mDelete.setVisibility(View.GONE);
        }


        refreshLabel();
        mVp.addOnPageChangeListener(onPageChangeListener);
        mVp.setAdapter(new VpAdapter());
        mVp.setCurrentItem(mShowPosition, false);
        initEvents();
    }


    @Override
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }


    private void initEvents() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult();
                finish();
            }
        });
    }

    private void setResult() {
        if (mOldListSize != mPhotoBeens.size()) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("data", (ArrayList<String>) mPhotoBeens);
            setResult(RESULT_OK, intent);
        }
    }

    private void refreshLabel() {
        mLabel.setText(mShowPosition + 1 + "/" + mPhotoBeens.size());
    }


    private View.OnClickListener deleteListenet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = mShowPosition;
            mPhotoBeens.remove(position);
            if (mPhotoBeens.size() < 1) {
                setResult();
                finish();
                return;
            }
            mVp.getAdapter().notifyDataSetChanged();
            refreshLabel();

        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mShowPosition = position;
            refreshLabel();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPhotoBeens.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.wactch_picture, container, false);
            PhotoView imageView = (PhotoView) view.findViewById(R.id.watch_picture_img);
            ImageLoad.bind(imageView, mPhotoBeens.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
