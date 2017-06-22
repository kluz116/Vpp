package com.kluz.vp6.vp1;

import android.content.Context;
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
 * Created by kluz on 6/19/17.
 */


public class PayAdapter extends RecyclerView.Adapter<PayAdapter.MyViewHolder> {

    private Context mContext;
    private List<Pay_Model> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount,date,name;
        public ImageView overflow,imageView3;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
            amount = (TextView) view.findViewById(R.id.amount);
            imageView3 = (ImageView) view.findViewById(R.id.imageView3);

        }
    }


    public PayAdapter(Context mContext, List<Pay_Model> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public PayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pay_card, parent, false);

        return new PayAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PayAdapter.MyViewHolder holder, int position) {
        final Pay_Model client = ListOfClients.get(position);
        holder.name.setText(client.getName());
        holder.amount.setText(""+client.getAmount()+" UGX");
        Glide.with(mContext).load(client.getImage()).centerCrop().crossFade().placeholder(R.drawable.app_user).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView3);

        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(client.getDate());

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.date.setText(relavetime1);

        }catch(ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}






