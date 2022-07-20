package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndianStates extends AppCompatActivity implements ExampleAdapter.onItemClickListener {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<Cardlayout> mExampleList;
    private RequestQueue mRequestQueue;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_states);

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


        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                spinner.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    for(int i=1; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String state = jsonObject.getString("state");
                        String confirmed = jsonObject.getString("confirmed");
                        String active = jsonObject.getString("active");
                        String recovered = jsonObject.getString("recovered");
                        String deaths = jsonObject.getString("deaths");

                        mExampleList.add(new Cardlayout(state,confirmed,active,recovered,deaths));
                    }

                    mExampleAdapter = new ExampleAdapter(IndianStates.this,mExampleList);
                    mRecyclerView.setAdapter(mExampleAdapter);
                    mExampleAdapter.setOnItemClickListener(IndianStates.this);

                } catch (JSONException e) {
//                    Toast.makeText(IndianStates.this, "Could Not Fetch the Data ! ", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(IndianStates.this, "Click on Refresh to get latest Info ", Toast.LENGTH_SHORT).show();
//                    Intent i  =new Intent(IndianStates.this,MainActivity.class);
//                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Intent i  =new Intent(IndianStates.this,MainActivity.class);
                Toast.makeText(IndianStates.this, "Could Not Fetch the Data ! ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onItemClick(int position) {

//        Cardlayout clickedItem = mExampleList.get(position);
//        String state = clickedItem.getmState();
//        Toast.makeText(this, state, Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(IndianStates.this,DetailState.class);
//        i.putExtra("State",state);
//        startActivity(i);

    }
}
