package com.newthread.campusbroadcast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.bean.ChannelBean.ChannelInfo;
import com.newthread.campusbroadcast.util.JumpToChannelUtil;

import java.util.List;

/**
 * Created by 张云帆 on 2017/7/24.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>  {
    private Context context;
    private List<ChannelInfo> channelInfoList;

    public SearchAdapter(List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context ==  null)
        {
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.search_item,parent,false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                Log.d("ca", "onClick: "+position);
                //处理跳转逻辑
                JumpToChannelUtil jumpToChannelUtil=new JumpToChannelUtil(channelInfoList.get(position).getChannelId(),context);
                ChannelInfo channelInfo = channelInfoList.get(position);
                Toast.makeText(v.getContext(),"主播"+channelInfo.getChannelId(),Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChannelInfo channelInfo=channelInfoList.get(position);
        holder.channelName.setText(channelInfo.getChannelName());
        holder.broadcastName.setText(channelInfo.getBroadcasterName());

    }

    @Override
    public int getItemCount() {
        return channelInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView channelName;
        TextView broadcastName;

        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            channelName= (TextView) itemView.findViewById(R.id.search_channel_name);
            broadcastName= (TextView) itemView.findViewById(R.id.search_broadcast_name);
        }
    }

}
