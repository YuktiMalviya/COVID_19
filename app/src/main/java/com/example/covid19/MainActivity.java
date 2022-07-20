package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner state_spinner;
    ProgressDialog dialog;

    private RequestQueue mRequestQueue;
    FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    String active,recovered,confirmed,deceased;
    PieChart pieChart;
    TextView user_text;

    CircleImageView circleImageView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        active = "0";
        recovered = "0";
        confirmed = "0";
        deceased = "0";


        state_spinner = findViewById(R.id.state_spinner);
        ArrayList<CustomItemSpinner> customList = new ArrayList<>();
        customList.add(new CustomItemSpinner("India"));
        customList.add(new CustomItemSpinner("Maharashtra"));
        customList.add(new CustomItemSpinner("Gujarat"));
        customList.add(new CustomItemSpinner("Tamil Nadu"));
        customList.add(new CustomItemSpinner("Delhi"));
        customList.add(new CustomItemSpinner("Rajasthan"));
        customList.add(new CustomItemSpinner("Madhya Pradesh"));
        customList.add(new CustomItemSpinner("Uttar Pradesh"));
        customList.add(new CustomItemSpinner("Andhra Pradesh"));
        customList.add(new CustomItemSpinner("West Bengal"));
        customList.add(new CustomItemSpinner("Punjab"));
        customList.add(new CustomItemSpinner("Telangana"));
        customList.add(new CustomItemSpinner("Jammu and Kashmir"));
        customList.add(new CustomItemSpinner("Karnataka"));
        customList.add(new CustomItemSpinner("Haryana"));
        customList.add(new CustomItemSpinner("Bihar"));
        customList.add(new CustomItemSpinner("Kerala"));
        customList.add(new CustomItemSpinner("Odisha"));
        customList.add(new CustomItemSpinner("Chandigarh"));
        customList.add(new CustomItemSpinner("Jharkhand"));
        customList.add(new CustomItemSpinner("Tripura"));
        customList.add(new CustomItemSpinner("Uttarakhand"));
        customList.add(new CustomItemSpinner("Assam"));
        customList.add(new CustomItemSpinner("Chhattisgarh"));
        customList.add(new CustomItemSpinner("Himachal Pradesh"));
        customList.add(new CustomItemSpinner("Ladakh"));
        customList.add(new CustomItemSpinner("Andaman and Nicobar Islands"));
        customList.add(new CustomItemSpinner("Meghalaya"));
        customList.add(new CustomItemSpinner("Puducherry"));
        customList.add(new CustomItemSpinner("Goa"));
        customList.add(new CustomItemSpinner("Manipur"));
        customList.add(new CustomItemSpinner("Mizoram"));
        customList.add(new CustomItemSpinner("Arunachal Pradesh"));
        //customList.add(new CustomItemSpinner("Dadra and Nagar Haveli and Daman and Diu"));
        customList.add(new CustomItemSpinner("Nagaland"));
        customList.add(new CustomItemSpinner("Daman and Diu"));
        customList.add(new CustomItemSpinner("Lakshadweep"));
        customList.add(new CustomItemSpinner("Sikkim"));


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this,customList);

        if (state_spinner != null){
            state_spinner.setAdapter(customSpinnerAdapter);
            state_spinner.setOnItemSelectedListener(this);
        }


        firebaseUser = mAuth.getCurrentUser();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        circleImageView = findViewById(R.id.profile_image);

        Toast.makeText(this, "Swipe to Refresh the Data !", Toast.LENGTH_SHORT).show();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendApiRequest();
            }
        });

