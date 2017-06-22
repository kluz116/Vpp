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
 * Created by kluz on 3/23/17.
 */


public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Payments> ListOfClients;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount,pay_date,outstandings,val,installments,days;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            amount = (TextView) view.findViewById(R.id.amount);
            pay_date = (TextView) view.findViewById(R.id.pay_date);
            outstandings = (TextView) view.findViewById(R.id.textView);
            installments = (TextView) view.findViewById(R.id.installments);
            val = (TextView) view.findViewById(R.id.valid);
            days = (TextView) view.findViewById(R.id.days_paid_lol);

        }
    }


    public PaymentsAdapter(Context mContext, List<Payments> ListOfClients) {
        this.mContext = mContext;
        this.ListOfClients = ListOfClients;
    }

    @Override
    public PaymentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payments_card, parent, false);

        return new PaymentsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PaymentsAdapter.MyViewHolder holder, int position) {
        final Payments client = ListOfClients.get(position);
        holder.amount.setText(""+client.getAmount()+" UGX");
        holder.pay_date.setText(client.getPay_date());
        holder.outstandings.setText(client.getOutstanding());
        holder.val.setText(client.getValid_until());
        holder.installments.setText("Installments : "+client.getInstallemnts()+" UGX");
        holder.days.setText("Days Paid "+client.getDaysPaid());

    }



    @Override
    public int getItemCount() {
        return ListOfClients.size();
    }
}





