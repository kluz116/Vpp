package com.kluz.vp6.vp1;

/**
 * Created by kluz on 2/22/17.
 */


        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.SharedPreferences;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.*;
        import android.widget.Button;
        import android.widget.EditText;
        import android.content.Intent;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.HashMap;
        import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://villagepower.info/vpc/login_user.php";
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    private Button btnlogin;
    private LinearLayout mroot;
    SharedPreferences sp;
    String Email,Password;
    String latestVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        mroot = (LinearLayout) findViewById(R.id.login);
        btnlogin = (Button) findViewById(R.id.btn_login);


        sp=getSharedPreferences("login",MODE_PRIVATE);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        if (sp.contains("loginInputEmail") && sp.contains("loginInputPassword")) {
            String sales_person = sp.getString("sales_person",null);
            String vpc = sp.getString("user_vpc",null);
            String id = sp.getString("sales_vpc",null);
            String yo =  sp.getString("loginInputEmail",null);
            String yos =  sp.getString("loginInputPassword",null);

            Intent intent = new Intent(LoginActivity.this, Menu.class);
            intent.putExtra("sales_person", sales_person);
            intent.putExtra("vpc", vpc);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();

        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckNetwork.isInternetAvailable(LoginActivity.this)) {
                    //if(loginInputEmail.getText().toString()!="" && loginInputPassword.getText().toString()!=null ){
                        GetLatestVersion(loginInputEmail.getText().toString(), loginInputPassword.getText().toString());
                    //}else{
                        //Snackbar snack = Snackbar.make(mroot,"Try to Fill In Password Or Email",Snackbar.LENGTH_LONG);
                        //snack.show();
                   // }
                }
                else
                {
                    Snackbar snack = Snackbar.make(mroot,"Please Check Your Internet Connection!!!",Snackbar.LENGTH_LONG);
                    snack.show();
                }

            }
        });


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
    private void loginUser( final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in....");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String emails = jObj.getJSONObject("row").getString("email");
                        String passwords = jObj.getJSONObject("row").getString("password");
                        String sales_person = jObj.getJSONObject("row").getString("sales_person");
                        String vpc = jObj.getJSONObject("row").getString("vpc");
                        String id = jObj.getJSONObject("row").getString("id");

                        PackageInfo pinfo = null;
                        try {
                            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        String currentVersion = pinfo.versionName;
                        //String google_play_version = GetLatestVersion();

                        // Launch User activity
                                Intent intent = new Intent(LoginActivity.this,Menu.class);
                                intent.putExtra("sales_person", sales_person);
                                intent.putExtra("vpc", vpc);
                                intent.putExtra("id", id);
                                SharedPreferences.Editor e=sp.edit();
                                e.putString("loginInputEmail",emails);
                                e.putString("loginInputPassword",password);
                                e.putString("sales_person",sales_person);
                                e.putString("user_vpc",vpc);
                                e.putString("sales_vpc",id);
                                e.putString("currentVersion",currentVersion);
                                e.commit();
                               startActivity(intent);
                                finish();

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Snackbar snack = Snackbar.make(mroot,errorMsg,Snackbar.LENGTH_LONG);
                        snack.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snack = Snackbar.make(mroot,"Something went wrong,Check Your Internet Connection."+error,Snackbar.LENGTH_LONG);
                snack.show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void GetLatestVersion(final String email, final String password){

        RequestQueue queue = Volley.newRequestQueue(this);
        String packageName = "com.kluz.vp6.vp1";
        String url = "http://carreto.pt/tools/android-store-version/?package=";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url+packageName, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            if(response != null && response.has("status") && response.getBoolean("status") && response.has("version")){
                                latestVersion=  response.getString("version").toString();
                                PackageInfo pinfo = null;
                                try {
                                    pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                String currentVersion = pinfo.versionName;
                                if(latestVersion!=null) {
                                    progressDialog.setMessage("Logging you in....");
                                    showDialog();
                                    if (!currentVersion.equalsIgnoreCase(latestVersion)){
                                        if(!isFinishing()){
                                            Intent i = new Intent(getApplicationContext(), Confirm_update.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }else{
                                        loginUser(email,password);

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



    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}

