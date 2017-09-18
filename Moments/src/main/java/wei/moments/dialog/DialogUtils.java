package wei.moments.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import wei.moments.R;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class DialogUtils {

    // 私密状态选择框
    public static void showOpenStateDialog(Context context, int state, final DialogListener.ChooseItemListener chooseItemListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.lm_dialog_open_state_choose, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        Button confirm = (Button) view.findViewById(R.id.dialog_open_state_choose_confirm);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.dialog_open_state_choose_rg);
        final String[] text = {""};
        final int[] position = {-1};
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dialog_open_state_choose_open) {
                    text[0] = ((RadioButton) view.findViewById(R.id.dialog_open_state_choose_open)).getText().toString().trim();
                    position[0] = 1;
                } else if (checkedId == R.id.dialog_open_state_choose_mutual) {
                    text[0] = ((RadioButton) view.findViewById(R.id.dialog_open_state_choose_mutual)).getText().toString().trim();
                    position[0] = 2;
                } else if (checkedId == R.id.dialog_open_state_choose_user) {
                    text[0] = ((RadioButton) view.findViewById(R.id.dialog_open_state_choose_user)).getText().toString().trim();
                    position[0] = 3;
                }
            }
        });
        if (state == 1) {
            radioGroup.check(R.id.dialog_open_state_choose_open);
        } else if (state == 2) {
            radioGroup.check(R.id.dialog_open_state_choose_mutual);
        } else if (state == 3) {
            radioGroup.check(R.id.dialog_open_state_choose_user);
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseItemListener != null) {
                    chooseItemListener.onSelected(text[0], position[0]);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
