package com.newthread.campusbroadcast.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.bean.ChannelBean.ChannelInfo;
import com.newthread.campusbroadcast.util.JumpToChannelUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 张云帆 on 2017/7/23.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {
    private Context context;
    private List<ChannelInfo> channelInfoList;

    public ChannelAdapter(List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context ==  null)
        {
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.channel_item,parent,false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                Log.d("ca", "onClick: "+position);
                JumpToChannelUtil jumpToChannelUtil=new JumpToChannelUtil(channelInfoList.get(position).getChannelId(),context);
                //channelInfoList.get(position).getChannelId();
                ChannelInfo channelInfo = channelInfoList.get(position);
              //  Toast.makeText(v.getContext(),"被关注人数"+channelInfo.getBeFocusedNum(),Toast.LENGTH_SHORT).show();
            }
        });
//        return new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChannelInfo channelInfo=channelInfoList.get(position);
//        holder.channelImage.setImageResource(channelInfo.getImageId());

        Picasso.with(context)
                .load(channelInfo.getImageId())
                .placeholder(R.drawable.b)
                .into(holder.channelImage);

        holder.channelName.setText(channelInfo.getChannelName());
        holder.broadcastName.setText(channelInfo.getBroadcasterName());

        if(!channelInfo.getSex().isEmpty() && !(channelInfo.getSex()==null))
        {
            if(channelInfo.getSex().equals("男"))
            {
                holder.sex.setImageResource(R.drawable.ic_sex_man);
            }else if(channelInfo.getSex().equals("女"))
            {
                holder.sex.setImageResource(R.drawable.ic_sex_woman);
            }
        }

        holder.beFocusedNum.setText(String.valueOf(channelInfo.getBeFocusedNum()));
    }

    @Override
    public int getItemCount() {
        return channelInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView channelImage;
        TextView channelName;
        TextView broadcastName;
        ImageView sex;
        TextView beFocusedNum;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            channelImage= (ImageView) itemView.findViewById(R.id.channel_Image);
            channelName= (TextView) itemView.findViewById(R.id.channel_name);
            broadcastName= (TextView) itemView.findViewById(R.id.broadcast_name);
            sex= (ImageView) itemView.findViewById(R.id.sex);
            beFocusedNum= (TextView) itemView.findViewById(R.id.be_focused_num);
        }
    }
}
