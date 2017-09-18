package bbc.com.moteduan_lib.renzheng.adapter;

import android.widget.BaseAdapter;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.bean.VideoInfo;
import bbc.com.moteduan_lib.tools.DateUtils;

/**
 * Created by Administrator on 2016/5/31 0031.
 */
public class VideoAdapter extends BaseAdapter {


    public VideoAdapter(java.util.List<VideoInfo> videoList) {
        this.videoList = videoList;
    }

    private  java.util.List<VideoInfo> videoList;

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        MyHolder holder ;

        if(convertView == null){
            holder = new VideoAdapter.MyHolder();
            convertView = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.chose_video_item,null);

            holder.name = (android.widget.TextView) convertView.findViewById(R.id.name);
            holder.thuml = (android.widget.ImageView) convertView.findViewById(R.id.thuml);
            holder.time = (android.widget.TextView) convertView.findViewById(R.id.times);
            convertView.setTag(holder);
        }else{
            holder = (VideoAdapter.MyHolder) convertView.getTag();
        }

        VideoInfo videoInfo = videoList.get(position);
        holder.name.setText( videoInfo.getVideoName() );
        holder.time.setText(DateUtils.transformationDate(videoInfo.getDuration(), DateUtils.HH_mm_ss));

        holder.thuml.setImageBitmap(videoInfo.getBmp());


//        com.bbc.lm.log.LogDebug.print(new java.io.File(videoInfo.getData()).toURI().toString());
//        com.bbc.lm.toools.Xutils3.Xutils.setRemoteImg(holder.thuml,new java.io.File(videoInfo.getData()).toURI().toString());

//        holder.thuml.setTag(videoInfo.getData());
//        new com.bbc.lm.squareFragment.adapter.VideoAdapter.MyAsyncTask(holder.thuml).execute(videoInfo.getData());

        return convertView;
    }

    class MyHolder{
        android.widget.ImageView thuml;
        android.widget.TextView name,time;
    }

//    class MyAsyncTask extends android.os.AsyncTask<String,Integer, android.graphics.Bitmap>{
//
//        public MyAsyncTask(android.widget.ImageView thuml) {
//            this.thuml = thuml;
//        }
//
//        android.widget.ImageView thuml;
//        String path;
//
//        @Override
//        protected android.graphics.Bitmap doInBackground(String... params) {
//            path = params[0];
//            return com.bbc.lm.toools.BitMapUtils.getThuml(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(android.graphics.Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//
//            if(bitmap != null && path.equals(thuml.getTag().toString())){
//                thuml.setImageBitmap(bitmap);
//            }
//
//        }
//    }

}



































