package com.example.volleywithjson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

public class MainActivity extends AppCompatActivity {

    // json object response url
    private String urlJsonObj = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=API_KEY";

    // json array response url
    private String urlJsonArray = "https://api.androidhive.info/volley/person_array.json";

    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnMakeObjectRequest, btnMakeArrayRequest;

    private ProgressBar mProgressBar;

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add activity resource id to view
        btnMakeObjectRequest = (Button) findViewById(R.id.btnObjRequest);
        btnMakeArrayRequest = (Button) findViewById(R.id.btnArrayRequest);
        txtResponse = (TextView) findViewById(R.id.txtResponse);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        // mProgressBar = new ProgressBar(this);  // Testing progressBars
        pDialog = new ProgressDialog(this);  // assign context for progressDialog
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //set click for btnMakeObjectRequest
        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call JsonObject method
                makeJsonObjectRequest();
            }
        });

//        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //call JsonArray method
//                makeJsonArrayRequest();
//            }
//        });

    }

    // JsonObjectRequest method
    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    // Parsing json object response
                    String id = response.getString("id");
                    String name = response.getString("name");
                    String author = response.getString("author");
                    String title = response.getString("title");
                    String description = response.getString("description");

                    jsonResponse = "";
                    jsonResponse += "ID: " + id + "\n\n";
                    jsonResponse += "Name: " + name + "\n\n";
                    jsonResponse += "Author: " + author + "\n\n";
                    jsonResponse += "Title: " + title + "\n\n";
                    jsonResponse += "Description: " + description + "\n\n";

                    txtResponse.setText(jsonResponse);

                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),

                        error.getMessage(), Toast.LENGTH_SHORT).show();

                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    // JsonArrayRequest method
    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest request = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, response.toString());
                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person
                                        .getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "Email: " + email + "\n\n";
                                jsonResponse += "Home: " + home + "\n\n";
                                jsonResponse += "Mobile: " + mobile + "\n\n\n";

                            }

                            txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    // showpDialog method with check for pDialog state
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    // hidepDialog method with check for pDialog state
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
