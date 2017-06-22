package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kluz on 5/8/17.
 */

public class ChangePassword extends AppCompatActivity{
    LinearLayout mroot;
    EditText old_pass, new_pass,con_password;
    Button confirm;
    ProgressDialog progressDialog;
    private static final String URL = "http://villagepower.info/vpc/change_pass.php";
    private static final String TAG = "ChangePassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass_app_bar);
        mroot = (LinearLayout) findViewById(R.id.change_pwd);
        old_pass = (EditText) findViewById(R.id.login_input_password);
        new_pass = (EditText) findViewById(R.id.new_password);
        con_password = (EditText) findViewById(R.id.confirm_password);
        confirm = (Button) findViewById(R.id.btn_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");
        final String sales_person = bundle.getString("sales_person");

        toolbar.setTitle("Change Password");

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        if(CheckNetwork.isInternetAvailable(ChangePassword.this)) //returns true if internet available
        {

            confirm.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String old_password = old_pass.getText().toString();
                    String new_passsword = new_pass.getText().toString();
                    String confirm_password = con_password.getText().toString();
                    changePassword(id,old_password,new_passsword,confirm_password);

                }
            });

        }
        else
        {
            Snackbar snack = Snackbar.make(mroot,"Oooops, Please Check Your Internet Connection.....",Snackbar.LENGTH_LONG);
            snack.show();
        }

    }

    private void changePassword(final String id, final  String old_password,final String new_password,final String confirm_password) {
        String cancel_req_tag = "change_password";
        progressDialog.setMessage("Changing Password....");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Snackbar snack = Snackbar.make(mroot,"Successfuly Changed Password..",Snackbar.LENGTH_LONG);
                        snack.show();
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Register Response: " + error.toString());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("old_password",old_password);
                params.put("new_password",new_password);
                params.put("confirm_password",confirm_password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
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
