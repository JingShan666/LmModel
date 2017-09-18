package bbc.com.moteduan_lib.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.log.LogDebug;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;
import bbc.com.moteduan_lib.mywidget.DialogProgress;
import bbc.com.moteduan_lib.mywidget.XCFlowLayout;
import bbc.com.moteduan_lib.network.Req;

import com.liemo.shareresource.Url;

import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib.renzheng.Auth;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib2.InviteTypeActivity;
import bbc.com.moteduan_lib2.bean.InviteNavigationBean;
import bbc.com.moteduan_lib2.bean.InviteTypeBean;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PermissionUtils;
import wei.toolkit.utils.PictureUtil;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private Dialog dialog;
    private View ageview;
    private RelativeLayout agelayout;
    private int agevalue;
    private NumberPicker agepicker;
    private static final String TAG = "SKYWANG";

    private XCFlowLayout mFlowLayout;

    private SeekBar height;
    private SeekBar weight;
    private SeekBar brest;
    private SeekBar middle;
    private SeekBar buttock;

    private int minheight = 160;
    private int minweight;
    private int minbrest;
    private int minmiddle;
    private int minbuttock;

    private TextView height_tv;
    private TextView weight_tv;
    private TextView brest_tv;
    private TextView middle_tv;
    private TextView buttock_tv;


    private TextView age_tv;
    private ImageView back;
    private TextView name;
    private TextView save;
    private RelativeLayout topbanner;
    private RelativeLayout icon_change_layout;
    private RelativeLayout view2;
    private RelativeLayout nicknamelayout;
    private RelativeLayout view3;
    private RelativeLayout age_layout;
    private RelativeLayout view4;
    private TextView height_edit_tv;
    private TextView height_sb_tv;
    private SeekBar height_sb;
    private RelativeLayout heightlayout;
    private RelativeLayout view8;
    private TextView weight_sb_tv;
    private SeekBar weight_sb;
    private RelativeLayout weightlayout;
    private TextView measurements;
    private TextView textView18;
    private SeekBar brest_sb;
    private SeekBar middle_sb;
    private SeekBar buttock_sb;
    private RelativeLayout relativeLayout17;
    private RelativeLayout view5;
    private LinearLayout parentlayout;
    private RelativeLayout phonebinglayout;
    private TextView phonenumber_edit;
    private CircleImageBorderView edit_icon;
    private String path;
    private String agestring;
    private String age;
    private int heightint;
    private String weightstring;
    private String buststring;
    private String waiststring;
    private String hipstring;

    private final int CAMERACODE = 23;
    private EditText edit_name;
    private String heightstring;
    private String picPath = null;
    private DialogProgress dialogProgress;
    private Dialog dialog1;
    private LoginBean mLoginBean;
    private Switch mDrinkSwitch;
    private TextView mEntryInviteSelect;
    private static final int REQUESR_CODE_INVITE_SELECT = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    private static final int REQUEST_CODE_ALBUM = 3;
    private static final int REQUEST_CODE_CROP = 4;
    private static final int REQUEST_CODE_PERMISSION = 5;
    private List<InviteTypeBean> mInviteTypes = new ArrayList<>();
    private RecyclerView mInviteTypeRv;
    private boolean hasNewPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mLoginBean = SpDataCache.getSelfInfo(EditActivity.this);
        if (mLoginBean == null) {
            toast.showText("您还未登录");
            finish();
            return;
        }
        initView();
        init();
        initData();
    }


    public void init() {
        edit_icon = (CircleImageBorderView) findViewById(R.id.edit_icon);
        phonenumber_edit = (TextView) findViewById(R.id.phonenumber_edit);
        if (getIntent() != null) {
            String phonenumber = getIntent().getStringExtra("phonenumber");
            phonenumber_edit.setText(phonenumber);
        }
        phonebinglayout = (RelativeLayout) findViewById(R.id.phonebinglayout);
        age_tv = (TextView) findViewById(R.id.age_tv);

        height_tv = (TextView) findViewById(R.id.height_sb_tv);
        weight_tv = (TextView) findViewById(R.id.weight_sb_tv);
        brest_tv = (TextView) findViewById(R.id.brest_tv);
        middle_tv = (TextView) findViewById(R.id.middle_tv);
        buttock_tv = (TextView) findViewById(R.id.buttock_tv);
        height = (SeekBar) findViewById(R.id.height_sb);
        weight = (SeekBar) findViewById(R.id.weight_sb);
        brest = (SeekBar) findViewById(R.id.brest_sb);
        middle = (SeekBar) findViewById(R.id.middle_sb);
        buttock = (SeekBar) findViewById(R.id.buttock_sb);

        agelayout = (RelativeLayout) findViewById(R.id.age_layout);

        mFlowLayout = (XCFlowLayout) findViewById(R.id.flowlayout);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_name.setCursorVisible(false);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setCursorVisible(true);
            }
        });


    }

    @Override
    public void initData() {
        View rootview = View.inflate(this, R.layout.activity_edit, null);
        dialogProgress = new DialogProgress(rootview, this);
        //初始化个人信息
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", mLoginBean.getData().getM_id());
        Req.post(Url.obtainInviteType, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                Loger.log(EditActivity.class.getSimpleName(), "inviteType = " + result);
                InviteNavigationBean bean = new Gson().fromJson(result, InviteNavigationBean.class);
                if (!"200".equals(bean.getCode())) {
                    return;
                }
                List<InviteNavigationBean.NavigationBean> list = bean.getNavigation();
                if (list == null || list.size() < 1) {
                    return;
                }
                mInviteTypes.clear();
                for (InviteNavigationBean.NavigationBean navigationBean : list) {
                    mInviteTypes.add(new InviteTypeBean(navigationBean.getSmall_navigation(), navigationBean.getSmall_of_navigation() + "", false));
                }
                mInviteTypeRv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void failed(String msg) {
                //toast.showText(msg);
                toast.showText("网络连接不可用，请稍后重试");
            }

            @Override
            public void completed() {

            }
        });

        ImageLoad.bind(edit_icon, mLoginBean.getData().getM_head_photo());
        edit_name.setText(mLoginBean.getData().getM_nick_name());

        if (mLoginBean.getData().getM_work_state() == 0) {
            mDrinkSwitch.setChecked(false);
        } else {
            mDrinkSwitch.setChecked(true);
        }

        int m_age = mLoginBean.getData().getM_age();
        if (m_age == 0) {
            age_tv.setText(22 + "");
        } else {
            age_tv.setText(m_age + "");
        }
        int m_tall = mLoginBean.getData().getM_tall();
        if (m_tall == 0) {
            height_sb_tv.setText(160 + "");
        } else {
            height_sb_tv.setText(m_tall + "");
        }

        int m_weight = mLoginBean.getData().getM_weight();
        if (m_weight == 0) {
            weight_sb_tv.setText(40 + "");
        } else {
            weight_sb_tv.setText(m_weight + "");
        }

        int m_bust = mLoginBean.getData().getM_bust();
        if (m_bust == 0) {
            brest_tv.setText(75 + "");
        } else {
            brest_tv.setText(m_bust + "");
        }

        int m_waist = mLoginBean.getData().getM_waist();
        if (m_waist == 0) {
            middle_tv.setText(50 + "");
        } else {
            middle_tv.setText(m_waist + "");
        }

        int m_hips = mLoginBean.getData().getM_hips();
        if (m_hips == 0) {
            buttock_tv.setText(75 + "");
        } else {
            buttock_tv.setText(m_hips + "");
        }

        buststring = brest_tv.getText().toString();
        waiststring = middle_tv.getText().toString();
        hipstring = buttock_tv.getText().toString();
        weightstring = weight_sb_tv.getText().toString();
        heightstring = height_sb_tv.getText().toString();

        int heighInt = Integer.parseInt(heightstring);
        int weightInt = Integer.parseInt(weightstring);
        int hipsInt = Integer.parseInt(hipstring);
        int waistInt = Integer.parseInt(waiststring);
        int bustInt = Integer.parseInt(buststring);

        height.setProgress(heighInt - 160);
        weight.setProgress(weightInt - 40);
        brest.setProgress(bustInt - 75);
        middle.setProgress(waistInt - 50);
        buttock.setProgress(hipsInt - 75);


        // agestring = age_tv.getText().toString();
        //age = Integer.parseInt("agestring");

        phonebinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditActivity.this, PhoneBingActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInviteTypes.size() < 3) {
                    toast.showText("请选择邀约类型,最少3个最多6个");
                    return;
                }


                String name = edit_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    toast.showText("昵称不能为空");
                    return;
                }

                age = age_tv.getText().toString().trim();
                if (TextUtils.isEmpty(age)) {
                    toast.showText("请选择你的年龄");
                    return;
                }

                buststring = brest_tv.getText().toString();
                waiststring = middle_tv.getText().toString();
                hipstring = buttock_tv.getText().toString();
                weightstring = weight_sb_tv.getText().toString();
                heightstring = height_sb_tv.getText().toString();

                dialogProgress.setJiazai_text("资料修改中...");
                dialogProgress.show();
                Map<String, Object> map = new HashMap<String, Object>();
                if (hasNewPortrait) {
                    try {
                        PutObjectResult putObjectResult = upLoadImgOss(picPath);
                        String eTag = putObjectResult.getETag();
                        map.put("m_head_photo", eTag);
                    } catch (ClientException | ServiceException e) {
                        dialogProgress.dismiss();
                        toast.showText("网络不畅通,头像上传失败");
                        return;
                    }
                } else {
                    map.put("m_head_photo", mLoginBean.getData().getM_head_photo());
                }
                map.put("member_id", mLoginBean.getData().getM_id());
                map.put("m_hips", hipstring);
                map.put("m_nick_name", name);
                map.put("m_age", age);
                map.put("m_tall", heightstring);
                map.put("m_weight", weightstring);
                map.put("m_bust", buststring);
                map.put("m_waist", waiststring);
                map.put("m_work_state", mDrinkSwitch.isChecked() ? 1 : 0);
                StringBuilder stringBuilder = new StringBuilder();
                for (InviteTypeBean bean : mInviteTypes) {
                    stringBuilder.append(bean.getId());
                    stringBuilder.append(",");
                }
                String navigation_id = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
                map.put("navigation_id", navigation_id);
                Req.post(Url.personageEditData, map, new Req.ReqCallback() {
                    @Override
                    public void success(String result) {
                        Loger.log(EditActivity.class.getSimpleName(), result);
                        dialogProgress.dismiss();
                        LoginBean loginbean = new Gson().fromJson(result, LoginBean.class);
                        if (!"200".equals(loginbean.getCode())) {
                            toast.showText(loginbean.getTips());
                            return;
                        }
                        SpDataCache.saveSelfInfo(loginbean);
                        toast.showText("保存成功");
                        finish();
                    }

                    @Override
                    public void failed(String msg) {
                        dialogProgress.dismiss();
                        toast.showText("网络不畅通,数据上传失败");
                    }

                    @Override
                    public void completed() {
                    }
                });

            }
        });

        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minheight = 160 + i;
                height_tv.setText(minheight + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minweight = 40 + i;
                weight_tv.setText(minweight + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        brest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minbrest = 75 + i;
                brest_tv.setText(minbrest + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        middle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minmiddle = 50 + i;
                middle_tv.setText(minmiddle + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttock.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minbuttock = 75 + i;
                buttock_tv.setText(minbuttock + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        agelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseAge();

            }
        });

        mEntryInviteSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditActivity.this, InviteTypeActivity.class)
                        .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) mInviteTypes), REQUESR_CODE_INVITE_SELECT);
            }
        });

    }

    private void openDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.cancellation_window, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText("您的个人信息符合专职模特标准，认证专职模特可以赚取好多模币~");
        title.setTextSize(12);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        dialog1 = builder.create();
        dialog1.show();
        Button exitAccount = (Button) view.findViewById(R.id.exitAccount);
        exitAccount.setText("去认证");
        exitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(EditActivity.this, Auth.class);
                intent3.putExtra("from", "Authentication");
                intent3.putExtra("type", Auth.MODEL_FLAG);
                startActivity(intent3);
                dialog1.dismiss();
                finish();
            }
        });

        Button close = (Button) view.findViewById(R.id.close);
        close.setText("暂不认证");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