//        mRequestQueue = Volley.newRequestQueue(this);
          mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        pieChart = findViewById(R.id.pie_chart);
        user_text = findViewById(R.id.user_text);

        if (isNetworkAvailable(this)) {

            if (mAuth.getCurrentUser() != null) {

                if (firebaseUser.getPhotoUrl()!=null){

                    Glide.with(this)
                            .load(firebaseUser.getPhotoUrl())
                            .into(circleImageView);
                }

                if (firebaseUser != null) {
                    if (firebaseUser.getDisplayName().isEmpty()) {
                        user_text.setText("Guest");
                    } else {
                        if (firebaseUser.getDisplayName().length() > 10) {
                            user_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                            user_text.setText(firebaseUser.getDisplayName());
                        } else {

                            user_text.setText(firebaseUser.getDisplayName());
                        }
                    }
                } else {
                    user_text.setText("Guest");
                }
            } else {
                user_text.setText("Guest");
            }

            //sendApiRequest();

        } else {

            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean isNetworkAvailable(Context context) {

        if (context == null) return false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ");
        return false;
    }

    private void sendApiRequest() {


        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    active = jsonObject.getString("active");
                    recovered = jsonObject.getString("recovered");
                    confirmed = jsonObject.getString("confirmed");
                    deceased = jsonObject.getString("deaths");

                    createChart(confirmed,active,recovered,deceased);

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Taking longer than Usual !", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                spinner.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Could Not Fetch the Data ! ", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Click on Refresh to get latest Info ", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    public void createChart(String cnf, String act, String rec, String dec){

        //swipeRefreshLayout.setRefreshing(true);

        if (cnf.isEmpty() || act.isEmpty() || rec.isEmpty() || dec.isEmpty()){

            cnf = "50";
            act = "50";
            rec = "50";
            dec = "50";

            pieChart.animateXY(3000,3000);

            List<PieEntry> pieEntries = new ArrayList<>();

            pieEntries.add(new PieEntry(Integer.valueOf(act),"active"));
            pieEntries.add(new PieEntry(Integer.valueOf(rec),"recovered"));
            pieEntries.add(new PieEntry(Integer.valueOf(cnf),"confirmed"));
            pieEntries.add(new PieEntry(Integer.valueOf(dec),"deceased"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries,"Cases");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();

            swipeRefreshLayout.setRefreshing(false);

        }
        else {

            swipeRefreshLayout.setRefreshing(true);

            pieChart.animateXY(5000,5000);

            List<PieEntry> pieEntries = new ArrayList<>();

            pieEntries.add(new PieEntry(Integer.valueOf(act),"active"));
            pieEntries.add(new PieEntry(Integer.valueOf(rec),"recovered"));
            pieEntries.add(new PieEntry(Integer.valueOf(cnf),"confirmed"));
            pieEntries.add(new PieEntry(Integer.valueOf(dec),"deceased"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries,"Cases");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);

            Description description = new Description();
            description.setText("Cases In India");
            pieChart.setDescription(description);
            pieChart.invalidate();

            swipeRefreshLayout.setRefreshing(false);
        }

    } // creating chart

    public void getstatedata(View view) {

        if (isNetworkAvailable(this)){
            Intent i = new Intent(MainActivity.this,IndianStates.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    public void getworlddata(View view) {

        if (isNetworkAvailable(this)){
            Intent i = new Intent(MainActivity.this,Countries.class);
            startActivity(i);
            //Toast.makeText(this, "Coming Soon !", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void tips(View view) {

        if (isNetworkAvailable(this))
        {
//            Toast.makeText(this, "Coming Soon !", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,Tips.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void selfassesss(View view) {

        if (isNetworkAvailable(this))
        {
//            Toast.makeText(this, "Coming Soon !", Toast.LENGTH_SHORT).show();
            if (mAuth.getCurrentUser()!=null){
                Intent i  = new Intent(MainActivity.this,selfassess.class);
                startActivity(i);
            }
            else {
                Toast.makeText(this, "Please Login, to get Started", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void user_profile(View view) {

        if (isNetworkAvailable(this)){

            if (mAuth.getCurrentUser() == null)
            {
                Intent i = new Intent(MainActivity.this,loginaccount.class);
                startActivity(i);
                finish();
            }
            else {

                Intent i = new Intent(MainActivity.this,User_profile.class);
                startActivity(i);
            }
        }
        else {
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    public void video_library(View view) {
       if (isNetworkAvailable(this)){
           Intent i = new Intent(MainActivity.this,Video_Media.class);
           startActivity(i);
       }
       else {
           No_Internet_Diaglog();
       }
    }

    private void No_Internet_Diaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("No Internet !");
        final View customLayout = getLayoutInflater().inflate(R.layout.no_internet,null,false);
        builder.setView(customLayout);

        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (alertInternet()){
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
                    No_Internet_Diaglog();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean alertInternet(){
        if (isNetworkAvailable(this)){return true;}
        else {return false;}
    }

    public void who_info(View view) {

        Intent who = new Intent(MainActivity.this,Who_Infographics.class);
        startActivity(who);
    }

    public void dos_donts(View view) {
        Intent i = new Intent(MainActivity.this,Dos_Donts.class);
        startActivity(i);
    }

    public void sendStateApiRequest(final String rState)  {

        String url = "https://api.covid19india.org/data.json";

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String state = jsonObject.getString("state");

                        if(rState.equals(state)){

                            String confirmed = jsonObject.getString("confirmed");
                            String active = jsonObject.getString("active");
                            String recovered = jsonObject.getString("recovered");
                            String deaths = jsonObject.getString("deaths");
                            createChart(confirmed,active,recovered,deaths);
                            dialog.dismiss();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                spinner.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Could Not Fetch the Data ! ", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Click on Refresh to get latest Info ", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        CustomItemSpinner items = (CustomItemSpinner)adapterView.getSelectedItem();
        String state = items.getSpinnerText();
        if (isNetworkAvailable(this)){
            if (state.equals("India")){
                sendStateApiRequest("Total");
            }
            else {
                sendStateApiRequest(state);
            }
        }
        else {
            Toast.makeText(this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

