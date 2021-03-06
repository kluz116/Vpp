package com.kluz.vp6.vp1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kluz on 3/17/17.
 */

public class SalespersonAdapter  extends RecyclerView.Adapter<SalespersonAdapter.MyViewHolder> {

    private Context mContext;
    private List<Sales_model> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, phone,kit,sales_p,date_of_sale;
        public ImageView overflow,thumbnail;

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


    public SalespersonAdapter(Context mContext, List<Sales_model> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public SalespersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_card_person, parent, false);

        return new SalespersonAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SalespersonAdapter.MyViewHolder holder, int position) {
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




    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_clients, popup.getMenu());
        popup.setOnMenuItemClickListener(new SalespersonAdapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            //case R.id.action_add_favourite:
            //Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
            //return true;
            //case R.id.action_play_next:
            // Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
            //return true;
            //default:
            //}    //switch (menuItem.getItemId()) {

            return false;
        }
    }
    public void setFilter(List<Sales_model> clientModels) {
        ListOfClients = new ArrayList<>();
        ListOfClients.addAll(clientModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}



