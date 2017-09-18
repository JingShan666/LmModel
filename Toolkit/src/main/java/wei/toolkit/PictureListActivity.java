package wei.toolkit;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wei.toolkit.bean.PhotoBean;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.CPUUtils;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class PictureListActivity extends BaseActivity {
    public static final String TAG = "PictureListActivity";
    private ImageView mBack;
    private TextView mTitle;
    private TextView mSure;
    private RecyclerView mRv;

    private List<PhotoBean> mPicLists;
    private List<String> mResultLists;
    private int defCount;
    private int selectCount;
    private String sureText = "确定";

    public PictureListActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choose);
        Intent intent = getIntent();
        if (intent != null) {
            defCount = intent.getIntExtra("count", 0);
        }
        mBack = (ImageView) findViewById(R.id.activity_picture_choose_back);
        mTitle = (TextView) findViewById(R.id.activity_picture_choose_label);
        mSure = (TextView) findViewById(R.id.activity_picture_choose_sure);
        mRv = (RecyclerView) findViewById(R.id.activity_picture_choose_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new GridlayoutDecoration());
        mTitle.setText("图片");
        mSure.setText(selectCount + "/" + defCount + sureText);
        mPicLists = new ArrayList<>();
        mResultLists = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Uri uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        ContentResolver cr = getContentResolver();

        String[] projectionPhotos = {
                MediaStore.Images.Media.BUCKET_ID,//图片所在文件夹ID
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//图片所在文件夹名称
                MediaStore.Images.Media.DISPLAY_NAME,// 图片的名字
                MediaStore.Images.Media.DATA,//图片路径
                MediaStore.Images.Media.DATE_TAKEN,//图片创建时间
        };

        Cursor c = cr.query(uri, projectionPhotos, MediaStore.Images.Media.MIME_TYPE + " in(?,?)", new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_TAKEN + " desc");

        while (c.moveToNext()) {
            String path = c.getString(3);
            File file = new File(path);
            if (!file.exists()) continue;
            PhotoBean bean = new PhotoBean();
            bean.setFileId(c.getString(0));
            bean.setFileName(c.getString(1));
            bean.setName(c.getString(2));
            bean.setPath(path);
            bean.setCreateTime(c.getString(4));
            Loger.log(TAG, bean.toString());
            mPicLists.add(bean);
        }

        c.close();
        mRv.setAdapter(new PhotoPickerAdapter());

        initEvent();

    }

    private void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("data", (ArrayList<String>) mResultLists);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private class PhotoPickerAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_picker, null);
            PhotoPickerHolder holder = new PhotoPickerHolder(view);
            holder.setIsRecyclable(false);
            return holder;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final PhotoBean bean = mPicLists.get(position);
            final PhotoPickerHolder h = (PhotoPickerHolder) holder;
            h.mCb.setChecked(bean.isSelected());
            h.mCb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN && !bean.isSelected()) {
                        if (defCount <= 0 || selectCount == defCount) {
                            ToastUtil.show(PictureListActivity.this, "您当前选择照片数量已达上限");
                            return true;
                        }
                    }
                    return false;
                }
            });
            h.mCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.isSelected()) {
                        --selectCount;
                        mResultLists.remove(bean.getPath());
                    } else {
                        ++selectCount;
                        mResultLists.add(bean.getPath());
                    }

                    mSure.setText(selectCount + "/" + defCount + sureText);
                    bean.setSelected(!bean.isSelected());

                    mSure.setEnabled(mResultLists.size() > 0);


                }
            });
            ImageLoad.bind(h.imageView, bean.getPath());
            h.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> urls = new ArrayList<String>();
                    for (PhotoBean b : mPicLists) {
                        urls.add(b.getPath());
                    }
                    Intent intent = new Intent(PictureListActivity.this, WatchPictureActivity.class);
                    intent.putStringArrayListExtra("data", (ArrayList<String>) urls);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mPicLists.size();
        }


        private class PhotoPickerHolder extends RecyclerView.ViewHolder {
            public RelativeLayout mRl;
            public ImageView imageView;
            public CheckBox mCb;

            public PhotoPickerHolder(View itemView) {
                super(itemView);
                mRl = (RelativeLayout) itemView.findViewById(R.id.item_photo_picker_rl);
                mRl.getLayoutParams().height = (int) (mRv.getWidth() * 0.3);
                mRl.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                imageView = (ImageView) itemView.findViewById(R.id.item_photo_picker_image);
                mCb = (CheckBox) itemView.findViewById(R.id.item_photo_picker_check);
            }


        }
    }
}
