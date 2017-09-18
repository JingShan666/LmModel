package bbc.com.moteduan_lib2.login;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.Req;
import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib2.InviteTypeActivity;
import bbc.com.moteduan_lib2.bean.InviteTypeBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PermissionUtils;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;

public class CompleteSelfInfoActivity extends BaseTranslucentActivity implements View.OnClickListener {
    public static final String TAG = "CompleteSelfInfoActivity";
    private CircleImageBorderView edit_headPhoto;
    private TextView edit_name_tv;
    private RelativeLayout edit_name_ll;
    private TextView edit_name_ll_line;
    private TextView edit_sex_tv;
    private TextView edit_sex_man;
    private TextView edit_sex_woman;
    private TextView edit_sex_ll_line;
    private RelativeLayout edit_age_rl;
    private TextView edit_age_ll_line;
    private RelativeLayout edit_profession_rl;
    private TextView edit_profession_ll_line;
    private RelativeLayout edit_hight_weight_rl;
    private TextView edit_hight_weight_ll_line;
    private TextView completeEdit;
    private EditText inviteCode;

    private TextView age;
    private TextView profession;
    private TextView heightAndweight;
    private TextView headTitle;
    private EditText edit_name;
    private LoginBean userInfo;
    private int sexTag = 1;
    private ArrayList<Integer> smallofnavigationList;

