package com.kluz.vp6.vp1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kluz on 3/2/17.
 */

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Sales_model> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, phone,kit,sales_p,date_of_sale;
        public ImageView  overflow,thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.comment);
            kit = (TextView) view.findViewById(R.id.kit);
            phone = (TextView) view.findViewById(R.id.phone);
            date_of_sale = (TextView) view.findViewById(R.id.textView);
            sales_p = (TextView) view.findViewById(R.id.sales);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail = (ImageView) view.findViewById(R.id.imageView3);
        }
    }


    public SalesAdapter(Context mContext, List<Sales_model> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public SalesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_card, parent, false);

        return new SalesAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SalesAdapter.MyViewHolder holder, int position) {

        final Sales_model client = ListOfClients.get(position);
        holder.title.setText(client.getName());
        holder.phone.setText(client.getPhone());
        holder.sales_p.setText(client.getSales_p());
        holder.kit.setText("" +client.getKit());

        Glide.with(mContext).load(client.getImage()).centerCrop().crossFade().placeholder(R.drawable.app_user).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);



        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(client.getdate());

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.date_of_sale.setText("Date Of Sale : "+relavetime1);

        }catch(ParseException e) {
            e.printStackTrace();
        }
        holder.phone.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+"+client.getPhone()));
                mContext.startActivity(callIntent);

            }
        });



    }


    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}


