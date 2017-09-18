package bbc.com.moteduan_lib2.album;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.bean.AlbumBean;
import wei.toolkit.WatchPictureActivity;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.helper.EmptyDataViewHolder;
import wei.toolkit.utils.DialogUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

public class AlbumsActivity extends BaseActivity {
    private static String TAG = "AlbumsActivity";
    private RecyclerView recyclerView;
    private SpringView springView;
    private ImageView back;
    private TextView title;
    private TextView sure;
    private String userId;
    private int userType = 2;
    private boolean canDelete;
    private boolean canUpload;
    private long timeStamp;
    private int page = 1;
    private int pageSize = 10;
    private List<AlbumBean.MapsBean> mapsBeanList = new ArrayList<>();
    private List<String> deleteLists = new ArrayList<>();
    private PopupWindow popupWindow;
    private static String userName = "";
    private AlbumsAdapter albumsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.isEmpty(userId)) {
            ToastUtils.getInstance(AlbumsActivity.this).showText("数据开小差了哦，暂时无法查看相册。");
            finish();
            return;
        }
        userName = getIntent().getStringExtra("userName");
        if (TextUtils.isEmpty(userName)) userName = "我";
        userType = getIntent().getIntExtra("userType", 2);
        canDelete = getIntent().getBooleanExtra("delete", false);
        canUpload = getIntent().getBooleanExtra("upload", false);
        initView();
        loadData(1);
        initEvents();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadData(1);
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.bar_action_back);
        title = (TextView) findViewById(R.id.bar_action_title);
        sure = (TextView) findViewById(R.id.bar_action_sure);
        springView = (SpringView) findViewById(R.id.activity_albums_sv);
        springView.setHeader(new DefaultHeader(AlbumsActivity.this));
        springView.setFooter(new DefaultFooter(AlbumsActivity.this));
        springView.setListener(onFreshListener);
        title.setText(userName + "的相册");
        if (canUpload) {
            sure.setVisibility(View.VISIBLE);
            sure.setText("上传");
        }

        recyclerView = (RecyclerView) findViewById(R.id.activity_albums_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        albumsAdapter = new AlbumsAdapter();

    }

    @Override
    public void initData() {

    }

    /**
     * @param type 1下拉 2上拉
     */
    private void loadData(final int type) {
        final Map<String, Object> map = new HashMap<>();
        map.put("use_id", userId);
        if (type == 1) {
            map.put("timeStamp", timeStamp < 1 ? System.currentTimeMillis() : timeStamp);
            map.put("pageNumber", page = 1);
        } else if (type == 2) {
            map.put("timeStamp", timeStamp);
            map.put("pageNumber", ++page);
        }
        map.put("pageSize", pageSize);
        map.put("use_type", userType);
        Req.post(Url.peekAlbums, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                AlbumBean albumBean = new Gson().fromJson(result, AlbumBean.class);
                if (!"200".equals(albumBean.getCode())) {
                    String tips = albumBean.getTips();
                    if ("201".equals(albumBean.getCode())) {
                        // 没数据
                        if (type == 1) {
                            if (mapsBeanList.size() > 0) {
                                mapsBeanList.clear();
                            }
                            recyclerView.setAdapter(albumsAdapter);
                            return;
                        }else if(type == 2){
                            tips = getResources().getString(R.string.sr_no_more_data);
                        }
                    }
                    toast.showText(tips);
                    return;
                }

                if (type == 1) {
                    mapsBeanList.clear();
                    mapsBeanList.addAll(albumBean.getMaps());
                    timeStamp = albumBean.getTimeStamp() > 0 ? albumBean.getTimeStamp() : System.currentTimeMillis();
                    recyclerView.setAdapter(albumsAdapter);
                } else {
                    mapsBeanList.addAll(albumBean.getMaps());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }


            }

            @Override
            public void failed(String msg) {
                //toast.showText(msg);
                toast.showText("网络连接不可用，请稍后重试");


            }

            @Override
            public void completed() {
                springView.onFinishFreshAndLoad();
            }
        });
    }

    private SpringView.OnFreshListener onFreshListener = new SpringView.OnFreshListener() {
        @Override
        public void onRefresh() {
            loadData(1);
        }

        @Override
        public void onLoadmore() {
            loadData(2);
        }
    };

    @Override
    public void initEvents() {
        super.initEvents();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlbumsActivity.this, AlbumsUploadPhoto.class));
            }
        });
    }

    private class AlbumsAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (-1 == viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_empty_data_view, parent, false);
                return new EmptyDataViewHolder(view);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums, parent, false);
            return new AlbumsHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof EmptyDataViewHolder) {
                holder.itemView.setVisibility(View.VISIBLE);
            } else if (holder instanceof AlbumsHolder) {
                AlbumsHolder h = (AlbumsHolder) holder;
                AlbumBean.MapsBean bean = mapsBeanList.get(position);
                h.date.setText(bean.getMoth());
                h.recyclerView.setAdapter(new AlbumsChildAdapter(h.recyclerView, bean.getAblumss()));
            }

        }

        @Override
        public int getItemCount() {
            return isEmptyData() ? 1 : mapsBeanList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isEmptyData()) return -1;
            return super.getItemViewType(position);
        }

        boolean isEmptyData() {
            return mapsBeanList.size() < 1;
        }
    }

    private class AlbumsHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public RecyclerView recyclerView;

        public AlbumsHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.item_albums_date);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.item_albums_rv);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new GridlayoutDecoration());
        }
    }

    private class AlbumsChildAdapter extends RecyclerView.Adapter {
        private RecyclerView recyclerView;
        private List<AlbumBean.MapsBean.AblumssBean> list;

        public AlbumsChildAdapter(RecyclerView recyclerView, List<AlbumBean.MapsBean.AblumssBean> list) {
            this.recyclerView = recyclerView;
            this.list = list;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums_photo, parent, false);
            return new AlbumsChildHolder(view, recyclerView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final AlbumsChildHolder h = (AlbumsChildHolder) holder;
            final AlbumBean.MapsBean.AblumssBean bean = list.get(position);
            ImageLoad.bind(h.imageView, bean.getPhotos_url());
            if (canDelete) {
                if (bean.isShowSelectBox()) {
                    h.relativeLayout.setVisibility(View.VISIBLE);
                    h.checkBox.setChecked(bean.isSelected());
                } else {
                    h.relativeLayout.setVisibility(View.GONE);
                    bean.setSelected(false);
                    bean.setShowSelectBox(false);
                    h.checkBox.setChecked(bean.isSelected());
                }

                h.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!canDelete) return false;
                        for (AlbumBean.MapsBean mapsBean : mapsBeanList) {
                            for (AlbumBean.MapsBean.AblumssBean ablumssBean : mapsBean.getAblumss()) {
                                ablumssBean.setShowSelectBox(true);
                            }
                        }
                        showDeleteDialog();
                        AlbumsActivity.this.recyclerView.getAdapter().notifyDataSetChanged();
                        return true;
                    }
                });

                h.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        h.checkBox.setChecked(!h.checkBox.isChecked());
                    }
                });

                h.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        bean.setSelected(isChecked);
                    }
                })
                ;
            }


            h.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> l = new ArrayList<String>();
                    for (AlbumBean.MapsBean.AblumssBean ablumssBean : list) {
                        l.add(ablumssBean.getPhotos_url());
                    }
                    Intent intent = new Intent(AlbumsActivity.this, WatchPictureActivity.class);
                    intent.putStringArrayListExtra("data", (ArrayList<String>) l);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class AlbumsChildHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public CheckBox checkBox;


        public AlbumsChildHolder(View itemView, RecyclerView attach) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_albums_photo_img);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_albums_photo_select_rl);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_albums_photo_select_checkbox);
            int columnCount = 1;
            if (attach != null) {
                RecyclerView.LayoutManager manager = attach.getLayoutManager();
                if (manager instanceof GridLayoutManager) {
                    columnCount = ((GridLayoutManager) manager).getSpanCount();
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    columnCount = ((StaggeredGridLayoutManager) manager).getSpanCount();
                }
            }
            if (attach != null && columnCount > 1) {
                int w = attach.getMeasuredWidth();
                itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, w / columnCount));
            }
        }
    }


    private void showDeleteDialog() {
        if (null == popupWindow) {
            View view = LayoutInflater.from(AlbumsActivity.this).inflate(R.layout.dialog_delete_album_photo, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        TextView delete = (TextView) popupWindow.getContentView().findViewById(R.id.dialog_delete_album_photo_delete);
        TextView cancel = (TextView) popupWindow.getContentView().findViewById(R.id.dialog_delete_album_photo_cancel);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLists.clear();
                for (AlbumBean.MapsBean mapsBean : mapsBeanList) {
                    for (AlbumBean.MapsBean.AblumssBean ablumssBean : mapsBean.getAblumss()) {
                        if (ablumssBean.isSelected()) {
                            deleteLists.add(ablumssBean.getAlbums_id());
                        }
                    }
                }
                if (deleteLists.size() < 1) {
                    toast.showText("请选择要删除的照片");
                    return;
                }

                DialogUtils.Factory.commonAlert(AlbumsActivity.this, "", "确定要删除这些照片吗？", "确定", "", "取消", new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if(msg == null)return false;
                        if(msg.what == 0){

                            if (null != popupWindow) popupWindow.dismiss();
                            showLoadDataDialog();
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String deleteId : deleteLists) {
                                stringBuilder.append(deleteId);
                                stringBuilder.append(",");
                            }
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("albums_id", stringBuilder.toString());
                            Req.post(Url.deleteAlbumPhoto, map, new Req.ReqCallback() {
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
                                        loadData(1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(String msg) {
                                    //toast.showText(msg);
                                    toast.showText("网络连接不可用，请稍后重试");
                                }

                                @Override
                                public void completed() {
                                    endLoadDataDialog();
                                }
                            });
                        }
                        return false;
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AlbumBean.MapsBean mapsBean : mapsBeanList) {
                    for (AlbumBean.MapsBean.AblumssBean ablumssBean : mapsBean.getAblumss()) {
                        ablumssBean.setShowSelectBox(false);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(recyclerView, Gravity.BOTTOM, 0, 0);
    }

}
