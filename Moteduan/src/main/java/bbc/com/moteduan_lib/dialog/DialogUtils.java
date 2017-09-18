package bbc.com.moteduan_lib.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.home.PhoneBingActivity;
import bbc.com.moteduan_lib.renzheng.ShipinRenzhengActivity;
import bbc.com.moteduan_lib.tools.PhoneUrils;


/**
 * Created by Administrator on 2017/2/20 0020.
 */
public class DialogUtils {

    /**
     * @param context 提示绑定手机
     */
    public static void phoneBindingAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您还未绑定手机号");
        builder.setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(new Intent(context, PhoneBingActivity.class));
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void videoCertification(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您还未视频认证");
        builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent2 = new Intent(context, ShipinRenzhengActivity.class);
                context.startActivity(intent2);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void callPhoneAlert(final Context context, final String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要拨打: " + phone + " 电话吗？");
        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(phone)) {
                    return;
                }
                PhoneUrils.callPhone(context, phone);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void videoUploadSelectDialog(Context context, final Handler handler) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_video_upload_select, null);
        Button local = (Button) view.findViewById(R.id.dialog_video_upload_select_local);
        Button shoot = (Button) view.findViewById(R.id.dialog_video_upload_select_shoot);
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        });
        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (handler != null) {
                    handler.sendEmptyMessage(1);
                }
            }
        });

        dialog.show();

    }

    public static void sanweiChooseDialog(Context context, final SanweiChooseDialogCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_sanwei, null);
        TextView ok = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        final NumberPicker xiong = (NumberPicker) view.findViewById(R.id.dialog_choose_sanwei_xiong);
        final NumberPicker yao = (NumberPicker) view.findViewById(R.id.dialog_choose_sanwei_yao);
        final NumberPicker tun = (NumberPicker) view.findViewById(R.id.dialog_choose_sanwei_tun);

        xiong.setMinValue(75);
        xiong.setMaxValue(100);
        xiong.setValue(90);
        yao.setMinValue(50);
        yao.setMaxValue(70);
        yao.setValue(60);
        tun.setMinValue(75);
        tun.setMaxValue(100);
        tun.setValue(90);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callBack != null) {
                    callBack.result(new String[]{xiong.getValue() + "", yao.getValue() + "", tun.getValue() + ""});
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void showTextDialog(Context context, String content, final Handler.Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(content);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback != null) {
                    Message message = Message.obtain();
                    message.what = 1;
                    callback.handleMessage(message);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback != null) {
                    Message message = Message.obtain();
                    message.what = 0;
                    callback.handleMessage(message);
                }
            }
        });
        builder.show();
    }


    public static void inputInviteCodeDialog(Context context, String titleText, final Handler.Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_input_invite_code, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_input_invite_code_title);
        if (!TextUtils.isEmpty(titleText)) title.setText(titleText);
        TextView ok = (TextView) view.findViewById(R.id.dialog_input_invite_code_ok);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_input_invite_code_cancel);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_input_invite_code_edit);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (null != callback) {
                    Message message = Message.obtain();
                    message.obj = editText.getText().toString().trim();
                    callback.handleMessage(message);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    public static interface SanweiChooseDialogCallBack {
        public void result(String[] result);
    }


    /**
     * @param context
     * @param callback
     */
    public static void commonAlert(Context context, String title, String content, String positiveName, String neutralName, String negativeName, final Handler.Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            builder.setMessage(content);
        }
        if (!TextUtils.isEmpty(positiveName)) {
            builder.setPositiveButton(positiveName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null) {
                        Message message = Message.obtain();
                        message.what = 0;
                        callback.handleMessage(message);
                    }

                    dialog.dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(neutralName)) {
            builder.setNeutralButton(neutralName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null) {
                        Message message = Message.obtain();
                        message.what = 1;
                        callback.handleMessage(message);
                    }
                    dialog.dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negativeName)) {
            builder.setNegativeButton(negativeName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null) {
                        Message message = Message.obtain();
                        message.what = 2;
                        callback.handleMessage(message);
                    }
                    dialog.dismiss();
                }
            });
        }
        builder.show();

    }

}
