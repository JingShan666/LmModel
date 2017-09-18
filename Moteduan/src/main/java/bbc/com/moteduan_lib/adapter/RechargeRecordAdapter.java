package bbc.com.moteduan_lib.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bbc.com.moteduan_lib.R;

/**
 * Created by zhang on 2016/12/21.
 */
public class RechargeRecordAdapter extends BaseAdapter {
    private JSONArray data = null;

    public RechargeRecordAdapter(JSONArray data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        Object o = null;
        try {
            o = data.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.rechargerecord_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) data.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        int model_money = dataBean.getMoney();
        try {
            String money = jsonObject.getString("money");
            String bank_card = jsonObject.getString("bank_card");
            String time = jsonObject.getString("time");
            int do_state = jsonObject.getInt("do_state");
            viewHolder.rrItem_money.setText(money + "");
            viewHolder.rrItem_way.setText(bank_card);
            viewHolder.rritem_time.setText(time);
            if (do_state == 0) {
                viewHolder.Imagewithdraw.setImageResource(R.drawable.icon_weixuan);
                viewHolder.rrItem_IsSuccess.setText(" 正在受理...");
            } else if (do_state == 1){
                viewHolder.Imagewithdraw.setImageResource(R.drawable.icon_yixuan_pre);
                viewHolder.rrItem_IsSuccess.setText(" 已到账");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView rrItem_money;
        public TextView rrItem_way;
        public TextView tv_line;
        public RelativeLayout rl1;
        public ImageView Imagewithdraw;
        public TextView rrItem_IsSuccess;
        public TextView rritem_time;
        public RelativeLayout rl2;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.rrItem_money = (TextView) rootView.findViewById(R.id.rrItem_money);
            this.rrItem_way = (TextView) rootView.findViewById(R.id.rrItem_way);
            this.tv_line = (TextView) rootView.findViewById(R.id.tv_line);
            this.rl1 = (RelativeLayout) rootView.findViewById(R.id.rl1);
            this.Imagewithdraw = (ImageView) rootView.findViewById(R.id.Imagewithdraw);
            this.rrItem_IsSuccess = (TextView) rootView.findViewById(R.id.rrItem_IsSuccess);
            this.rritem_time = (TextView) rootView.findViewById(R.id.rritem_time);
            this.rl2 = (RelativeLayout) rootView.findViewById(R.id.rl2);
        }

    }
}
