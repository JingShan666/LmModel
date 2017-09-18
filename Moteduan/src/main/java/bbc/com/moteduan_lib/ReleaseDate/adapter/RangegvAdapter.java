package bbc.com.moteduan_lib.ReleaseDate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.ReleaseSkill;

/**
 * Created by zhang on 2016/11/29.
 */
public class RangegvAdapter extends BaseAdapter {
    private ArrayList<String> gvitems;
    private Context context;
    private int flag;

    public RangegvAdapter(ArrayList<String> gvitems, Context context,int flag) {
        this.gvitems = gvitems;
        this.context = context;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return gvitems == null? 0: gvitems.size();
    }

    @Override
    public Object getItem(int position) {
        return gvitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            if (context instanceof ReleaseSkill){
                if (flag == ReleaseSkill.DATERANGE){
                    convertView = View.inflate(context, R.layout.item_releaseskillgv,null);
                } else if (flag == ReleaseSkill.ADDRESS){
                    convertView = View.inflate(context, R.layout.item_addressgv,null);
                }

            } else {
                convertView = View.inflate(context, R.layout.item_rangegv,null);
            }
            viewHolder.lvtext = (TextView) convertView.findViewById(R.id.rangegv_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.lvtext.setText(gvitems.get(position));

        return convertView;
    }

    public class ViewHolder{
        TextView lvtext;
    }
}
