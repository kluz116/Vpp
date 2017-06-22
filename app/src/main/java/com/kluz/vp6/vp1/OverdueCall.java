package com.kluz.vp6.vp1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
 * Created by kluz on 4/30/17.
 */

public class OverdueCall extends AppCompatActivity {
    DatePicker datePicker;
    EditText get_reason,get_comment;
    private Button add_complaint;
    RadioGroup genderRadioGroup;
    ProgressDialog progressDialog;
    private static final String URL = "http://villagepower.info/vpc/add_overdue_complaint_app.php";
    private static final String TAG = "OverdueCall";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overdue_app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        Bundle bundle = getIntent().getExtras();
        final String cid = bundle.getString("cid");
        final String sales_person = bundle.getString("sales_person");
        final String name = bundle.getString("name");
        final String phone = bundle.getString("phone");
        final String kit = bundle.getString("kit");
        final String days = bundle.getString("days");
        toolbar.setTitle(name);

        datePicker = (DatePicker) findViewById(R.id.calendarView);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        get_reason = (EditText) findViewById(R.id.get_reason);
        get_comment = (EditText) findViewById(R.id.get_comment);
        add_complaint = (Button) findViewById(R.id.add_complaint);
        if(CheckNetwork.isInternetAvailable(OverdueCall.this))
        {

            DecimalFormat mFormat= new DecimalFormat("00");
            String day = mFormat.format(Double.valueOf(datePicker.getDayOfMonth()));
            String mon =  mFormat.format(Double.valueOf((datePicker.getMonth() + 1)));
            int year = datePicker.getYear();
            final String date_called = ""+year+"-"+mon+"-"+day+"";

                add_complaint.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                        String status;
                        if(selectedId == R.id.yes)
                            status = "Yes";
                        else
                            status = "No";

                        final String comment = get_comment.getText().toString();
                        final String reason = get_reason.getText().toString();
                        addOverdueCall(cid,name,phone,kit,days,date_called, status,reason,comment,sales_person);


                    }
                });




        }
        else
        {
            Toast.makeText(OverdueCall.this,"Check Your Internet Connection..",Toast.LENGTH_LONG).show();
        }

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void addOverdueCall(final String cid, final String name,final String phone,final String kit,final String days, final String date_called, final String status,final String reason, final String comment, final String sales_person) {
        String cancel_req_tag = "complaints";
        progressDialog.setMessage("Adding calls data..");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), " Successfully Saved Data For "+name, Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("cid",cid);
                params.put("name",name);
                params.put("phone",phone);
                params.put("kit",kit);
                params.put("days",days);
                params.put("date_called",date_called);
                params.put("status",status);
                params.put("reason",reason);
                params.put("comment",comment);
                params.put("sales_person",sales_person);
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