    private RelativeLayout mSanWeiRl;
    private TextView mSanweiTv;
    private RecyclerView mInviteTypeRv;
    private TextView mInviteTypeEntry;
    private List<InviteTypeBean> mInviteTypes = new ArrayList<>();
    private static final int REQUESR_CODE_INVITE_SELECT = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    private static final int REQUEST_CODE_ALBUM = 3;
    private static final int REQUEST_CODE_CROP = 4;
    private static final int REQUEST_CODE_PERMISSION = 5;
    private List<String> mProfessions = new ArrayList<>();
    private Map<String, String> professionMap = new HashMap<>();
    private String photoUrl = "";
    private String mlocalPhotoUrl = "";
    private boolean hasNewPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_self_info);
        initView();
        initData();
    }

    @Override
    public void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        headTitle = (TextView) findViewById(R.id.head_title);
        headTitle.setText("完善信息");

        mSanWeiRl = (RelativeLayout) findViewById(R.id.edit_sanwei_rl);
        mSanWeiRl.setOnClickListener(this);
        mSanweiTv = (TextView) findViewById(R.id.activity_complete_info_sanwei);
        edit_headPhoto = (CircleImageBorderView) findViewById(R.id.edit_headPhoto);
        edit_headPhoto.setOnClickListener(this);
        edit_name_tv = (TextView) findViewById(R.id.edit_name_tv);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_name_ll = (RelativeLayout) findViewById(R.id.edit_name_ll);
        edit_name_ll.setOnClickListener(this);
        edit_name_ll_line = (TextView) findViewById(R.id.edit_name_ll_line);
        edit_sex_tv = (TextView) findViewById(R.id.edit_sex_tv);
        edit_sex_man = (TextView) findViewById(R.id.edit_sex_man);
        edit_sex_man.setOnClickListener(this);
        edit_sex_woman = (TextView) findViewById(R.id.edit_sex_woman);
        edit_sex_woman.setOnClickListener(this);
        edit_sex_ll_line = (TextView) findViewById(R.id.edit_sex_ll_line);
        edit_sex_ll_line.setOnClickListener(this);
        edit_age_rl = (RelativeLayout) findViewById(R.id.edit_age_rl);
        edit_age_rl.setOnClickListener(this);
        edit_age_ll_line = (TextView) findViewById(R.id.edit_age_ll_line);
        edit_age_ll_line.setOnClickListener(this);
        edit_profession_rl = (RelativeLayout) findViewById(R.id.edit_profession_rl);
        edit_profession_rl.setOnClickListener(this);
        edit_profession_ll_line = (TextView) findViewById(R.id.edit_profession_ll_line);
        edit_profession_ll_line.setOnClickListener(this);
        edit_hight_weight_rl = (RelativeLayout) findViewById(R.id.edit_hight_weight_rl);
        edit_hight_weight_rl.setOnClickListener(this);
        edit_hight_weight_ll_line = (TextView) findViewById(R.id.edit_hight_weight_ll_line);
        edit_hight_weight_ll_line.setOnClickListener(this);
        completeEdit = (TextView) findViewById(R.id.completeEdit);
        completeEdit.setOnClickListener(this);
        age = (TextView) findViewById(R.id.age);
        profession = (TextView) findViewById(R.id.profession);
        heightAndweight = (TextView) findViewById(R.id.hightAndweight);
        inviteCode = (EditText) findViewById(R.id.edit_invite_code);

        mInviteTypeRv = (RecyclerView) findViewById(R.id.activity_complete_invite_type_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mInviteTypeRv.setLayoutManager(manager);
        mInviteTypeRv.addItemDecoration(new GridlayoutDecoration());
        mInviteTypeRv.setAdapter(new InviteTypeAdapter());
        mInviteTypeEntry = (TextView) findViewById(R.id.activity_complete_invite_type_entry);
        mInviteTypeEntry.setOnClickListener(this);
    }

    @Override
    public void initData() {
        userInfo = getIntent().getParcelableExtra("data");
        if (userInfo == null) {
            ToastUtil.show(this, "用户信息为空.");
            finish();
            return;
        }
        String u_head_photo = userInfo.getData().getM_head_photo();
        if (TextUtils.isEmpty(u_head_photo)) {
            edit_headPhoto.setImageResource(R.drawable.appicon);
        } else {
            ImageLoad.bind(edit_headPhoto, u_head_photo);
        }
        String u_nick_name = userInfo.getData().getM_nick_name();
        if (!TextUtils.isEmpty(u_nick_name)) {
            edit_name.setText(u_nick_name);
            edit_name.setSelection(u_nick_name.length());
        }
    }

    /**
     * 后台返回的模特职业类型，供用户选择
     */
    private void requestUserType() {
        Req.post(Url.professionList, null, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if (!"200".equals(code)) {
                        ToastUtil.show(CompleteSelfInfoActivity.this, tips);
                        return;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("navigation");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if ("全部".equals(object.getString("small_navigation"))) {
                            continue;
                        }
                        mProfessions.add(object.getString("small_navigation"));
                        professionMap.put(object.getString("small_navigation"), object.getString("small_of_navigation"));
                    }
                    choseProfession();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }

            @Override
            public void completed() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();
        } else if (i == R.id.edit_profession_rl) {
            if (mProfessions.size() < 1) {
                requestUserType();
            } else {
                choseProfession();
            }
        } else if (i == R.id.edit_age_rl) {
            choseAge();

        } else if (i == R.id.edit_hight_weight_rl) {
            chosehightAndweight();

        } else if (i == R.id.edit_sanwei_rl) {
            DialogUtils.sanweiChooseDialog(CompleteSelfInfoActivity.this, new DialogUtils.SanweiChooseDialogCallBack() {
                @Override
                public void result(String[] result) {
                    mSanweiTv.setText(result[0] + "/" + result[1] + "/" + result[2]);
                }
            });
        } else if (i == R.id.edit_sex_man) {
            sexTag = 1;
            edit_sex_man.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_prenv, 0, 0, 0);
            edit_sex_woman.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_nan, 0, 0, 0);

        } else if (i == R.id.edit_sex_woman) {
            sexTag = 2;
            edit_sex_woman.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_prenv, 0, 0, 0);
            edit_sex_man.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_nan, 0, 0, 0);

        } else if (i == R.id.completeEdit) {
            updateSelfInfo();

        } else if (i == R.id.activity_complete_invite_type_entry) {
            startActivityForResult(new Intent(CompleteSelfInfoActivity.this, InviteTypeActivity.class)
                    .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) mInviteTypes), REQUESR_CODE_INVITE_SELECT);
        } else if (i == R.id.edit_headPhoto) {
            new AlertDialog.Builder(CompleteSelfInfoActivity.this)
                    .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:
                                    PermissionUtils.checkPermission(CompleteSelfInfoActivity.this, new String[]{Manifest.permission.CAMERA}, new PermissionUtils.PermissionCallback() {
                                        @Override
                                        public void onGranted() {
                                            PictureUtil.startSystemCamera(CompleteSelfInfoActivity.this,
                                                    Uri.fromFile(new File(mlocalPhotoUrl = PictureUtil.getPicSaveAddress() + "/" + System.currentTimeMillis()+".jpg")), REQUEST_CODE_CAMERA);
                                        }

                                        @Override
                                        public void onDenied(String[] deniedName) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(deniedName, REQUEST_CODE_PERMISSION);
                                            } else {
                                                Toast.makeText(CompleteSelfInfoActivity.this, "您拒绝了相机权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    break;
                                case 1:
                                    PictureUtil.startSystemAlbum(CompleteSelfInfoActivity.this, REQUEST_CODE_ALBUM);
                                    break;

                                default:
                                    break;
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CompleteSelfInfoActivity.this, "您拒绝了相机权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            PictureUtil.startSystemCamera(CompleteSelfInfoActivity.this,
                    Uri.fromFile(new File(mlocalPhotoUrl = PictureUtil.getPicSaveAddress() + "/" + System.currentTimeMillis()+".jpg")), REQUEST_CODE_CAMERA);

        }
    }

    /**
     * 更新个人资料
     */
    private void updateSelfInfo() {

        if (!hasNewPortrait) {
            toast.showText("请选择新的头像");
            return;
        }

        final String editNameString = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(editNameString)) {
            toast.showText("昵称不能为空");
            return;
        }

        if (editNameString.length() > 6) {
            toast.showText("昵称长度不能超过6个字符");
            return;
        }

        final String ageString = age.getText().toString().trim();
        if (TextUtils.isEmpty(ageString)) {
            toast.showText("请选择您的年龄");
            return;
        }
        final String professiontext = profession.getText().toString().trim();
        if (TextUtils.isEmpty(professiontext)) {
            toast.showText("请选择您的职业");
            return;
        }
        final String weightValue = heightAndweight.getText().toString().trim();
        if (TextUtils.isEmpty(weightValue)) {
            toast.showText("请选择您的体重");
            return;
        }
        final String sanwei = mSanweiTv.getText().toString().trim();
        if (TextUtils.isEmpty(sanwei)) {
            toast.showText("请选择您的三围");
            return;
        }

        if (mInviteTypes.size() < 3) {
            toast.showText("请选择邀约类型,最少3个最多6个");
            return;
        }
        showLoadDataDialog();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) {
                OSSManager ossManager = OSSManager.getInstance();
                PutObjectResult result = null;
                try {
                    result = ossManager.synchUpLoad(PictureUtil.pictureCompress(mlocalPhotoUrl), OSSManager.IMAGE_TYPE, null);
                    photoUrl = result.getETag();
                    HashMap<String, Object> selfInfoHashMap = new HashMap<>();
                    selfInfoHashMap.put("member_id", userInfo.getData().getM_id());
                    selfInfoHashMap.put("head_photo", photoUrl);
//        selfInfoHashMap.put("u_cover", photoUrl);
                    selfInfoHashMap.put("nick_name", editNameString);
                    selfInfoHashMap.put("m_sex", sexTag + "");
                    selfInfoHashMap.put("m_age", ageString);
                    selfInfoHashMap.put("identity", professionMap.get(professiontext));
                    selfInfoHashMap.put("weight_age", weightValue.replaceAll("c|m|k|g", ""));
                    selfInfoHashMap.put("dimensional", sanwei);
                    selfInfoHashMap.put("invitation_code", inviteCode.getText().toString().trim());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (InviteTypeBean bean : mInviteTypes) {
                        stringBuilder.append(bean.getId());
                        stringBuilder.append(",");
                    }
                    String navigation_id = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
                    selfInfoHashMap.put("navigation_id", navigation_id);
                    Req.post(Url.editData, selfInfoHashMap, new Req.ReqCallback() {
                        @Override
                        public void success(String result) {
                            e.onNext(result);
                        }

                        @Override
                        public void failed(String msg) {
                            e.onError(new Throwable());
                        }

                        @Override
                        public void completed() {

                        }
                    });
                } catch (ClientException e1) {
                    e1.printStackTrace();
                    e.onError(new Throwable());
                } catch (ServiceException e1) {
                    e1.printStackTrace();
                    e.onError(new Throwable());
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        endLoadDataDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            String code = jsonObject.getString("code");
                            String tips = jsonObject.getString("tips");
                            if (!"200".equals(code)) {
                                ToastUtil.show(CompleteSelfInfoActivity.this, tips);
                                return;
                            }
                            LoginBean loginBean = new Gson().fromJson(value, LoginBean.class);
                            SpDataCache.saveSelfInfo(loginBean);
                            Intent intent = new Intent();
                            intent.setAction("com.lm.loginedNotificationReceiver");
                            sendBroadcast(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        endLoadDataDialog();
                        ToastUtil.show(CompleteSelfInfoActivity.this, "网络不畅通,发布失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 选择身高体重
     */
    private void chosehightAndweight() {
        View choseheightweightView = LayoutInflater.from(this).inflate(R.layout.chose_height_weight_layout, null);
        final NumberPicker heightNp = (NumberPicker) choseheightweightView.findViewById(R.id.height_np);
        heightNp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        heightNp.setMinValue(160);
        heightNp.setMaxValue(190);
        heightNp.setValue(175);

        final NumberPicker weightNp = (NumberPicker) choseheightweightView.findViewById(R.id.weight_np);
        weightNp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        weightNp.setMinValue(40);
        weightNp.setMaxValue(70);
        weightNp.setValue(60);

        final Dialog Agedialog = new Dialog(this, R.style.MyDialogStyle);
        Agedialog.setContentView(choseheightweightView);
        Agedialog.show();

        (choseheightweightView.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heightAndweight.setText(heightNp.getValue() + "cm/" + weightNp.getValue() + "kg");
                Agedialog.dismiss();
            }
        });
        (choseheightweightView.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Agedialog.dismiss();
            }
        });
    }

    /**
     * 选择年龄
     */
    private void choseAge() {
        View choseAgeView = LayoutInflater.from(this).inflate(R.layout.chose_profession_layout, null);
        TextView dilog_type = (TextView) choseAgeView.findViewById(R.id.dilog_type);
        dilog_type.setText("年龄（岁）");
        final NumberPicker choseAgeNp = (NumberPicker) choseAgeView.findViewById(R.id.age_np);
        choseAgeNp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        choseAgeNp.setMinValue(18);
        choseAgeNp.setMaxValue(50);
        choseAgeNp.setValue(22);
        final Dialog Agedialog = new Dialog(this, R.style.MyDialogStyle);
        Agedialog.setContentView(choseAgeView);
        Agedialog.show();
        (choseAgeView.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age.setText(choseAgeNp.getValue() + "");
                Agedialog.dismiss();
            }
        });
        (choseAgeView.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Agedialog.dismiss();
            }
        });
    }

    /**
     * 选择职业
     */
    private void choseProfession() {
        View choseProfessionView = LayoutInflater.from(this).inflate(R.layout.chose_profession_layout, null);
        final NumberPicker choseProfessionNp = (NumberPicker) choseProfessionView.findViewById(R.id.age_np);
        final String[] mDateDisplayValues = mProfessions.toArray(new String[]{});
        choseProfessionNp.setDisplayedValues(mDateDisplayValues);
        choseProfessionNp.setMinValue(0);
        choseProfessionNp.setMaxValue(mDateDisplayValues.length - 1);
        choseProfessionNp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final Dialog professiondialog = new Dialog(this, R.style.MyDialogStyle);
        professiondialog.setContentView(choseProfessionView);
        professiondialog.show();
        (choseProfessionView.findViewById(R.id.confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profession.setText(mDateDisplayValues[choseProfessionNp.getValue()] + "");
                professiondialog.dismiss();
            }
        });
        (choseProfessionView.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professiondialog.dismiss();

            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUESR_CODE_INVITE_SELECT:
                if (data == null) return;
                List<InviteTypeBean> list = data.getParcelableArrayListExtra("data");
                if (list == null) return;
                mInviteTypes.clear();
                mInviteTypes.addAll(list);
                mInviteTypeRv.getAdapter().notifyDataSetChanged();
                break;
            case REQUEST_CODE_CAMERA:
                PictureUtil.startSystemPhotoZoom(CompleteSelfInfoActivity.this, Uri.fromFile(new File(mlocalPhotoUrl)), REQUEST_CODE_CROP);
                break;
            case REQUEST_CODE_ALBUM:
                if (data.getData() != null) {
                    LogDebug.log(TAG, "REQUEST_CODE_ALBUM uri = " + data.getData());
                    if (data.getData().toString().startsWith("file:")) {
                        mlocalPhotoUrl = data.getData().getPath();
                        PictureUtil.startSystemPhotoZoom(CompleteSelfInfoActivity.this, data.getData(), REQUEST_CODE_CROP);
                    } else if (data.getData().toString().startsWith("content:")) {
                        Cursor c = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                            mlocalPhotoUrl = c.getString(0);
                            c.close();
                            PictureUtil.startSystemPhotoZoom(CompleteSelfInfoActivity.this, Uri.fromFile(new File(mlocalPhotoUrl)), REQUEST_CODE_CROP);
                        }
                    }
                } else {
                    Loger.log(TAG, "REQUEST_CODE_ALBUM uri = null");
                }
                break;
            case REQUEST_CODE_CROP:
                if (data == null) return;
                Bitmap bitmap = data.getParcelableExtra("data");
                if (bitmap == null) return;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                try {
                    baos.writeTo(new FileOutputStream(mlocalPhotoUrl = PictureUtil.getPicSaveAddress() + "/" + System.currentTimeMillis() + "_crop.jpg"));
                    baos.close();
                    ImageLoad.bind(edit_headPhoto, mlocalPhotoUrl);
                    hasNewPortrait = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.show(CompleteSelfInfoActivity.this,"图片剪切失败");
                }
                break;
            default:
                Loger.log(TAG, "onActivityResult is default");
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
