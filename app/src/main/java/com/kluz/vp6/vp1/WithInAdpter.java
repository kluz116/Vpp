package com.kluz.vp6.vp1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kluz on 2/24/17.
 */



public class WithInAdpter extends RecyclerView.Adapter<WithInAdpter.MyViewHolder> {

    private Context mContext;
    private List<clients> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,status,valid_until;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.comment);
           status = (TextView) view.findViewById(R.id.status);
            count = (TextView) view.findViewById(R.id.count);
            valid_until = (TextView) view.findViewById(R.id.valid_until);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public WithInAdpter(Context mContext, List<clients> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clients_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final clients client = ListOfClients.get(position);
        holder.title.setText(client.getName());
        holder.count.setText(client.getPhone());
        holder.status.setText(client.getStatus());
        holder.valid_until.setText("VALID TILL "+client.getValid_until());


        holder.title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Valid_Till.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });
        holder.count.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+"+client.getPhone()));
                mContext.startActivity(callIntent);

            }
        });
        holder.valid_until.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Valid_Till.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });
        holder.status.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Valid_Till.class);
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

