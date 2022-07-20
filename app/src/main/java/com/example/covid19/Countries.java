package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Countries extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<Cardlayout> mExampleList;
    private RequestQueue mRequestQueue;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);

        spinner.setVisibility(View.VISIBLE);
        sendApiRequest();


    }

    private void sendApiRequest() {


        String url = "https://corona.lmao.ninja/v2/countries";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                spinner.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String state = jsonObject.getString("country");
                        String confirmed = jsonObject.getString("cases");
                        String active = jsonObject.getString("active");
                        String recovered = jsonObject.getString("recovered");
                        String deaths = jsonObject.getString("deaths");

                        mExampleList.add(new Cardlayout(state,confirmed,active,recovered,deaths));

                    }

                    mExampleAdapter = new ExampleAdapter(Countries.this,mExampleList);
                    mRecyclerView.setAdapter(mExampleAdapter);

                } catch (JSONException e) {
//                    Toast.makeText(Countries.this, "Could Not Fetch the Data ! ", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Countries.this, "Click on Refresh to get latest Info ", Toast.LENGTH_SHORT).show();
//                    Intent i  =new Intent(Countries.this,MainActivity.class);
//                    finish();
//                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent i  =new Intent(Countries.this,MainActivity.class);
                Toast.makeText(Countries.this, "Could Not Fetch the Data ! ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mRequestQueue.add(stringRequest);

    }
}
