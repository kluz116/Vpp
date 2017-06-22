package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kluz on 3/4/17.
 */

public class Valid_Till extends AppCompatActivity  {

    DatePicker datePicker;
    EditText reasons;
    Button confirm;
    RadioGroup genderRadioGroup;
    ProgressDialog progressDialog;
    private static final String URL = "http://villagepower.info/vpc/add_warning_call.php";
    private static final String TAG = "Valid_Till";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valid_main);


        datePicker = (DatePicker) findViewById(R.id.datePicker);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        reasons = (EditText) findViewById(R.id.val);
        confirm = (Button) findViewById(R.id.btn_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        if (getIntent().getSerializableExtra("client")!=null){

            clients obj = (clients) getIntent().getSerializableExtra("client");
            toolbar.setTitle(""+obj.getName() +" | "+obj.getKit());

            DecimalFormat mFormat= new DecimalFormat("00");
            String day = mFormat.format(Double.valueOf(datePicker.getDayOfMonth()));
            String mon =  mFormat.format(Double.valueOf((datePicker.getMonth() + 1)));
            int year = datePicker.getYear();



            final String date = ""+year+"-"+mon+"-"+day+"";
            final String client_name = obj.getName();
            final String sales_p = obj.getSales_p();
            final String cid = obj.getCid();

            confirm.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                    String status;
                    if(selectedId == R.id.yes)
                        status = "Yes";
                    else
                        status = "No";
                    final String comment = reasons.getText().toString();
                    addComplaint(cid,client_name,date, status, comment, sales_p);
                    //Toast.makeText(getApplicationContext(), " "+cid +client_name +date +status +comment +sales_p, Toast.LENGTH_SHORT).show();

                }
            });


        }


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

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
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }else if(got_id==R.id.change_password){
            Intent i = new Intent(Valid_Till.this,ChangePassword.class);
            i.putExtra("sales_person", sales_person);
            i.putExtra("id",id );
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private void addComplaint(final String cid, final String client_name, final String date,  final String status, final String comment, final String sales_p) {
        String cancel_req_tag = "login";
        progressDialog.setMessage("Adding calls data..Please Hold On...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), " successfully Added Data For "+client_name, Toast.LENGTH_SHORT).show();

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
                params.put("cid", cid);
                params.put("client_name",client_name);
                params.put("date",date);
                params.put("status",status);
                params.put("comment",comment);
                params.put("sales_p",sales_p);
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
