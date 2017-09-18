package bbc.com.moteduan_lib.ReleaseDate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bbc.com.moteduan_lib.R;

/**
 * Created by zhang on 2016/11/29.
 */
public class RangelvAdapter extends BaseAdapter {
    private List<String> lvitems;
    private Context context;

    public RangelvAdapter(List<String> lvitems, Context context) {
        this.lvitems = lvitems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lvitems == null? 0:lvitems.size();
    }

    @Override
    public Object getItem(int position) {
        return lvitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_ranglv,null);
            viewHolder.lvtext = (TextView) convertView.findViewById(R.id.range_lv_text);
            viewHolder.rangeimg = (ImageView) convertView.findViewById(R.id.range_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.lvtext.setText(lvitems.get(position));
        return convertView;
    }

    public class ViewHolder{
        TextView lvtext;
        ImageView rangeimg;
    }
}
