package bbc.com.moteduan_lib.ReleaseDate;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;

import bbc.com.moteduan_lib.bean.Picture;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.tools.ScreenUtils;
import io.rong.photoview.PhotoView;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class PreviewPicActivity extends BaseTranslucentActivity {

//    private com.aphidmobile.flip.FlipViewController flipView;
//    private com.bbc.lm.squareFragment.adapter.PreviewAdapter adapter;

    private android.support.v4.view.ViewPager viewPager;

    private java.util.ArrayList<Picture> picList;
    private java.util.ArrayList<android.view.View> viewList;

    private ViewPagerAdapter adapter;


//    private android.widget.ImageView img;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题栏
//        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.preview_activity);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        flipView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        flipView.onPause();
    }

    @Override
    public void initView() {
        viewList = new java.util.ArrayList<android.view.View>();
        picList = getIntent().getParcelableArrayListExtra("pic");
        viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public void initData() {
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("index",0));
    }


    class ViewPagerAdapter extends android.support.v4.view.PagerAdapter{

        org.xutils.image.ImageOptions ops = new org.xutils.image.ImageOptions.Builder()
                .setUseMemCache(true)
//                .setAnimation(new android.view.animation.AlphaAnimation(0.5f,1f))
                .setAutoRotate(true)
                .setSize(ScreenUtils.getScreenWidth(PreviewPicActivity.this),ScreenUtils.getScreenHeight(PreviewPicActivity.this))//图片大小
//                .setRadius(org.xutils.common.util.DensityUtil.dip2px(5))//ImageView圆角半径
//                    .setCrop(true_chat)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE)//FIT_CENTER
//            .setLoadingDrawableId(R.mipmap.ic_launcher)//加载中默认显示图片
//                .setFailureDrawableId(com.bbc.lm.R.mipmap.ic_launcher)//加载失败后默认显示图片
                .build();

        @Override
        public int getCount() {

            return picList.size();
        }

        @Override
        public boolean isViewFromObject(android.view.View view, Object object) {

            return view == object;
        }

        @Override
        public Object instantiateItem(android.view.ViewGroup container, int position) {

            String path = picList.get(position).getData();
//            android.graphics.Bitmap bmp = null;
            android.view.View layout = null;

//            try {
//
//                bmp = com.bbc.lm.toools.BitMapUtils.decodeScaleBitmapFromFilePath(
//                        path,
//                        com.bbc.lm.toools.ScreenUtils.getScreenWidth(PreviewPicActivity.this),
//                        com.bbc.lm.toools.ScreenUtils.getScreenHeight(PreviewPicActivity.this));
//
//            } catch (java.io.FileNotFoundException e) {
//                e.printStackTrace();
//            }

//            if(bmp != null){

            java.io.File f = new java.io.File(path);

            LogDebug.print("path = "+path);
//            com.bbc.lm.log.LogDebug.print("f.exists() = "+f.exists());

                layout = android.view.LayoutInflater.from(container.getContext()).inflate(R.layout.preview,null);
                PhotoView photoView = (PhotoView) layout.findViewById(R.id.img);
//                android.widget.ImageView photoView = (android.widget.ImageView) layout.findViewById(com.bbc.lm.R.id.img);

//                photoView.enable();
//                photoView.setImageBitmap(bmp);
//                photoView.setBackground(new android.graphics.drawable.BitmapDrawable(bmp));
                org.xutils.x.image().bind(photoView,path ,ops );

//            }

            container.addView(layout);
            return layout;
        }


        @Override
        public void destroyItem(android.view.ViewGroup container, int position, Object object) {

            PhotoView view = (PhotoView) object;
            android.graphics.drawable.BitmapDrawable drawable = (android.graphics.drawable.BitmapDrawable) view.getBackground();
            if(drawable != null){
                drawable.getBitmap().recycle();
            }
            ((android.view.ViewGroup)container).removeView(view);
            view.destroyDrawingCache();
            view = null;

        }
    }

}
