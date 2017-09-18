package wei.moments.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public abstract class BaseRvAdapter extends RecyclerView.Adapter {
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public abstract int getItemCount();
}
