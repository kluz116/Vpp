
package com.kluz.vp6.vp1;

        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
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
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.LinearLayout;
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
 * Created by kluz on 3/2/17.
 */

public class Overdue extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private LinearLayout mroot;
    private WarninigAdapter adapter;
    private List<Warning> clientList;
    ProgressDialog progressDialog;
    String sales;
    Toolbar toolbar;
    String sales_menu="";
    String sales_rev="";
    String vpc;
    String sp;
    String sales_person_name;
    String sales_previous_month="";

    MenuItem sales_res;
    MenuItem previous_month_sales;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning_main_crap);

        mroot = (LinearLayout) findViewById(R.id.warning);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        toolbar.setTitle("Overdue Clients");
        setSupportActionBar(toolbar);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        clientList = new ArrayList<>();
        adapter = new WarninigAdapter(this, clientList);
        recyclerView.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user);


        Menu menu = navigationView.getMenu();
        sales_res = menu.findItem(R.id.nav_gallery);
        previous_month_sales = menu.findItem(R.id.n);

        if (getIntent().getSerializableExtra("client")!=null) {
            MenuModel obj = (MenuModel) getIntent().getSerializableExtra("client");

            sp = obj.getId();
            vpc = obj.getVpc();
            sales_person_name = obj.getSales_person_name();
            if(CheckNetwork.isInternetAvailable(Overdue.this)) //returns true if internet available
            {
                Clients(sp);
                Num_of_sales_menu(vpc);
                Num_of_sales_men(sp,sales_person_name);
                Num_of_previous_sales_men(vpc);
                txtProfileName.setText(sales_person_name);
            }
            else
            {
                Snackbar snack = Snackbar.make(mroot,"Oooops, Please Check Your Internet Connection.....",Snackbar.LENGTH_LONG);
                snack.show();
            }

        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Overdue.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
            Intent i = new Intent(Overdue.this,ChangePassword.class);
            i.putExtra("sales_person", sales_person);
            i.putExtra("id",id );
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
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

    private void Num_of_sales_men(final String sp, final String session){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://villagepower.info/vpc/num_of_sale.php";

        StringRequest arrayreq = new StringRequest(Request.Method.POST,url,

                new Response.Listener<String>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i<jsonarray.length(); i++){
                                // String num_of_sales;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                sales  = obj.getString("sales_made");
                                sales_rev  = obj.getString("revenue");

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
                params.put("id", sp);
                return params;
            }

        };

        queue.add(arrayreq);

    }

    private void Clients(final String sp) {


        progressDialog.setMessage("Please Wait...Loading Data...");
        showDialog();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://villagepower.info/vpc/overdue_app_v1.php";
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
                                //int rank = obj.getInt("cid");
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
                params.put("id", sp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_help, menu);

        return true;
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
        MenuModel obj = (MenuModel) getIntent().getSerializableExtra("client");
        final String vpc = obj.getVpc();
        final String sales_person = obj.getSales_person_name();
        final String id = obj.getId();

        if (item_id == R.id.nav_gallery) {

            Intent intent = new Intent(Overdue.this, Sales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();
        } else if (item_id == R.id.pending_overdue) {
            Intent intent = new Intent(Overdue.this, MainActivity.class);
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
            Intent intent = new Intent(Overdue.this, PendingVisit.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id == R.id.search){
            Intent intent = new Intent(Overdue.this, Anaylsis.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id == R.id.sales_vpc){

            Intent intent = new Intent(Overdue.this, Sales.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id==R.id.home){
            Intent intent = new Intent(Overdue.this, com.kluz.vp6.vp1.Menu.class);
            intent.putExtra("vpc", vpc);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("id",id );
            startActivity(intent);
            finish();

        }else if(item_id == R.id.search) {
            Intent intent = new Intent(Overdue.this, Anaylsis.class);
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
}

