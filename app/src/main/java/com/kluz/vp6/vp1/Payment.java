package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kluz on 6/19/17.
 */

public class Payment extends AppCompatActivity {
    ProgressDialog progressDialog;
    private RelativeLayout mroot;
    private PayAdapter adapter;
    private List<Pay_Model> clientList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments);
         mroot = (RelativeLayout) findViewById(R.id.main_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payments Collected");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        final String sales_person = bundle.getString("sales_person");
        final String vpc = bundle.getString("vpc");
        final String sp = bundle.getString("id");


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        clientList = new ArrayList<>();
        adapter = new PayAdapter(this, clientList);
        recyclerView.setAdapter(adapter);

        if(CheckNetwork.isInternetAvailable(Payment.this)) //returns true if internet available
        {
            Clients(sp);

        }
        else
        {
            Snackbar snack = Snackbar.make(mroot,"Oooops, Please Check Your Internet Connection.....",Snackbar.LENGTH_LONG);
            snack.show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.app_help, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int got_id = item.getItemId();
        Bundle bundle = getIntent().getExtras();
        final String sales_person = bundle.getString("sales_person");
        final String id = bundle.getString("id");


        if (got_id == R.id.nav_sign_out) {

            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.clear();
            e.commit();

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if (got_id == R.id.change_password) {
            Intent i = new Intent(Payment.this, ChangePassword.class);
            i.putExtra("sales_person", sales_person);
            i.putExtra("id", id);
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void Clients(final String sp) {


        progressDialog.setMessage("Please Wait..");
        showDialog();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://villagepower.info/vpc/payments_app.php";
        StringRequest arrayreq = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);
                                Pay_Model a = new Pay_Model(obj.getString("cid"),obj.getString("name"),obj.getString("amount"),obj.getString("date"),obj.getString("image"));
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
                params.put("id", sp);
                return params;
            }

        };

        queue.add(arrayreq);


    }

}
