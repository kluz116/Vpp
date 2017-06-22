package com.kluz.vp6.vp1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kluz on 4/10/17.
 */

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    String sales_menu = "";
    String sales_previous_month = "";
    private List<MenuModel> clientList;
    private CustomGrid adapter;
    TextView Sum_of_money,names,overdue;

    MenuItem sales_res;
    MenuItem previous_month_sales;

    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Sum_of_money = (TextView) findViewById(R.id.Sum_of_money);
        names = (TextView) findViewById(R.id.names);
        overdue = (TextView) findViewById(R.id.overdue);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        Bundle bundle = getIntent().getExtras();
        final String vpc = bundle.getString("vpc");
        final String sp = bundle.getString("id");
        final String sales_person = bundle.getString("sales_person");

        
        names.setText(sales_person);
        Sum_of_money.setOnClickListener(this);


        if (CheckNetwork.isInternetAvailable(Menu.this)) {
            checkVersion(sp);
            Num_of_sales_menu(vpc);
            Num_of_previous_sales_men(vpc);
            getAmountCollected(sp);
        } else {
            Toast.makeText(Menu.this, "Check Your Internet Connection..", Toast.LENGTH_LONG).show();
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        
        clientList = new ArrayList<>();
        adapter = new CustomGrid(this, clientList);
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

        android.view.Menu menu = navigationView.getMenu();
        sales_res = menu.findItem(R.id.nav_gallery);
        previous_month_sales = menu.findItem(R.id.n);



        initCollapsingToolbar();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Menu.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.app_help, menu);

        return true;
    }

    private void Num_of_previous_sales_men(final String vpc) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://villagepower.info/vpc/previous_vpc_sales.php";

        StringRequest arrayreq = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);
                                sales_previous_month = obj.getString("sales_made");
                                previous_month_sales.setTitle("Last Month VPC Sales (" + sales_previous_month + ") ");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("vpc", vpc);
                return params;
            }

        };

        queue.add(arrayreq);


    }

    private void Num_of_sales_menu(final String vpc) {
        //String sales_menu;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://villagepower.info/vpc/num_of_sales.php";

        StringRequest arrayreq = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);
                                sales_menu = obj.getString("sales_made");
                                sales_res.setTitle("Month To Date VPC Sales (" + sales_menu + ")");
                                //Toast.makeText(getApplicationContext(), sales_menu, Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(Menu.this, ChangePassword.class);
            i.putExtra("sales_person", sales_person);
            i.putExtra("id", id);
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        Bundle bundle = getIntent().getExtras();
        final String vpc = bundle.getString("vpc");
        final String sales_person = bundle.getString("sales_person");
        final String id = bundle.getString("id");

        if (item_id == R.id.nav_gallery) {

            Intent intent = new Intent(Menu.this, Sales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        } else if (item_id == R.id.pending_overdue) {
            Intent intent = new Intent(Menu.this, MainActivity.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();

        } else if (item_id == R.id.nav_sign_out) {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.clear();
            e.commit();

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();

        } else if (item_id == R.id.pending_visit) {
            Intent intent = new Intent(Menu.this, PendingVisit.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();

        } else if (item_id == R.id.sales_vpc) {


            Intent intent = new Intent(Menu.this, PreviousSales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        } else if (item_id == R.id.home) {
            Intent intent = new Intent(Menu.this, com.kluz.vp6.vp1.Menu.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();

        } else if (item_id == R.id.search) {
            Intent intent = new Intent(Menu.this, Anaylsis.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void Num_of_overdue_menu(final String sp) {
        progressDialog.setMessage("Please Wait....");
        showDialog();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://villagepower.info/vpc/menu_app.php";

        StringRequest arrayreq = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject obj = jsonarray.getJSONObject(i);
                                String menu_text = obj.getString("data");
                                String id = obj.getString("id");
                                String vpc = obj.getString("vpc");
                                String sales_person_name = obj.getString("sales_person_name");
                                String icon = obj.getString("icon");

                                /*
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, menu_text);
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);

                                // [START custom_event]
                                Bundle params = new Bundle();
                                params.putString("sales_person_name", sales_person_name);
                                params.putString("full_text", menu_text);
                                mFirebaseAnalytics.logEvent("share_image", params);
                                // [END custom_event]

                                */

                                MenuModel model = new MenuModel(menu_text, id, vpc, sales_person_name, icon);
                                clientList.add(model);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sp);
                return params;
            }

        };

        queue.add(arrayreq);

    }

    private void checkVersion(final String sp){

        RequestQueue queue = Volley.newRequestQueue(this);
        String packageName = "com.kluz.vp6.vp1";
        String url = "http://carreto.pt/tools/android-store-version/?package=";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url+packageName, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            if(response != null && response.has("status") && response.getBoolean("status") && response.has("version")){
                                String latestVersion=  response.getString("version").toString();
                                PackageInfo pinfo = null;
                                try {
                                    pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                String currentVersion = pinfo.versionName;
                                if(latestVersion!=null) {
                                    if (!currentVersion.equalsIgnoreCase(latestVersion)){
                                        if(!isFinishing()){
                                            Intent i = new Intent(getApplicationContext(), Confirm_update.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }else{

                                        Num_of_overdue_menu(sp);

                                    }
                                }

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Not Getting Anything", Toast.LENGTH_LONG).show();

                            }
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO handling error
                    }
                });

        queue.add(jsObjRequest);



    }

    private void getAmountCollected( final String sp){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://villagepower.info/vpc/customer_assoc_analysis.php";
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
                                overdue.setText("Number Of Overdue : "+obj.getString("overdue_num_sp")+" Clients");
                                Sum_of_money.setText("Amount Collected (MTD) : "+obj.getString("total_paid")+" UGX");

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



    private void initCollapsingToolbar() {

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
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
                    collapsingToolbar.setTitle("Home");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
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
    public void onClick(View v) {

        Bundle bundle = getIntent().getExtras();
        final String vpc = bundle.getString("vpc");
        final String sales_person = bundle.getString("sales_person");
        final String id = bundle.getString("id");


        Intent intent = new Intent(getApplicationContext(), Payment.class);
        intent.putExtra("vpc", vpc);
        intent.putExtra("sales_person", sales_person);
        intent.putExtra("id", id);
        startActivity(intent);

    }
}
