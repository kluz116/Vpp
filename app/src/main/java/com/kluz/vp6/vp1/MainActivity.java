package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private WithInAdpter adapter;
    private List<clients> clientList;

    MenuItem sales_res;
    MenuItem previous_month_sales;
    String sales_menu="";
    String sales_previous_month="";


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        final String vpc = bundle.getString("vpc");
        final String sales_person = bundle.getString("sales_person");
        final String sales_vpc = bundle.getString("id");
        final String sp = bundle.getString("id");



        initCollapsingToolbar("Next Payment Date");

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                DecimalFormat mFormat= new DecimalFormat("00");
                String day = mFormat.format(Double.valueOf(dayOfMonth));
                String mon =  mFormat.format(Double.valueOf((month + 1)));
                final String valid_until = ""+year+"-"+mon+"-"+day+"";

                if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
                {
                    getWithIn(sp,valid_until);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Check Your Internet Connection..",Toast.LENGTH_LONG).show();
                }

            }
        });


        clientList = new ArrayList<>();
        adapter = new WithInAdpter(this, clientList);
        recyclerView.setAdapter(adapter);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user);
        ImageView imgProfile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_header);
        txtProfileName.setText(sales_person);

        Menu menu = navigationView.getMenu();
        sales_res = menu.findItem(R.id.nav_gallery);
        previous_month_sales = menu.findItem(R.id.n);

        Num_of_sales_menu(vpc);
        Num_of_previous_sales_men(vpc);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent i = new Intent(MainActivity.this,ChangePassword.class);
            i.putExtra("sales_person", sales_person);
            i.putExtra("id",id );
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
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

            Intent intent = new Intent(MainActivity.this, Sales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();
        } else if (item_id == R.id.pending_overdue) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        } else if (item_id == R.id.nav_sign_out) {
            SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();

        }else if(item_id == R.id.pending_visit){
            Intent intent = new Intent(MainActivity.this, PendingVisit.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id == R.id.sales_vpc){
            Intent intent = new Intent(MainActivity.this, PreviousSales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();
        }else if(item_id==R.id.home){
            Intent intent = new Intent(MainActivity.this, com.kluz.vp6.vp1.Menu.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id == R.id.search) {
            Intent intent = new Intent(MainActivity.this, Anaylsis.class);
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
        //String sales_menu;
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
    private void getWithIn( final String sp, final  String valid_until){
        progressDialog.setMessage("Loading Next Payment Date Of "+valid_until);
        showDialog();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://villagepower.info/vpc/within_app.php";
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
                                clients a = new clients(obj.getString("cid"),obj.getString("clients_name"), obj.getString("default_phone"), obj.getString("image"),obj.getString("cur_overdue"),obj.getString("current_outstanding"), obj.getString("loan_outstanding"), obj.getString("sales_person"), obj.getString("kit"),obj.getString("valid_until"));
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
                params.put("valid_until", valid_until);
                return params;
            }

        };

        queue.add(arrayreq);


    }
    private void initCollapsingToolbar( final String session) {

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
                    collapsingToolbar.setTitle(session);
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
}