//设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.transparent)));
// pf.set(picker, new Div)
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    /**
     * 选择图片
     *
     * @param view
     */
    public void show(View view) {
        new android.support.v7.app.AlertDialog.Builder(EditActivity.this)
                .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                PermissionUtils.checkPermission(EditActivity.this, new String[]{Manifest.permission.CAMERA}, new PermissionUtils.PermissionCallback() {
                                    @Override
                                    public void onGranted() {
                                        PictureUtil.startSystemCamera(EditActivity.this,
                                                Uri.fromFile(new File(picPath = PictureUtil.getPicSaveAddress() + "/" + System.currentTimeMillis() + ".jpg")), REQUEST_CODE_CAMERA);
                                    }

                                    @Override
                                    public void onDenied(String[] deniedName) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(deniedName, REQUEST_CODE_PERMISSION);
                                        } else {
                                            Toast.makeText(EditActivity.this, "您拒绝了相机权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                break;
                            case 1:
                                PictureUtil.startSystemAlbum(EditActivity.this, REQUEST_CODE_ALBUM);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 上传头像到oss
     */
    private PutObjectResult upLoadImgOss(final String path) throws ClientException, ServiceException {
        PutObjectResult result = OSSManager.getInstance().synchUpLoad(path, Constants.IMAGE_TYPE, new OSSProgressCallback() {
            @Override
            public void onProgress(Object o, long l, long l1) {
                LogDebug.print(path + " : " + (float) l / l1 * 100 + " %");
            }
        });
        return result;
    }


    @Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
    }

    public void choseAge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ageview = LayoutInflater.from(this).inflate(R.layout.age_layout, null);
        agepicker = (NumberPicker) ageview.findViewById(R.id.age_np);
        setNumberPickerDividerColor(agepicker);
        builder.setView(ageview);
        dialog = builder.create();
        dialog.show();
        agevalue = 22;
        agepicker.setMinValue(18);//设置最小值
        agepicker.setMaxValue(35);//设置最大值
        agepicker.setValue(agevalue);//设置当前值

        agepicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {

                agevalue = Integer.parseInt(String.valueOf(newVal));
            }

        });

        (ageview.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age_tv.setText(agevalue + "");
                dialog.dismiss();
            }
        });
        (ageview.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        save = (TextView) findViewById(R.id.save);
        topbanner = (RelativeLayout) findViewById(R.id.topbanner);
        icon_change_layout = (RelativeLayout) findViewById(R.id.icon_change_layout);
        nicknamelayout = (RelativeLayout) findViewById(R.id.nicknamelayout);
        age_layout = (RelativeLayout) findViewById(R.id.age_layout);
        height_edit_tv = (TextView) findViewById(R.id.height_edit_tv);
        height_sb_tv = (TextView) findViewById(R.id.height_sb_tv);
        height_sb = (SeekBar) findViewById(R.id.height_sb);
        heightlayout = (RelativeLayout) findViewById(R.id.heightlayout);
        weight_sb_tv = (TextView) findViewById(R.id.weight_sb_tv);
        weight_sb = (SeekBar) findViewById(R.id.weight_sb);
        weightlayout = (RelativeLayout) findViewById(R.id.weightlayout);
        measurements = (TextView) findViewById(R.id.measurements);
        textView18 = (TextView) findViewById(R.id.textView18);
        brest_sb = (SeekBar) findViewById(R.id.brest_sb);
        middle_sb = (SeekBar) findViewById(R.id.middle_sb);
        buttock_sb = (SeekBar) findViewById(R.id.buttock_sb);
        relativeLayout17 = (RelativeLayout) findViewById(R.id.relativeLayout17);
        view5 = (RelativeLayout) findViewById(R.id.view5);
        parentlayout = (LinearLayout) findViewById(R.id.parentlayout);
        mDrinkSwitch = (Switch) findViewById(R.id.activity_edit_drink_switch);
        mEntryInviteSelect = (TextView) findViewById(R.id.activity_edit_invite_type_entry);
        mInviteTypeRv = (RecyclerView) findViewById(R.id.activity_edit_invite_type_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mInviteTypeRv.setLayoutManager(manager);
        mInviteTypeRv.addItemDecoration(new GridlayoutDecoration());
        mInviteTypeRv.setAdapter(new InviteTypeAdapter());

    }

    private class InviteTypeAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_type_stroke, parent, false);
            return new InviteTypeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            InviteTypeHolder h = (InviteTypeHolder) holder;
            InviteTypeBean bean = mInviteTypes.get(position);
            h.label.setText(bean.getLabel());
        }

        @Override
        public int getItemCount() {
            return mInviteTypes.size();
        }
    }

    private class InviteTypeHolder extends RecyclerView.ViewHolder {
        public TextView label;

        public InviteTypeHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.item_invite_type_stroke_label);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditActivity.this, "您拒绝了相机权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            PictureUtil.startSystemCamera(EditActivity.this,
                    Uri.fromFile(new File(picPath = PictureUtil.getPicSaveAddress() + "/" + System.currentTimeMillis() + ".jpg")), REQUEST_CODE_CAMERA);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUESR_CODE_INVITE_SELECT) {
            if (data == null) return;
            List<InviteTypeBean> list = data.getParcelableArrayListExtra("data");
            if (list == null) return;
            mInviteTypes.clear();
            mInviteTypes.addAll(list);
            mInviteTypeRv.getAdapter().notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            LogDebug.log(TAG, "REQUEST_CODE_CAMERA picPath = " + picPath);
            PictureUtil.startSystemPhotoZoom(EditActivity.this, Uri.fromFile(new File(picPath)), REQUEST_CODE_CROP);
        } else if (requestCode == REQUEST_CODE_ALBUM) {
            if (data.getData() != null) {
                Loger.log(TAG, "REQUEST_CODE_ALBUM uri = " + data.getData() + " path=" + data.getData().getPath());
                if (data.getData().toString().startsWith("file:")) {
                    picPath = data.getData().getPath();
                    PictureUtil.startSystemPhotoZoom(EditActivity.this, data.getData(), REQUEST_CODE_CROP);
                } else if (data.getData().toString().startsWith("content:")) {
                    Cursor c = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        picPath = c.getString(0);
                        Loger.log(TAG, "REQUEST_CODE_ALBUM path = " + picPath);
                        c.close();
                        PictureUtil.startSystemPhotoZoom(EditActivity.this, Uri.fromFile(new File(picPath)), REQUEST_CODE_CROP);
//                        PictureUtil.startSystemPhotoZoom(EditActivity.this, FileProvider.getUriForFile(EditActivity.this,"bbc.com.moteduan.FileProvider",new File(picPath)), REQUEST_CODE_CROP);
                    }
                }
            } else {
                Loger.log(TAG, "REQUEST_CODE_ALBUM uri = null");
            }


        } else if (requestCode == REQUEST_CODE_CROP) {
            if (data == null) return;
            Bitmap bitmap = data.getParcelableExtra("data");
            if (bitmap == null) return;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            try {
                baos.writeTo(new FileOutputStream(picPath = PictureUtil.getPicSaveAddress() + "/" + System.currentTimeMillis() + "_crop.jpg"));
                baos.close();
                ImageLoad.bind(edit_icon, picPath);
                hasNewPortrait = true;
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.show(EditActivity.this,"图片剪切失败");
            }
        } else {
            Loger.log(TAG, "onActivityResult is default");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
