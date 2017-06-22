package com.kluz.vp6.vp1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by kluz on 6/14/17.
 */

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Statistics_Model> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,textView4;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.comment);
            textView4 = (TextView) view.findViewById(R.id.textView4);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public StatisticsAdapter(Context mContext, List<Statistics_Model> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public StatisticsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_card, parent, false);

        return new StatisticsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final StatisticsAdapter.MyViewHolder holder, int position) {

        final Statistics_Model client_stat = ListOfClients.get(position);
        holder.title.setText(client_stat.getLoan_month());
        holder.textView4.setText(client_stat.getLoan());

    }


    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}



