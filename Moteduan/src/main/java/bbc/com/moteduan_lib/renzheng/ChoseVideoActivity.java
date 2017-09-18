package bbc.com.moteduan_lib.renzheng;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.bean.VideoInfo;
import bbc.com.moteduan_lib.renzheng.adapter.VideoAdapter;
import bbc.com.moteduan_lib.mywidget.swipemenulistview.SwipeMenu;
import bbc.com.moteduan_lib.mywidget.swipemenulistview.SwipeMenuCreator;
import bbc.com.moteduan_lib.mywidget.swipemenulistview.SwipeMenuItem;
import bbc.com.moteduan_lib.mywidget.swipemenulistview.SwipeMenuListView;
import bbc.com.moteduan_lib.tools.DimensionConvert;
import bbc.com.moteduan_lib.tools.LoadVideoUtils;
import bbc.com.moteduan_lib.tools.UpdateMediaStore;

/**
 * Created by Administrator on 2016/5/31 0031.
 */
public class ChoseVideoActivity extends BaseTranslucentActivity {

    public static final int REQUEST_CODE = 100;

    public static boolean isNeedUpdate = false;

//    private android.widget.ListView listView;
    private SwipeMenuListView listView;
    private android.widget.TextView record;

    private  java.util.List<VideoInfo> videoList;
    private VideoAdapter adapter;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_video);
        initView();
        initData();

    }


    @Override
    public void initView() {
        record = (android.widget.TextView) ChoseVideoActivity.this.findViewById(R.id.record);
        record.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                android.content.Intent it = new android.content.Intent(ChoseVideoActivity.this,VideoRecordActivity.class);
                startActivityForResult(it,REQUEST_CODE);

            }
        });

        listView = (SwipeMenuListView) this.findViewById(R.id.videoList);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                VideoInfo video = (VideoInfo) adapter.getItem(position);
//                setResult(ReleaseActivity.VIDEO_REQUEST_CODE,
//                        new android.content.Intent().putExtra("video",video));
//                finish();
            }
        });
    }

    @Override
    public void initData() {

        new MyAsychTask().execute(0);
        videoList = new java.util.ArrayList<>();
        adapter = new VideoAdapter(videoList);
        listView.setAdapter(adapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator =
                new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem =
                        new SwipeMenuItem( ChoseVideoActivity.this.getApplicationContext());
                // set item background
                openItem.setBackground(R.color.dark_green);
                // set item width
                openItem.setWidth(DimensionConvert.dip2px(ChoseVideoActivity.this,90));
                // set item title
                openItem.setTitle("播放");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(android.graphics.Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem =
                        new SwipeMenuItem(
                        ChoseVideoActivity.this.getApplicationContext());
                // set item background
//                deleteItem.setBackground(R.color.topbar_color);
                // set item width
                deleteItem.setWidth(DimensionConvert.dip2px(ChoseVideoActivity.this,90));
                // set a icon
//                deleteItem.setIcon(com.bbc.lm.R.drawable.u1f4de);

                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(android.graphics.Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(
                new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(int position,SwipeMenu menu, int index) {

                switch (index) {
                    case 0:

                        android.content.Intent it = new android.content.Intent(
                                ChoseVideoActivity.this,
                                VideoPlay.class);
                        it.putExtra("video",videoList.get(position));
                        startActivity(it);

//                        toast.showText("播放");
                        break;
                    case 1:

                        String path = videoList.get(position).getData();
                        java.io.File file = new java.io.File(path);
                        if(file!= null && file.exists()){
                            file.delete();
                            UpdateMediaStore.updateDelete(ChoseVideoActivity.this,path);
                        }
                        videoList.remove(position);
                        adapter.notifyDataSetChanged();
//                        toast.showText("删除");
                        break;
                }

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isNeedUpdate){
            new MyAsychTask().execute(0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == ChoseVideoActivity.RESULT_OK){
            new MyAsychTask().execute(0);
        }

    }

    public void back(android.view.View view){

        finish();

    }


    class MyAsychTask extends android.os.AsyncTask<Integer,Integer,java.util.List<VideoInfo> >{

        @Override
        protected java.util.List<VideoInfo> doInBackground(Integer... params) {
           return LoadVideoUtils.getVideo(ChoseVideoActivity.this);
        }

        @Override
        protected void onPostExecute(java.util.List<VideoInfo> videoInfos) {
            super.onPostExecute(videoInfos);

            videoList.clear();
            videoList.addAll(videoInfos);
            adapter.notifyDataSetChanged();
        }
    }

}












































