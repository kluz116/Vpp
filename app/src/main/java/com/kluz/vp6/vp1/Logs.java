package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

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
 * Created by kluz on 6/21/17.
 */

public class Logs extends AppCompatActivity {
    ProgressDialog progressDialog;
    private LinearLayout mroot;
    private LogsAdapter adapter;
    private List<LogsModel> clientList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mroot = (LinearLayout) findViewById(R.id.mroot);
        Bundle bundle = getIntent().getExtras();
        final String cid = bundle.getString("cid");
        final String sales_person = bundle.getString("sales_person");
        final String name = bundle.getString("name");
        final String phone = bundle.getString("phone");
        final String annual = bundle.getString("annual");
        final String cur = bundle.getString("current");
        final String loan_period = bundle.getString("loan_period");

        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        clientList = new ArrayList<>();
        adapter = new LogsAdapter(this, clientList);
        recyclerView.setAdapter(adapter);

        if(CheckNetwork.isInternetAvailable(Logs.this))
        {
            getLogs(cid);
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


        if (got_id ==R.id.nav_sign_out) {
            SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }else if(got_id==R.id.change_password){
            Intent i = new Intent(Logs.this,ChangePassword.class);
            i.putExtra("sales_person", sales_person);
            i.putExtra("id",id );
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private void getLogs( final String cid){

        progressDialog.setMessage("Loading..");
        showDialog();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://villagepower.info/vpc/history_data.php";
        StringRequest arrayreq = new StringRequest(Request.Method.POST,url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                LogsModel a = new LogsModel(obj.getString("date_called"),obj.getString("responsibility"),obj.getString("status"),obj.getString("comment"));
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

}


