package bbc.com.moteduan_lib.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;

public class MarkActivity extends BaseActivity {

    private ImageView back;
    private TextView name;
    private TextView save;
    private RelativeLayout topbanner;
    private TextView mengmengda;
    private RelativeLayout mengmengda_back;
    private TextView ganai;
    private RelativeLayout ganai_back;
    private TextView hou;
    private RelativeLayout hou_back;
    private LinearLayout linearLayout4;
    private TextView qiangpo;
    private RelativeLayout qiangpo_back;
    private TextView tuoyan;
    private RelativeLayout tuoyan_back;
    private TextView lanai;
    private RelativeLayout lanai_back;
    private LinearLayout linearLayout5;
    private TextView chihuo;
    private RelativeLayout chihuo_back;
    private TextView anjing;
    private RelativeLayout anjing_back;
    private TextView smart;
    private RelativeLayout smart_back;
    private LinearLayout linearLayout7;
    private TextView nvhanzi;
    private RelativeLayout nvhanzi_back;
    private TextView doubi;
    private RelativeLayout doubi_back;
    private TextView jiandan;
    private RelativeLayout jiandan_back;
    private LinearLayout linearLayout6;
    private TextView zhai;
    private RelativeLayout zhai_back;
    private TextView wenyifan;
    private RelativeLayout wenyifan_back;
    private TextView houdao;
    private RelativeLayout houdao_back;
    private LinearLayout linearLayout3;
    private TextView yiqi;
    private RelativeLayout yiqi_back;
    private TextView majia;
    private RelativeLayout majia_back;
    private TextView renyu;
    private RelativeLayout renyu_back;
    private LinearLayout linearLayout10;
    private TextView changfa;
    private RelativeLayout changfa_back;
    private TextView rexue;
    private RelativeLayout rexue_back;
    private TextView panni;
    private RelativeLayout panni_back;
    private LinearLayout linearLayout13;
    private ImageView add_iv;
    private RelativeLayout add_more_mark;
    private TextView luoli;
    private RelativeLayout luoli_back;
    private TextView suixing;
    private RelativeLayout suixing_back;
    private LinearLayout linearLayout11;
    private TextView dashu;
    private RelativeLayout dashu_back;
    private TextView jiantan;
    private RelativeLayout jiantan_back;
    private TextView nvyou;
    private RelativeLayout nvyou_back;
    private LinearLayout linearLayout9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initView();
        initData();
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        save = (TextView) findViewById(R.id.save);
        topbanner = (RelativeLayout) findViewById(R.id.topbanner);

        mengmengda = (TextView) findViewById(R.id.mengmengda);
        mengmengda_back = (RelativeLayout) findViewById(R.id.mengmengda_back);
        ganai = (TextView) findViewById(R.id.ganai);
        ganai_back = (RelativeLayout) findViewById(R.id.ganai_back);
        hou = (TextView) findViewById(R.id.hou);
        hou_back = (RelativeLayout) findViewById(R.id.hou_back);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        qiangpo = (TextView) findViewById(R.id.qiangpo);
        qiangpo_back = (RelativeLayout) findViewById(R.id.qiangpo_back);
        tuoyan = (TextView) findViewById(R.id.tuoyan);
        tuoyan_back = (RelativeLayout) findViewById(R.id.tuoyan_back);
        lanai = (TextView) findViewById(R.id.lanai);
        lanai_back = (RelativeLayout) findViewById(R.id.lanai_back);
        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        chihuo = (TextView) findViewById(R.id.chihuo);
        chihuo_back = (RelativeLayout) findViewById(R.id.chihuo_back);
        anjing = (TextView) findViewById(R.id.anjing);
        anjing_back = (RelativeLayout) findViewById(R.id.anjing_back);
        smart = (TextView) findViewById(R.id.smart);
        smart_back = (RelativeLayout) findViewById(R.id.smart_back);
        linearLayout7 = (LinearLayout) findViewById(R.id.linearLayout7);
        nvhanzi = (TextView) findViewById(R.id.nvhanzi);
        nvhanzi_back = (RelativeLayout) findViewById(R.id.nvhanzi_back);
        doubi = (TextView) findViewById(R.id.doubi);
        doubi_back = (RelativeLayout) findViewById(R.id.doubi_back);
        jiandan = (TextView) findViewById(R.id.jiandan);
        jiandan_back = (RelativeLayout) findViewById(R.id.jiandan_back);
        linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
        zhai = (TextView) findViewById(R.id.zhai);
        zhai_back = (RelativeLayout) findViewById(R.id.zhai_back);
        wenyifan = (TextView) findViewById(R.id.wenyifan);
        wenyifan_back = (RelativeLayout) findViewById(R.id.wenyifan_back);
        houdao = (TextView) findViewById(R.id.houdao);
        houdao_back = (RelativeLayout) findViewById(R.id.houdao_back);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        yiqi = (TextView) findViewById(R.id.yiqi);
        yiqi_back = (RelativeLayout) findViewById(R.id.yiqi_back);
        majia = (TextView) findViewById(R.id.majia);
        majia_back = (RelativeLayout) findViewById(R.id.majia_back);
        renyu = (TextView) findViewById(R.id.renyu);
        renyu_back = (RelativeLayout) findViewById(R.id.renyu_back);
        linearLayout10 = (LinearLayout) findViewById(R.id.linearLayout10);
        changfa = (TextView) findViewById(R.id.changfa);
        changfa_back = (RelativeLayout) findViewById(R.id.changfa_back);
        rexue = (TextView) findViewById(R.id.rexue);
        rexue_back = (RelativeLayout) findViewById(R.id.rexue_back);
        panni = (TextView) findViewById(R.id.panni);
        panni_back = (RelativeLayout) findViewById(R.id.panni_back);
        linearLayout13 = (LinearLayout) findViewById(R.id.linearLayout13);
        add_iv = (ImageView) findViewById(R.id.add_iv);
        add_more_mark = (RelativeLayout) findViewById(R.id.add_more_mark);
        luoli = (TextView) findViewById(R.id.luoli);
        luoli_back = (RelativeLayout) findViewById(R.id.luoli_back);
        suixing = (TextView) findViewById(R.id.suixing);
        suixing_back = (RelativeLayout) findViewById(R.id.suixing_back);
        linearLayout11 = (LinearLayout) findViewById(R.id.linearLayout11);
        dashu = (TextView) findViewById(R.id.dashu);
        dashu_back = (RelativeLayout) findViewById(R.id.dashu_back);
        jiantan = (TextView) findViewById(R.id.jiantan);
        jiantan_back = (RelativeLayout) findViewById(R.id.jiantan_back);
        nvyou = (TextView) findViewById(R.id.nvyou);
        nvyou_back = (RelativeLayout) findViewById(R.id.nvyou_back);
        linearLayout9 = (LinearLayout) findViewById(R.id.linearLayout9);
    }

    @Override
    public void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mengmengda_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
