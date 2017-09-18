package bbc.com.moteduan_lib.renzheng;

import android.content.Intent;
import android.os.Message;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.PreviewPicActivity;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.Picture;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.DetailActivity;
import bbc.com.moteduan_lib.mywidget.DialogProgress;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib.tools.LoadBitmapUtils;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class Auth extends BaseActivity {

    private OSSManager ossManager;

    private DialogProgress dialogProgress;

    private AuthHolder authHolder;

    public static final int upload_front_request = 1000;
    public static final int upload_behind_request = 2000;
    public static final int upload_hand_front_request = 3000;
    public static final int IDCARD_FLAG = 1;
    public static final int CAR_FLAG = 2;
    public static final int MODEL_FLAG = 3;
    public static final int SHIPPER_FLAG = 4;
    public static final int VEDIO_FLAG = 5;

    private int type = -1;


    public static final int IMAGE_REQUEST_CODE = 111;
    public static final int IMAGE_RESULT_CODE = 112;

    private java.util.ArrayList<Picture> picList;  //所选图片

    private android.widget.GridView picGridView;

    private int grideH;//高像素值px

    private String url = "";

    private static final int progress_flag = 1111;
    private static final int error_flag = 2222;
    private static final int success_flag = 3333;
    private static final int auth_success_flag = 4444;
    private static final int auth_error_flag = 5555;
    private static final int auth_cancle_flag = 6666;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:

                    if (type == IDCARD_FLAG) {
//                        dialogProgress.setTitle("上传正面照...");
                    } else if (type == SHIPPER_FLAG) {
//                        dialogProgress.setTitle("上传营业执照(正面)...");
                    } else if (type == MODEL_FLAG) {
//                        dialogProgress.setTitle("上传生活照...");
                    } else if (type == CAR_FLAG) {
//                        dialogProgress.setTitle("上传车与钥匙合照...");
                    }

                    break;
                case 1:
                    if (type == IDCARD_FLAG) {
//                        dialogProgress.setTitle("上传背面照...");
                    } else if (type == MODEL_FLAG) {
//                        dialogProgress.setTitle("上传艺术照...");
                    }
                    break;
                case 2:
                    if (type == IDCARD_FLAG) {
//                        dialogProgress.setTitle("上传手持正面照");
                    } else if (type == MODEL_FLAG) {
//                        dialogProgress.setTitle("上传模卡...");
                    }
                    break;
                case progress_flag:

                    int progress = (int) msg.obj;
//                    dialogProgress.setProgress(progress);
//                    dialogProgress.setProTxt(progress + "%");

                    break;
                case error_flag:
//                    dialogProgress.setTitle("很抱歉，上传失败");
                    break;
                case success_flag:

//                    dialogProgress.setTitle("上传成功，正在提交验证信息...");
                    break;
                case auth_success_flag:

//                    dialogProgress.setTitle("成功！");
                    dialogProgress.dismiss();


                    break;
                case auth_error_flag:

//                    dialogProgress.setTitle("失败！");
                    dialogProgress.dismiss();
                    toast.showText("上传失败，请重新上传");
                    break;
                case auth_cancle_flag:

//                    dialogProgress.setTitle("已取消！");
                    dialogProgress.dismiss();
                    toast.showText("已取消上传");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        initView();
        initData();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String from = intent.getStringExtra("from");
        if ("PictureChose".equals(from)) {
            Picture chosePic = intent.getParcelableExtra("pic");
            String auth_type = intent.getStringExtra("auth_type");
            switch (auth_type) {
                case "front":
                    Xutils.setRemoteImg(authHolder.getFront(), chosePic.getData());
                    frontPic = chosePic.getData();
                    break;
                case "bind":
                    Xutils.setRemoteImg(authHolder.getBehind(), chosePic.getData());
                    behindPic = chosePic.getData();
                    break;
                case "handfront":
                    Xutils.setRemoteImg(authHolder.getHandFront(), chosePic.getData());
                    hand_frontPic = chosePic.getData();
                    break;
            }
        }
    }

    @Override
    public void initView() {
        View rootView = View.inflate(Auth.this, R.layout.auth, null);
        ossManager = OSSManager.getInstance();
        authHolder = new AuthHolder(this);
        dialogProgress = new DialogProgress(rootView,Auth.this);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        if ("Authentication".equals(from)) {
            type = getIntent().getIntExtra("type", -1);
            switch (type) {
                case CAR_FLAG:

                    authHolder.getModulAuthLayout().setVisibility(android.view.View.GONE);
                    authHolder.getTitleName().setText("车辆认证");
                    authHolder.getT1().setText("带车钥匙(正面)");
                    authHolder.getT11().setText("请用手机横向拍摄保证图片正常显示");
                    authHolder.getT2().setVisibility(android.view.View.GONE);
                    authHolder.getT22().setVisibility(android.view.View.GONE);
                    authHolder.getLayout2().setVisibility(android.view.View.GONE);
                    authHolder.getT3().setVisibility(android.view.View.GONE);
                    authHolder.getT33().setVisibility(android.view.View.GONE);
                    authHolder.getLayout3().setVisibility(android.view.View.GONE);


                    break;
                case IDCARD_FLAG:

                    authHolder.getModulAuthLayout().setVisibility(android.view.View.GONE);
                    authHolder.getTitleName().setText("身份认证");
                    authHolder.getT1().setText("身份证正面照片");
                    authHolder.getT11().setText("请用手机横向拍摄保证图片正常显示");
                    authHolder.getT2().setText("身份证背面照片");
                    authHolder.getT22().setText("请用手机横向拍摄保证图片正常显示");
                    authHolder.getT3().setText("手持身份证正面照片");
                    authHolder.getT33().setText("请用手机横向拍摄保证图片正常显示");

                    break;
                case MODEL_FLAG:

                    authHolder.getModulAuthLayout().setVisibility(android.view.View.GONE);
                    authHolder.getTitleName().setText("模特认证");
                    authHolder.getT1().setText("生活照");
                    authHolder.getT11().setText("请上传清晰生活照用以模特认证");
                    authHolder.getT2().setText("艺术照");
                    authHolder.getT22().setText("请上传清晰艺术照用以模特认证");
                    authHolder.getT3().setText("模卡");
                    authHolder.getT33().setText("请上传清晰模卡用以模特认证");

                    break;
                case SHIPPER_FLAG:
                    authHolder.getModulAuthLayout().setVisibility(android.view.View.GONE);
                    authHolder.getTitleName().setText("商家认证");
                    authHolder.getT1().setText("营业执照(正面)");
                    authHolder.getT11().setText("请用手机横向拍摄保证图片正常显示");
                    authHolder.getT2().setVisibility(android.view.View.GONE);
                    authHolder.getT22().setVisibility(android.view.View.GONE);
                    authHolder.getLayout2().setVisibility(android.view.View.GONE);
                    authHolder.getT3().setVisibility(android.view.View.GONE);
                    authHolder.getT33().setVisibility(android.view.View.GONE);
                    authHolder.getLayout3().setVisibility(android.view.View.GONE);

//                    authHolder.getFront().setImageResource(R.drawable.yingyezhizhao);
                    break;
            }

        }


        final android.content.Intent it = new android.content.Intent(Auth.this,PictureChose.class);

        authHolder.getChoseFront().setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                it.putExtra("auth_other", true);
                it.putExtra("auth_type", "front");
                startActivityForResult(it, upload_front_request);
            }
        });

        authHolder.getChoseBehind().setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                it.putExtra("auth_other", true);
                it.putExtra("auth_type", "bind");
                startActivityForResult(it, upload_behind_request);
            }
        });

        authHolder.getChoseHandFront().setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                it.putExtra("auth_other", true);
                it.putExtra("auth_type", "handfront");
                startActivityForResult(it, upload_hand_front_request);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
