package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    private RequestQueue myQueue;
    JSONObject data;
    String location, deg, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);

        Intent intent = getIntent();
        location = intent.getStringExtra("location");

        Toolbar toolbar = findViewById(R.id.result_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(location);

        myQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        getDeg(location);

        findViewById(R.id.r_card1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                Log.d("ResultActivity", "here");
                intent.putExtra("data", data.toString());
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });

        final FloatingActionButton fab = findViewById(R.id.r_favorite);
        final SharedPreferences fsp = getSharedPreferences("fav", MODE_PRIVATE);
        if(fsp.contains(location)) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.dislike));
            type = "add";
        }
        else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.like));
            type = "remove";
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = fsp.edit();
                Context context = getApplicationContext();
                if(fsp.contains(location)) {
                    editor.remove(location);
                    editor.apply();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.like));
                    type = "remove";
                    Toast.makeText(context, location+" was removed form favorites", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString(location, deg);
                    editor.commit();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.dislike));
                    Toast.makeText(context, location+" was added to favorites", Toast.LENGTH_SHORT).show();
                    type = "add";
                }

            }
        });
    }

    private void getDeg(String loc) {
        final String location = loc;
        String url = "http://dzyhw8.us-east-2.elasticbeanstalk.com/api/geocode/"+location;
        JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String lat, lon;
                            JSONObject temp = response.getJSONArray("results").getJSONObject(0);
                            temp = temp.getJSONObject("geometry").getJSONObject("location");
                            lat = String.valueOf(temp.getDouble("lat"));
                            lon = String.valueOf(temp.getDouble("lng"));
                            deg = lat + "/" + lon;
                            getWeather(deg);
                        } catch (JSONException e) { e.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ResultActivity", "getLoc Error");
                    }
                }
        );
        myQueue.add(json_request);
    }

    private void getWeather(String deg) {
        String url = "http://dzyhw8.us-east-2.elasticbeanstalk.com/api/forecast/"+deg;
        JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        data = response;
                        setLayout(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ResultActivity", "getWeather Error");
                    }
                }
        );
        myQueue.add(json_request);
    }

    void setLayout(JSONObject response) {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            int[] card3_date = {R.id.r_c30_date, R.id.r_c31_date, R.id.r_c32_date, R.id.r_c33_date,
                    R.id.r_c34_date, R.id.r_c35_date, R.id.r_c36_date, R.id.r_c37_date};
            int[] card3_icon = {R.id.r_c30_icon, R.id.r_c31_icon, R.id.r_c32_icon, R.id.r_c33_icon,
                    R.id.r_c34_icon, R.id.r_c35_icon, R.id.r_c36_icon, R.id.r_c37_icon};
            int[] card3_mintemp = {R.id.r_c30_mintemp, R.id.r_c31_mintemp, R.id.r_c32_mintemp, R.id.r_c33_mintemp,
                    R.id.r_c34_mintemp, R.id.r_c35_mintemp, R.id.r_c36_mintemp, R.id.r_c37_mintemp};
            int[] card3_maxtemp = {R.id.r_c30_maxtemp, R.id.r_c31_maxtemp, R.id.r_c32_maxtemp, R.id.r_c33_maxtemp,
                    R.id.r_c34_maxtemp, R.id.r_c35_maxtemp, R.id.r_c36_maxtemp, R.id.r_c37_maxtemp};

            ((TextView) findViewById(R.id.r_c1_location)).setText(location);
            JSONObject tdata = response.getJSONObject("currently");
            String temp = tdata.getString("icon");
            ((ImageView) findViewById(R.id.r_c1_icon)).setImageResource(getIconid(temp));
            temp = String.valueOf(tdata.getInt("temperature")).concat("°F");
            ((TextView) findViewById(R.id.r_c1_temperature)).setText(temp);
            temp = tdata.getString("summary");
            ((TextView) findViewById(R.id.r_c1_summary)).setText(temp);
            temp = String.valueOf((int)tdata.getDouble("humidity")*100).concat("%");
            ((TextView) findViewById(R.id.r_humidity_value)).setText(temp);
            temp = df.format(tdata.getDouble("windSpeed")).concat(" mph");
            ((TextView) findViewById(R.id.r_wind_speed_value)).setText(temp);
            temp = df.format(tdata.getDouble("visibility")).concat(" km");
            ((TextView) findViewById(R.id.r_visibility_value)).setText(temp);
            temp = df.format(tdata.getDouble("pressure")).concat(" mb");
            ((TextView) findViewById(R.id.r_pressure_value)).setText(temp);

            int time;
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            JSONArray tarray = response.getJSONObject("daily").getJSONArray("data");
            for(int i=0; i<8; i++) {
                time = tarray.getJSONObject(i).getInt("time");
                date.setTime(time*1000L);
                ((TextView) findViewById(card3_date[i])).setText(sdf.format(date));
                temp = String.valueOf(tarray.getJSONObject(i).getInt("temperatureMin"));
                ((TextView) findViewById(card3_mintemp[i])).setText(temp);
                temp = String.valueOf(tarray.getJSONObject(i).getInt("temperatureMax"));
                ((TextView) findViewById(card3_maxtemp[i])).setText(temp);
                temp = tarray.getJSONObject(i).getString("icon");
                ((ImageView) findViewById(card3_icon[i])).setImageResource(getIconid(temp));
            }
        } catch(JSONException e) {e.printStackTrace();}
        findViewById(R.id.r_prog_layout).setVisibility(View.GONE);
    }

    private int getIconid(String name) {
        int iconid = R.drawable.clear_day;
        switch(name) {
            case "clear-day": iconid = R.drawable.clear_day; break;
            case "clear-night": iconid = R.drawable.clear_night; break;
            case "fog": iconid = R.drawable.fog; break;
            case "sleet": iconid = R.drawable.sleet; break;
            case "wind": iconid = R.drawable.wind; break;
            case "rain": iconid = R.drawable.rain; break;
            case "cloudy": iconid = R.drawable.cloudy; break;
            case "snow": iconid = R.drawable.snow; break;
            case "partly-cloudy-day": iconid = R.drawable.partly_cloudy_day; break;
            case "partly-cloudy-night": iconid = R.drawable.partly_cloudy_night; break;
        }
        return iconid;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.putExtra("location", location);
            intent.putExtra("deg", deg);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
