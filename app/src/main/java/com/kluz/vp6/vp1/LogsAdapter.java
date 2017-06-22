package com.kluz.vp6.vp1;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.text.format.DateUtils;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;

/**
 * Created by kluz on 6/14/17.
 */

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> {

    private Context mContext;
    private List<LogsModel> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView status,comment,date_called,responbility;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            status = (TextView) view.findViewById(R.id.status);
            comment = (TextView) view.findViewById(R.id.comment);
            date_called = (TextView) view.findViewById(R.id.date_called);
            responbility = (TextView) view.findViewById(R.id.responsibility);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public LogsAdapter(Context mContext, List<LogsModel> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public LogsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logs_card, parent, false);

        return new LogsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final LogsAdapter.MyViewHolder holder, int position) {

        final LogsModel client_stat = ListOfClients.get(position);
        holder.status.setText(client_stat.getStatus());
        holder.comment.setText(client_stat.getComment());
        holder.date_called.setText(client_stat.getDate());
        holder.responbility.setText(client_stat.getResponsibility());

        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(client_stat.getDate());

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.date_called.setText(relavetime1);

        }catch(ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}



