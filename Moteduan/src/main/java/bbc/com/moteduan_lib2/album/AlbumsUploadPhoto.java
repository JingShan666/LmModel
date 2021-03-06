package bbc.com.moteduan_lib2.album;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import wei.moments.oss.OSSManager;
import wei.toolkit.WatchPictureActivity;
import wei.toolkit.bean.PhotoBean;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class AlbumsUploadPhoto extends BaseActivity {
    private ImageView mBack;
    private TextView mTitle;
    private TextView mSure;
    private RecyclerView mRv;

    private List<PhotoBean> mPicLists;
    private List<String> mResultLists;
    private int defCount;
    private int selectCount;
    private String sureText = "上传";
    private OSSManager ossManager;

    public AlbumsUploadPhoto() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_upload_photo);
        Intent intent = getIntent();
        if (intent != null) {
            defCount = intent.getIntExtra("count", 9);
        }
        ossManager = OSSManager.getInstance(AlbumsUploadPhoto.this);
        mBack = (ImageView) findViewById(R.id.activity_albums_upload_photo_back);
        mTitle = (TextView) findViewById(R.id.activity_albums_upload_photo_label);
        mSure = (TextView) findViewById(R.id.activity_albums_upload_photo_sure);
        mRv = (RecyclerView) findViewById(R.id.activity_albums_upload_photo_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new GridlayoutDecoration());
        mTitle.setText("图片");
        mSure.setText(selectCount + "/" + defCount + sureText);
        mPicLists = new ArrayList<>();
        mResultLists = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
            mPicLists.add(bean);

        }

        c.close();
        mRv.setAdapter(new PhotoPickerAdapter());

        initEvent();

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

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
                if (mResultLists.size() < 1) {
                    toast.showText("请选择要上传的照片");
                    return;
                }
                showLoadDataDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("use_id", SpDataCache.getSelfInfo(AlbumsUploadPhoto.this).getData().getM_id());
                        map.put("use_type", "2");
                        try {
                            final JSONArray jsonArray = new JSONArray();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            for (String mResultList : mResultLists) {
                                mResultList = PictureUtil.pictureCompress(mResultList);
                                BitmapFactory.decodeFile(mResultList, options);
                                int w = options.outWidth;
                                int h = options.outHeight;
                                PutObjectResult result = ossManager.synchUpLoad(mResultList, OSSManager.IMAGE_TYPE, null);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("width", w);
                                jsonObject.put("height", h);
                                jsonObject.put("url", result.getETag());
                                jsonArray.put(jsonObject);
                            }
                            map.put("photos_url", new JSONObject().put("list", jsonArray).toString());
                            Req.post(Url.uploadAlbumsPhoto, map, new Req.ReqCallback() {
                                @Override
                                public void success(String result) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        String code = jsonObject.getString("code");
                                        String tips = jsonObject.getString("tips");
                                        toast.showText(tips);
                                        if (!"200".equals(code)) {
                                            return;
                                        }
                                        startActivity(new Intent(AlbumsUploadPhoto.this, AlbumsActivity.class));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(String msg) {
                                    toast.showText(msg);
                                }

                                @Override
                                public void completed() {
                                    endLoadDataDialog();
                                }
                            });
                        } catch (ClientException e) {
                            e.printStackTrace();
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }


    private class PhotoPickerAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(wei.toolkit.R.layout.item_photo_picker, null);
            PhotoPickerAdapter.PhotoPickerHolder holder = new PhotoPickerAdapter.PhotoPickerHolder(view);
            holder.setIsRecyclable(false);
            return holder;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final PhotoBean bean = mPicLists.get(position);
            final PhotoPickerAdapter.PhotoPickerHolder h = (PhotoPickerAdapter.PhotoPickerHolder) holder;
            h.mCb.setChecked(bean.isSelected());
            h.mCb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN && !bean.isSelected()) {
                        if (defCount <= 0 || selectCount == defCount) {
                            ToastUtil.show(AlbumsUploadPhoto.this, "您当前选择照片数量已达上限");
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
            ImageLoad.bind(h.imageView, bean.getPath()
            );
            h.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> urls = new ArrayList<String>();
                    for (PhotoBean b : mPicLists) {
                        urls.add(b.getPath());
                    }
                    Intent intent = new Intent(AlbumsUploadPhoto.this, WatchPictureActivity.class);
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
