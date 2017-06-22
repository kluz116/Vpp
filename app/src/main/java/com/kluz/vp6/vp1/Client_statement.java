package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kluz on 2/24/17.
 */

public class Client_statement extends AppCompatActivity implements FloatingToolbar.ItemClickListener, FloatingToolbar.MorphListener {
    private NestedScrollView mroot;
    private PaymentsAdapter adapter;
    private List<Payments> clientList;
    ProgressDialog progressDialog;
    private FloatingToolbar mFloatingToolbar;
    TextView client_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mroot = (NestedScrollView) findViewById(R.id.scrollView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView amt = (TextView) findViewById(R.id.amoun_pa);
        TextView phone = (TextView) findViewById(R.id.tvNumber1);
        TextView nok = (TextView) findViewById(R.id.nok);
        TextView deposit = (TextView) findViewById(R.id.textView29);
        TextView nok_phone = (TextView) findViewById(R.id.tvNumber2);
        TextView sales_p = (TextView) findViewById(R.id.valid);
        TextView current_out = (TextView) findViewById(R.id.current_out);
        TextView loan_out = (TextView) findViewById(R.id.loan_out);
        TextView tech = (TextView) findViewById(R.id.technician);
        TextView pay_plan = (TextView) findViewById(R.id.pay_plan);
        TextView disbursed_date = (TextView) findViewById(R.id.disbursed_date);
        TextView installation_date = (TextView) findViewById(R.id.installation_date);
        ImageView thumbnail = (ImageView) findViewById(R.id.imageView);
        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TextView status = (TextView) findViewById(R.id.status);
        TextView loan_status = (TextView) findViewById(R.id.loan_Period);
        TextView barcode = (TextView) findViewById(R.id.barcode);
        TextView district = (TextView) findViewById(R.id.district);
        TextView subcounty = (TextView) findViewById(R.id.subcounty);
        TextView parish = (TextView) findViewById(R.id.parish);
        TextView village = (TextView) findViewById(R.id.village);
        TextView customer_assoc_id = (TextView) findViewById(R.id.customer_assoc_id);
        TextView overdue_value = (TextView) findViewById(R.id.overdue_value);
        client_ids = (TextView) findViewById(R.id.client_ids);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        clientList = new ArrayList<>();
        adapter = new PaymentsAdapter(this, clientList);
        recyclerView.setAdapter(adapter);

        if (getIntent().getSerializableExtra("client")!=null){

            Warning obj = (Warning) getIntent().getSerializableExtra("client");
            phone.setText(obj.getPhone());
            nok.setText(obj.getNok());
            nok_phone.setText(obj.getNokPhohe());
            sales_p.setText(obj.getSales_p());
            amt.setText("Total Amount Paid : "+obj.getAmount_paid()+" UGX");
            deposit.setText("Deposit Required : "+obj.getDeposit_required()+" UGX");
            current_out.setText("Discount Balance : " +obj.getCurrentOut()+ " UGX");
            loan_out.setText("Annual Balance : " +obj.getLoan_outOut()+ " UGX");

            tech.setText(obj.getTechnician());
            pay_plan.setText("Payment Plan: ("+obj.getPay_plan()+")");
            disbursed_date.setText("Disbursed Date : "+ obj.getDisbursed());
            installation_date.setText("Installation Date : "+obj.getInstallation());
            Glide.with(this).load(obj.getThumbnail()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.images).centerCrop().crossFade().into(thumbnail);
            String cid = obj.getCid();
            status.setText(obj.getStatus());
            loan_status.setText("Discount Period ("+obj.getLoanPeriod()+") Month");
            barcode.setText("Barcode : "+obj.getBarcode());
            district.setText(obj.getDistrict());
            subcounty.setText("Sub-County: "+obj.getSubcounty());
            parish.setText("Parish: "+obj.getParish());
            village.setText("Village: "+obj.getAddress_desc());
            client_ids.setText("CID :"+obj.getCid());
            customer_assoc_id.setText(obj.getCustomer_assoc_name());
            overdue_value.setText("Overdue Value: "+obj.getOverdue_value()+" UGX");

            if(CheckNetwork.isInternetAvailable(Client_statement.this))
            {
                getClientsPayments(cid);
            }
            else
            {
                Snackbar snack = Snackbar.make(mroot,"Oooops, Please Check Your Internet Connection.....",Snackbar.LENGTH_LONG);
                snack.show();
            }


            final Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+"+obj.getPhone()));

            final Intent callIntent_nok = new Intent(Intent.ACTION_DIAL);
            callIntent_nok.setData(Uri.parse("tel:+"+obj.getNokPhohe()));

            phone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    startActivity(callIntent);
                    //Toast.makeText(Client_statement.this, "Selected Client "+phone.getText().toString().trim(), Toast.LENGTH_LONG).show();
                }
            });

            nok_phone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    startActivity(callIntent_nok);
                    //Toast.makeText(Client_statement.this, "Selected Client "+phone.getText().toString().trim(), Toast.LENGTH_LONG).show();
                }
            });

            String user = ""+obj.getName() +" | "+obj.getKit();
            initCollapsingToolbar(user);

        }



        mFloatingToolbar.setClickListener(this);
        mFloatingToolbar.attachFab(fab);
       mFloatingToolbar.attachRecyclerView(recyclerView);
        mFloatingToolbar.addMorphListener(this);

    }
    private void getClientsPayments(final String cid) {


        progressDialog.setMessage("Please Wait...Loading Payments Data...");
        showDialog();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://villagepower.info/vpc/get_payment_app.php";
        StringRequest arrayreq = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                Payments a = new Payments(obj.getString("amount"),obj.getString("installs"), obj.getString("daysPaid"), obj.getString("valid_until"), obj.getString("due_days"), obj.getString("outstanding"), obj.getString("pay_date"));
                                clientList.add(a);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        hideDialog();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", cid);
                return params;
            }

        };

        queue.add(arrayreq);


    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void initCollapsingToolbar( final String user) {

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(user);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public void onItemClick(MenuItem item) {
        int item_id = item.getItemId();
        if (getIntent().getSerializableExtra("client")!=null);
        Warning obj = (Warning) getIntent().getSerializableExtra("client");

        Bundle bundle = getIntent().getExtras();
        final String sales_person = obj.getCustomer_assoc_name();
        final String id = bundle.getString("id");
        final String cid = obj.getCid();
        final String name = obj.getName();
        final String phone = obj.getPhone();
        final String current = obj.getCurrentOut();
        final String annual = obj.getLoan_outOut();
        final String days = obj.getStatus();
        final String loan_period = obj.getLoanPeriod();
        final String kit = obj.getKit();

        if (item_id == R.id.overdue_call){

            Intent intent = new Intent(Client_statement.this, OverdueCall.class);
            intent.putExtra("cid", cid);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("days",days);
            intent.putExtra("kit",kit);
            startActivity(intent);


        }else if(item_id == R.id.statistics){
            Intent intent = new Intent(Client_statement.this, Statistics.class);
            intent.putExtra("cid", cid);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("current",current);
            intent.putExtra("annual",annual);
            intent.putExtra("loan_period",loan_period);
            startActivity(intent);

        }else if(item_id == R.id.logs){

            Intent intent = new Intent(Client_statement.this, Logs.class);
            intent.putExtra("cid", cid);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("current",current);
            intent.putExtra("annual",annual);
            intent.putExtra("loan_period",loan_period);
            startActivity(intent);

        }



    }

    @Override
    public void onItemLongClick(MenuItem item) {

    }

    @Override
    public void onMorphEnd() {

    }

    @Override
    public void onMorphStart() {

    }

    @Override
    public void onUnmorphStart() {

    }

    @Override
    public void onUnmorphEnd() {

    }
}
