package com.kluz.vp6.vp1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by kluz on 6/8/17.
 */

public class Confirm_update extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout mroot;
    Button update;
    TextView update_text;


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_update);
        update_text = (TextView) findViewById(R.id.textView3);
        update = (Button) findViewById(R.id.button2);
        update.setOnClickListener(this);

        GetLatestVersion();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Confirm_update.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kluz.vp6.vp1")));
        finish();

    }

    private void GetLatestVersion(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String packageName = "com.kluz.vp6.vp1";
        String url = "http://carreto.pt/tools/android-store-version/?package=";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url+packageName, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            if(response != null && response.has("status") && response.getBoolean("status") && response.has("version")){
                                PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                String currentVersion = pinfo.versionName;
                                String latestVersion =  response.getString("version").toString();

                                update_text.setText("(App Latest Version "+latestVersion+")");
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
}
