package bbc.com.moteduan_lib.leftmenu.holder;

import android.widget.RelativeLayout;

import bbc.com.moteduan_lib.R;

public class SetMenuHolder {
    private android.widget.RelativeLayout titleLayout;
    private android.widget.ImageButton back;
    private android.widget.TextView l11;
    private android.widget.TextView l22;
    private android.widget.TextView l33;
    private android.widget.TextView l44;
    private android.widget.TextView l55;
    private android.widget.TextView l66;
    private android.widget.Button cancellation;

    public RelativeLayout getL2() {
        return l2;
    }

    private final RelativeLayout l2;
    //    ToggleButton togglebutton;

    public SetMenuHolder(android.view.View view) {
        titleLayout = (android.widget.RelativeLayout) view.findViewById(R.id.titleLayout);
        back = (android.widget.ImageButton) titleLayout.findViewById(R.id.back);
//        l11 = (android.widget.TextView) view.findViewById(com.bbc.lm.R.id.l11);
        l22 = (android.widget.TextView) view.findViewById(R.id.l22);
        l2 = (RelativeLayout) view.findViewById(R.id.l2);
//        l33 = (android.widget.TextView) view.findViewById(com.bbc.lm.R.id.l33);
        l44 = (android.widget.TextView) view.findViewById(R.id.l44);
        l55 = (android.widget.TextView) view.findViewById(R.id.l55);
        l66 = (android.widget.TextView) view.findViewById(R.id.l66);
        cancellation = (android.widget.Button) view.findViewById(R.id.cancellation);
//        togglebutton = (ToggleButton) view.findViewById(R.id.togglebutton);
    }

   /* public ToggleButton getTogglebutton() {
        return togglebutton;
    }*/

    public android.widget.TextView getL55() {
        return l55;
    }

    public android.widget.TextView getL44() {
        return l44;
    }

    public android.widget.TextView getL66() {
        return l66;
    }

    public android.widget.RelativeLayout getTitleLayout() {
        return titleLayout;
    }

    public android.widget.Button getCancellation() {
        return cancellation;
    }

    public android.widget.ImageButton getBack() {
        return back;
    }

    public android.widget.TextView getL11() {
        return l11;
    }

    public android.widget.TextView getL33() {
        return l33;
    }

    public android.widget.TextView getL22() {
        return l22;
    }
}
