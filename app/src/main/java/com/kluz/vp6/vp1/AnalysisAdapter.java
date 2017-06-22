package com.kluz.vp6.vp1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by kluz on 3/23/17.
 */


public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.MyViewHolder> {

    private Context mContext;
    private List<Warning> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, phone,kit,sales_p;
        public ImageView overflow,profile_picture;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.comment);
            kit = (TextView) view.findViewById(R.id.kit);
            phone = (TextView) view.findViewById(R.id.count);
            sales_p = (TextView) view.findViewById(R.id.valid);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            profile_picture = (ImageView) view.findViewById(R.id.imageView3);
        }
    }


    public AnalysisAdapter(Context mContext, List<Warning> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public AnalysisAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.analysis_card, parent, false);

        return new AnalysisAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final AnalysisAdapter.MyViewHolder holder, int position) {
        final Warning client = ListOfClients.get(position);
        holder.title.setText(client.getName());
        holder.phone.setText(client.getPhone());
        holder.sales_p.setText(client.getSales_p());
        holder.kit.setText("" +client.getKit());
        Glide.with(mContext).load(client.getThumbnail()).centerCrop().crossFade().placeholder(R.drawable.app_user).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_picture);


        holder.title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Client_statement.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });
        holder.phone.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Client_statement.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });

        holder.sales_p.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Client_statement.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });

        holder.profile_picture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Client_statement.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });
        holder.profile_picture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Client_statement.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });


    }



    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}




