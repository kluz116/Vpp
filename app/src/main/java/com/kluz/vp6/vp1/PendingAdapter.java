

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
 * Created by kluz on 3/2/17.
 */

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    private Context mContext;
    private List<Warning> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, phone,status,sales_p;
        public ImageView overflow,thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.comment);
            status = (TextView) view.findViewById(R.id.status);
            phone = (TextView) view.findViewById(R.id.phone);
            sales_p = (TextView) view.findViewById(R.id.sales);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail = (ImageView) view.findViewById(R.id.imageView3);
        }
    }


    public PendingAdapter(Context mContext, List<Warning> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public PendingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_cards, parent, false);

        return new PendingAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final PendingAdapter.MyViewHolder holder, int position) {
        final Warning client = ListOfClients.get(position);
        holder.title.setText(client.getName());
        holder.phone.setText(client.getPhone());
        holder.sales_p.setText(client.getSales_p());
        holder.status.setText(client.getStatus());
        Glide.with(mContext).load(client.getThumbnail()).centerCrop().crossFade().placeholder(R.drawable.app_user).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);


        holder.title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext,Client_statement.class);
                in.putExtra("client", client);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);

            }
        });
        holder.status.setOnClickListener(new View.OnClickListener(){

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

        holder.thumbnail.setOnClickListener(new View.OnClickListener(){

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


