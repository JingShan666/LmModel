package bbc.com.moteduan_lib.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.bean.Picture;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class PictureAdapter extends android.widget.BaseAdapter {

	private List<Object> listPicData;
	private Context context;
//	private org.xutils.image.ImageOptions ops = new org.xutils.image.ImageOptions.Builder()
//			.setImageScaleType(android.widget.ImageView.ScaleType.CENTER_CROP)
////				.setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//			.setUseMemCache(true)
//			.setLoadingDrawableId(com.bbc.lm.R.drawable.loading)//加载中默认显示图片
//			.setFailureDrawableId(com.bbc.lm.R.drawable.loading_fail)//加载失败后默认显示图片
////			.setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
////			.setSize(org.xutils.common.util.DensityUtil.dip2px(120), org.xutils.common.util.DensityUtil.dip2px(120))//图片大小
//			.build();

	public PictureAdapter(Context context, List<Object> listPicGroup){
		this.context = context;
		this.listPicData = listPicGroup;
	}

	@Override
	public int getCount() {

		return listPicData == null?0:listPicData.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if(position == 0)return null;
		return listPicData.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, android.view.ViewGroup parent) {

		View view;
		ViewHolder holder;
		if(convertView == null){
			view = android.view.LayoutInflater.from(context).inflate(R.layout.pic_group_item, null);
			holder = new ViewHolder();
//			holder.name = (android.widget.TextView) view.findViewById(com.bbc.lm.R.id.groupName);
//			holder.picNums = (android.widget.TextView) view.findViewById(com.bbc.lm.R.id.picNums);
			holder.imageView1 = (android.widget.ImageView) view.findViewById(R.id.imageView1);
			holder.check = (android.widget.CheckBox) view.findViewById(R.id.check);
			view.setTag(holder);
		}else{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		if(position == 0){
			holder.check.setVisibility(android.view.View.GONE);
			holder.imageView1.setImageResource(R.drawable.add_pic);
		}else{
			Picture data = (Picture) listPicData.get(position-1);
			holder.check.setVisibility(android.view.View.VISIBLE);
			holder.check.setChecked(data.isCheck());
			Xutils.setRemoteImg(holder.imageView1, data.getData());
		}

		return view;
	}
	public void setCheck(Picture data){
		data.setCheck(!data.isCheck());
		notifyDataSetChanged();
	}

	class ViewHolder{

		android.widget.CheckBox check;
//		android.widget.TextView name;
//		android.widget.TextView picNums;
		android.widget.ImageView imageView1;

	}


}