//        int modelState = App.getApp().getModel_state();
//        switch (modelState){
//            case 1:
//                authHolder.getChoseFront().setEnabled(false);
//                authHolder.getChoseFront().setImageResource(R.drawable.daishenhe);
//                authHolder.getChoseBehind().setEnabled(false);
//                authHolder.getChoseBehind().setImageResource(R.drawable.daishenhe);
//                authHolder.getChoseHandFront().setEnabled(false);
//                authHolder.getChoseHandFront().setImageResource(R.drawable.daishenhe);
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 检查grideView的高度，重新设置
     */
    public void updateGridViewHight() {
        if (picList.size() <= 3) {
            android.view.ViewGroup.LayoutParams layoutParams = picGridView.getLayoutParams();
            layoutParams.height = grideH / 2;
            picGridView.setLayoutParams(layoutParams);
        }
    }

  /*  public void toChosePic() {
        //        startActivity(new android.content.Intent(Auth_model.this, com.bbc.lm.menu.PictureChose.class));
        android.content.Intent it = new android.content.Intent(this, com.bbc.lm.menu.PictureChose.class);
        it.putExtra("auth_model", true);
        it.putExtra("model_oldNums", picList.size());
        startActivityForResult(it, IMAGE_REQUEST_CODE);
    }*/

    public void previewPic(int index) {

        android.content.Intent it = new android.content.Intent(this, PreviewPicActivity.class);
        it.putParcelableArrayListExtra("pic", picList);
        it.putExtra("index", index);
        startActivity(it);

    }

    public void back(android.view.View view) {
        finish();
    }

    private String frontPic = "";
    private String behindPic = "";
    private String hand_frontPic = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == IMAGE_RESULT_CODE) {
            java.util.ArrayList<Picture> pics = data.getParcelableArrayListExtra("choosePic");
            if (pics != null && !pics.isEmpty()) {
                new GenerateThumbnail().execute(pics);
                if (picList.size() + pics.size() > 3) {
                    android.view.ViewGroup.LayoutParams layoutParams = picGridView.getLayoutParams();
                    layoutParams.height = grideH;
                    picGridView.setLayoutParams(layoutParams);
                }
            }

        } else if (PictureChose.Auth_other_result == resultCode) {
            Picture picture = data.getParcelableExtra("choosePic");

            switch (requestCode) {
                case upload_front_request:
                    Xutils.setRemoteImg(authHolder.getFront(), picture.getData());
                    frontPic = picture.getData();
                    break;
                case upload_behind_request:
                    Xutils.setRemoteImg(authHolder.getBehind(), picture.getData());
                    behindPic = picture.getData();
                    break;
                case upload_hand_front_request:
                    Xutils.setRemoteImg(authHolder.getHandFront(), picture.getData());
                    hand_frontPic = picture.getData();
                    break;
            }

//            if(!"".equals(frontPic) && !"".equals(behindPic) &&!"".equals(hand_frontPic)){
//                switch (type) {
//                    case com.bbc.lm.menu.Authentication.IDCARD_FLAG:
//                        map.put("pathJson",frontPic+","+behindPic+hand_frontPic);
//                        break;
//                    case Authentication.CAR_FLAG:
//                        map.put("pathJson",frontPic+","+behindPic+hand_frontPic);
//                        break;
//                    case com.bbc.lm.menu.Authentication.MODEL_FLAG:
//                        //                    map.put("model_rz",frontPic+","+behindPic+hand_frontPic);
//                        break;
//                    case com.bbc.lm.menu.Authentication.SHIPPER_FLAG:
//                        map.put("yyzz_path",frontPic);
//                        break;
//                }
//            }

        }
    }


    private class GenerateThumbnail extends android.os.AsyncTask<java.util.ArrayList<Picture>, Integer, java.util.ArrayList<Picture>> {

        @Override
        protected java.util.ArrayList<Picture> doInBackground(java.util.ArrayList<Picture>... params) {

            //根据ID生成 缩略图
            java.util.ArrayList<Picture> pictureArrayList = params[0];
            for (int i = 0, count = pictureArrayList.size(); i < count; i++) {

                Picture picture = pictureArrayList.get(i);
                String imageThumb = LoadBitmapUtils.getThumbnail(getApplicationContext(), picture.getData(), picture.get_id(), 160, 160);

                if (imageThumb != null) {
                    pictureArrayList.get(i).setThumbnailPath(imageThumb);
                    //                    picList.add(picture.setThumbnailPath(imageThumb));
                }
            }
            return pictureArrayList;
        }


        @Override
        protected void onPostExecute(java.util.ArrayList<Picture> pictureArrayList) {
            super.onPostExecute(pictureArrayList);
            if (!pictureArrayList.isEmpty()) {

                //int oldPicNums = picList.size();
                picList.addAll(pictureArrayList);

                //setBmp(pictureArrayList,oldPicNums);
            }

        }
    }


    public void updateLoad(android.view.View view) {
        switch (type) {
            case IDCARD_FLAG:

//                Object path = map.get("pathJson");
                if ("".equals(frontPic)) {
                    toast.showText("请选择正面照片");
                    return;
                }
                if ("".equals(behindPic)) {
                    toast.showText("请选择背面照片");
                    return;
                }
                if ("".equals(hand_frontPic)) {
                    toast.showText("请选择手持正面照片");
                    return;
                }


                url = Url.real_name;
                updateLoadPhoto(new String[]{frontPic, behindPic, hand_frontPic});


                break;
            case CAR_FLAG:

                if ("".equals(frontPic)) {
                    toast.showText("行驶证(正本)");
                    return;
                }

                url =Url.car_rz;
                updateLoadPhoto(new String[]{frontPic});

                break;
            case MODEL_FLAG:

                if ("".equals(frontPic)) {
                    toast.showText("生活照");
                    return;
                }
                if ("".equals(behindPic)) {
                    toast.showText("艺术照");
                    return;
                }
                if ("".equals(hand_frontPic)) {
                    toast.showText("模卡");
                    return;
                }

                url = Url.model_rz;
                updateLoadPhoto(new String[]{frontPic, behindPic, hand_frontPic});

                break;
            case SHIPPER_FLAG:

                if ("".equals(frontPic)) {
                    toast.showText("请选择正面照片");
                    return;
                }

                url = Url.merchant_rz;
                updateLoadPhoto(new String[]{frontPic});

                break;
        }
    }

    private void updateLoadPhoto(String[] pics) {

        dialogProgress.show();

        new Thread(new UpLoadThread(pics)).start();

//        switch (type) {
//            case com.bbc.lm.menu.Authentication.IDCARD_FLAG:
//
//                new Thread(new UpLoadThread(pics)).start();
//
//                break;
//            case Authentication.CAR_FLAG:
//
//                break;
//            case Authentication.MODEL_FLAG:
//
//                break;
//            case Authentication.SHIPPER_FLAG:
//
//                break;
//        }

    }


    class UpLoadThread implements Runnable {

        private String[] pics;

        public UpLoadThread(String[] pics) {
            this.pics = pics;
        }

        @Override
        public void run() {

            StringBuffer sbf = new StringBuffer();
            try {
                for (int i = 0; i < pics.length; i++) {
                    final android.os.Message msg = new Message();
                    msg.what = i;
                    handler.sendMessage(msg);
//                    dialogProgress.setTitle(i== 0?"上传正面照...":(i==1?"上传背面照":"上传手持正面照"));
                    PutObjectResult putResult = ossManager.synchUpLoad(pics[i], Constants.IMAGE_TYPE, new OSSProgressCallback() {
                        @Override
                        public void onProgress(Object o, long currentSize, long totalSize) {
                            android.os.Message msg = new Message();
                            msg.what = progress_flag;
                            int progress = Math.round((float) currentSize / totalSize * 100L);
                            msg.obj = progress;
                            handler.sendMessage(msg);
                        }
                    });
                    String url = putResult.getETag();
                    sbf.append(i == 0 ? url : "," + url);

                }

            } catch (ClientException e) {
                e.printStackTrace();

                android.os.Message msg = new Message();
                msg.what = error_flag;
                handler.sendMessage(msg);
                return;

            } catch (ServiceException e) {
                e.printStackTrace();
                android.os.Message msg = new Message();
                msg.what = error_flag;
                handler.sendMessage(msg);
                return;

            }

            android.os.Message msg = new Message();
            msg.what = success_flag;
            handler.sendMessage(msg);

            if (type == SHIPPER_FLAG) {
                map.put("yyzz_path", sbf.toString());
            } else if (type ==MODEL_FLAG) {
                map.put("model_rz", sbf.toString());
            } else if (type == IDCARD_FLAG) {
                map.put("pathJson", sbf.toString());
            } else if (type == CAR_FLAG) {
                map.put("pathJson", sbf.toString());
            }


            auth_ID();

        }
    }


    private java.util.Map<String, Object> map = new java.util.HashMap<>();

    private void auth_ID() {

        map.put("userid", SpDataCache.getSelfInfo(Auth.this).getData().getM_head_photo());

//        map.put("realName", "");
//        map.put("personCardnum", "");



        Xutils.post(url, map, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                android.os.Message msg = handler.obtainMessage();
                msg.what = auth_success_flag;
                handler.sendMessage(msg);

                Intent intent = new Intent(Auth.this, DetailActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                android.os.Message msg = handler.obtainMessage();
                msg.what = auth_error_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancelled(org.xutils.common.Callback.CancelledException cex) {

                android.os.Message msg = handler.obtainMessage();
                msg.what = auth_cancle_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinished() {

            }
        });

    }

}
