package bbc.com.moteduan_lib.ReleaseDate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.adapter.RangegvAdapter;
import bbc.com.moteduan_lib.ReleaseDate.adapter.RangelvAdapter;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.log.LogDebug;


public class DateRange extends BaseActivity implements View.OnClickListener {

    private ImageButton back;
    private Button range_confirm;
    private RelativeLayout titleLayout;
    private GridView range_gv;
    private ListView range_lv;
    private TagFlowLayout range_flowlayout;
    private ArrayList<String> lvitems;
    public final static String[] mVals = {"吃饭",  "咖啡", "K歌","电影", "运动健身", "电竞", "酒吧", "台球","高尔夫","商务饭局", "商务歌局", "商务助理"};
    private String[] mVals1 = new String[]
            {"吃饭",  "咖啡", "K歌","电影" ,"酒吧", "电竞"};
    private String[] mVals2 = new String[]
            {"运动健身",  "高尔夫","台球"};
    private String[] mVals3 = new String[]
            {"商务饭局", "商务歌局", "商务助理"};
    private LayoutInflater mInflater;
    private ArrayList<String> gvitems;
    private RangegvAdapter rangegvAdapter;
    private RangelvAdapter rangelvAdapter;


    public static final String XX= "xiuxianyule";
    public static final String YD = "yundong";
    public static final String SW = "shangwu";
    private String type = XX;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private TagAdapter<String> tagAdapter;
    private HashMap<String, Object> rangemap;
    private TextView bg_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_range);
        initView();
    }
    @Override
    public void initView() {
        rangemap = new HashMap<>();
        lvitems = new ArrayList<>();
        gvitems = new ArrayList<>();
        lvitems.add("休闲娱乐");
        lvitems.add("运动");
        lvitems.add("商务");
        back = (ImageButton) findViewById(R.id.back);
        range_confirm = (Button) findViewById(R.id.range_confirm);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        range_gv = (GridView) findViewById(R.id.range_gv);
        range_lv = (ListView) findViewById(R.id.range_lv);
        range_flowlayout = (TagFlowLayout) findViewById(R.id.range_flowlayout);
        bg_text = (TextView) findViewById(R.id.bg_text);


        rangegvAdapter = new RangegvAdapter(gvitems, DateRange.this,0);
        range_gv.setAdapter(rangegvAdapter);

        range_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("gvitems", gvitems);
                setResult(ReleaseSkill.DATERANGE, intent);
                LogDebug.err("setresult");
                finish();
            }
        });
        rangelvAdapter = new RangelvAdapter(lvitems, DateRange.this);
        range_lv.setAdapter(rangelvAdapter);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                resetlv(0);
//            }
//        }, 300);
        mInflater = LayoutInflater.from(DateRange.this);
//        range_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        type = XX;
//                        resetlv(0);
//                        mVals = mVals1;
//                        refreshFlowLayou();
//                        break;
//                    case 1:
//                        type = YD;
//                        resetlv(1);
//                        mVals = mVals2;
//                        refreshFlowLayou();
//                        break;
//                    case 2:
//                        type = SW;
//                        resetlv(2);
//                        mVals = mVals3;
//                        refreshFlowLayou();
//                        break;
//                }
//            }
//
//        });
//        mVals = mVals1;
        refreshFlowLayou();
        back.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

   /* //重置listview
    private void resetlv(int position) {
        for (int i = 0; i < rangelvAdapter.getCount(); i++) {
            RelativeLayout rl = (RelativeLayout) range_lv.getChildAt(i);
            ImageView img = (ImageView) rl.getChildAt(1);
            if (position == i) {
                img.setVisibility(View.VISIBLE);
            } else {
                img.setVisibility(View.GONE);
            }
        }
    }*/


    private void refreshFlowLayou() {
        tagAdapter = new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.range_flowteg_item, range_flowlayout, false);
                tv.setText(s);
                return tv;
            }
        };
        range_flowlayout.setAdapter(tagAdapter);

      /*  Set<Integer> selectPosSet = (Set<Integer>) rangemap.get(type);
        if (selectPosSet!=null && selectPosSet.size()>0){
            for (Integer selectint : selectPosSet) {
                LogDebug.err(selectint+"==========");
                tagAdapter.setSelectedList(selectint);
            }
        } else {
            LogDebug.err("s值为空");
        }*/


        TagFlowLayout.OnTagClickListener tagFlowListener = new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                bg_text.setVisibility(View.GONE);
                range_gv.setVisibility(View.VISIBLE);
                if (gvitems.contains(mVals[position])) {
                    gvitems.remove(mVals[position]);
                } else {
                    if (gvitems.size()<3){
                        gvitems.add(mVals[position]);
                    }
                }
                rangegvAdapter.notifyDataSetChanged();

                return true;
            }
        };
        range_flowlayout.setOnTagClickListener(tagFlowListener);
        //点击标签时的回调
        range_flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                LogDebug.err("choose:" + selectPosSet.toString());
                rangemap.put(type,selectPosSet);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        }
    }
}
