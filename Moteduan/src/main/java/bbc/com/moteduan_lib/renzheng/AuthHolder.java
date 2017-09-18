package bbc.com.moteduan_lib.renzheng;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import bbc.com.moteduan_lib.R;

public class AuthHolder {
    private android.widget.RelativeLayout name;
    private android.widget.ImageButton back;
    private android.widget.TextView titleName;
    private android.widget.TextView updateLoad;
    private android.widget.TextView t1;
    private android.widget.TextView t11;
    private android.widget.RelativeLayout layout1;
    private android.widget.ImageView front;
    private android.widget.ImageView choseFront;
    private android.widget.TextView t2;
    private android.widget.TextView t22;
    private android.widget.RelativeLayout layout2;
    private android.widget.ImageView behind;
    private android.widget.ImageView choseBehind;
    private android.widget.TextView t3;
    private android.widget.TextView t33;
    private android.widget.RelativeLayout layout3;
    private android.widget.ImageView handFront;
    private android.widget.ImageView choseHandFront;
    private android.widget.LinearLayout modulAuthLayout;
    private android.widget.GridView picGridView;
    private android.widget.TextView uploadFront;

    public AuthHolder(Activity view) {
        name = (android.widget.RelativeLayout) view.findViewById(R.id.name);
        back = (android.widget.ImageButton) name.findViewById(R.id.back);
        titleName = (android.widget.TextView) name.findViewById(R.id.titleName);
        updateLoad = (android.widget.TextView) name.findViewById(R.id.updateLoad);
        t1 = (android.widget.TextView) view.findViewById(R.id.t1);
        t11 = (android.widget.TextView) view.findViewById(R.id.t11);
        layout1 = (android.widget.RelativeLayout) view.findViewById(R.id.layout1);
        front = (android.widget.ImageView) layout1.findViewById(R.id.front);
        choseFront = (android.widget.ImageView) layout1.findViewById(R.id.choseFront);
        t2 = (android.widget.TextView) view.findViewById(R.id.t2);
        t22 = (android.widget.TextView) view.findViewById(R.id.t22);
        layout2 = (android.widget.RelativeLayout) view.findViewById(R.id.layout2);
        behind = (android.widget.ImageView) layout2.findViewById(R.id.behind);
        choseBehind = (android.widget.ImageView) layout2.findViewById(R.id.choseBehind);
        t3 = (android.widget.TextView) view.findViewById(R.id.t3);
        t33 = (android.widget.TextView) view.findViewById(R.id.t33);
        layout3 = (android.widget.RelativeLayout) view.findViewById(R.id.layout3);
        handFront = (android.widget.ImageView) layout3.findViewById(R.id.hand_front);
        choseHandFront = (android.widget.ImageView) layout3.findViewById(R.id.choseHand_front);
        modulAuthLayout = (android.widget.LinearLayout) view.findViewById(R.id.modulAuthLayout);
        picGridView = (android.widget.GridView) modulAuthLayout.findViewById(R.id.picGridView);
        uploadFront = (android.widget.TextView) modulAuthLayout.findViewById(R.id.upload_front);
    }

    public android.widget.TextView getT22() {
        return t22;
    }

    public android.widget.RelativeLayout getName() {
        return name;
    }

    public android.widget.ImageView getFront() {
        return front;
    }

    public android.widget.LinearLayout getModulAuthLayout() {
        return modulAuthLayout;
    }

    public android.widget.TextView getUpdateLoad() {
        return updateLoad;
    }

    public android.widget.TextView getT33() {
        return t33;
    }


    public android.widget.ImageButton getBack() {
        return back;
    }

    public android.widget.TextView getT1() {
        return t1;
    }


    public android.widget.TextView getT2() {
        return t2;
    }

    public android.widget.TextView getTitleName() {
        return titleName;
    }

    public android.widget.TextView getT3() {
        return t3;
    }

    public android.widget.GridView getPicGridView() {
        return picGridView;
    }
    public ImageView getChoseHandFront() {
        return choseHandFront;
    }

    public RelativeLayout getLayout3() {
        return layout3;
    }

    public RelativeLayout getLayout2() {
        return layout2;
    }

    public ImageView getChoseBehind() {
        return choseBehind;
    }

    public ImageView getChoseFront() {
        return choseFront;
    }

    public RelativeLayout getLayout1() {
        return layout1;
    }

    public android.widget.TextView getUploadFront() {
        return uploadFront;
    }

    public android.widget.TextView getT11() {
        return t11;
    }

    public android.widget.ImageView getBehind() {
        return behind;
    }

    public android.widget.ImageView getHandFront() {
        return handFront;
    }
}
