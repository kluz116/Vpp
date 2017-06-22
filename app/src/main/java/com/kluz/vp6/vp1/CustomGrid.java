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

public class CustomGrid extends RecyclerView.Adapter<CustomGrid.MyViewHolder> {

    private Context mContext;
    private List<MenuModel> ListOfClients;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView overflow;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.comment);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public CustomGrid(Context mContext, List<MenuModel> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public CustomGrid.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_single, parent, false);

        return new CustomGrid.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final CustomGrid.MyViewHolder holder, final int position) {
        final MenuModel client = ListOfClients.get(position);
        holder.title.setText(client.getMenu_data());
        Glide.with(mContext).load(client.getIcon()).centerCrop().crossFade().placeholder(R.drawable.images).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);

        holder.title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if (position==0){
                    Intent in = new Intent(mContext,Salesperson.class);
                    in.putExtra("client", client);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     mContext.startActivity(in);
                    ((Menu)mContext).finish();

                }else  if(position==2){
                    Intent in = new Intent(mContext,Overdue.class);
                    in.putExtra("client", client);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(in);
                    ((Menu)mContext).finish();
                }else  if (position==4){
                    Intent in = new Intent(mContext,Warning_visit.class);
                    in.putExtra("client", client);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(in);
                    ((Menu)mContext).finish();

                }



            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (position==0){
                    Intent in = new Intent(mContext,Salesperson.class);
                    in.putExtra("client", client);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(in);
                    ((Menu)mContext).finish();

                }else  if(position==2){
                    Intent in = new Intent(mContext,Overdue.class);
                    in.putExtra("client", client);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(in);
                    ((Menu)mContext).finish();
                }else  if (position==4){
                    Intent in = new Intent(mContext,Warning_visit.class);
                    in.putExtra("client", client);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(in);
                    ((Menu)mContext).finish();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}


