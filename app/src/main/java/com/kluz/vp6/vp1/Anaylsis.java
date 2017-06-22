package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by kluz on 3/4/17.
 */

public class Anaylsis extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout mroot;
    private AnalysisAdapter adapter;
    private List<Warning> clientList;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;
    SearchView searchView = null;
    String sales;
    String sales_menu="";
    String sales_previous_month="";
    String sales_rev="";
    String overdue_percent;
    String overdue_num_sp;
    String overdue_num_plus;
    String percent_plus;


    MenuItem sales_res;
    MenuItem sa;
    MenuItem previous_month_sales;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_main);
        mroot = (RelativeLayout) findViewById(R.id.content_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        final String sales_person = bundle.getString("sales_person");
        final String vpc = bundle.getString("vpc");
        final String sp = bundle.getString("id");

        toolbar.setTitle("Search Client");

        
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        clientList = new ArrayList<>();
        adapter = new AnalysisAdapter(getApplicationContext(), clientList);
        recyclerView.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user);
        txtProfileName.setText(sales_person);

        Menu menu = navigationView.getMenu();
        sales_res = menu.findItem(R.id.nav_gallery);
        previous_month_sales = menu.findItem(R.id.n);
        Num_of_sales_menu(vpc);
        Num_of_previous_sales_men(vpc);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.statistics) {
            return true;
        }else if(id==android.R.id.home){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Anaylsis.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        //getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) Anaylsis.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Anaylsis.this.getComponentName()));
            searchView.setIconified(false);
        }


        return true;
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
      //handleIntent(intent);
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String search_query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }

            if(CheckNetwork.isInternetAvailable(Anaylsis.this)) //returns true if internet available
            {
                getSeletedClient(search_query);
            }
            else
            {
                Snackbar snack = Snackbar.make(mroot,"Ooops, Please Check Your Internet Connection.....",Snackbar.LENGTH_LONG);
                snack.show();
            }


        }

    }

    private void handleIntent(Intent intent) {


    }



    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int item_id = item.getItemId();

        Bundle bundle = getIntent().getExtras();
        final String vpc = bundle.getString("vpc");
        final String sales_person = bundle.getString("sales_person");
        final String id = bundle.getString("id");

        if (item_id == R.id.nav_gallery) {

            Intent intent = new Intent(Anaylsis.this, Sales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();
        } else if (item_id == R.id.pending_overdue) {
            Intent intent = new Intent(Anaylsis.this, MainActivity.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        } else if (item_id == R.id.nav_sign_out) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();

        }else if(item_id == R.id.pending_visit){
            Intent intent = new Intent(Anaylsis.this, PendingVisit.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id == R.id.sales_vpc){


            Intent intent = new Intent(Anaylsis.this, PreviousSales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();
        }else if(item_id==R.id.home){
            Intent intent = new Intent(Anaylsis.this, com.kluz.vp6.vp1.Menu.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Num_of_previous_sales_men(final String vpc){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://villagepower.info/vpc/previous_vpc_sales.php";

        StringRequest arrayreq = new StringRequest(Request.Method.POST,url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                sales_previous_month  = obj.getString("sales_made");
                                previous_month_sales.setTitle("Last Month VPC Sales ("+sales_previous_month+") ");


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

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vpc",vpc);
                return params;
            }

        };

        queue.add(arrayreq);


    }


    private void Num_of_sales_menu(final String vpc){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://villagepower.info/vpc/num_of_sales.php";

        StringRequest arrayreq = new StringRequest(Request.Method.POST,url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                sales_menu = obj.getString("sales_made");
                                sales_res.setTitle("Month To Date VPC Sales ("+ sales_menu+")");
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

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("vpc", vpc);
                return params;
            }

        };

        queue.add(arrayreq);

        //return sales_menu;
    }
        private void getSeletedClient( final String search_query){

            progressDialog.setMessage("Searching For "+search_query+". Please Wait...");
            showDialog();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String url = "http://villagepower.info/vpc/clients_data_v1.php";
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
                                    Warning a = new Warning(obj.getString("cid"),obj.getString("clients_name"), obj.getString("default_phone"), obj.getString("image"), obj.getString("cur_overdue"), obj.getString("next_ov_keen"), obj.getString("nok_phone"), obj.getString("current_outstanding"), obj.getString("loan_outstanding"), obj.getString("sales_person"), obj.getString("kit"),obj.getString("amount_paid"),obj.getString("deposit_required"),obj.getString("installation"),obj.getString("disbursed"),obj.getString("pay_plan"),obj.getString("technician"),obj.getString("loan_period"),obj.getString("barcode"),obj.getString("district"),obj.getString("sub_county"),obj.getString("parish"),obj.getString("address_desc"),obj.getString("overdue_value"),obj.getString("customer_assoc_name"));
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
                    params.put("search_query",search_query);
                    return params;
                }

            };

            queue.add(arrayreq);


        }



}
