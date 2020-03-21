package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class summaryFragment extends Fragment {

    private JSONObject data;
    String location, deg;
    RequestQueue myQueue;
    summaryFragment(String location, String deg) {
        this.location = location;
        this.deg = deg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.s_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myQueue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        getWeather(deg);

        if(location.equals(getResources().getString(R.string.curloc))) {
            view.findViewById(R.id.s_favorite).setVisibility(View.GONE);
        }

        view.findViewById(R.id.card1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("data", data.toString());
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });

        final FloatingActionButton fab = view.findViewById(R.id.s_favorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences fsp = getActivity().getSharedPreferences("fav", Context.MODE_PRIVATE);
                if(fsp.contains(location)) {
                    SharedPreferences.Editor editor = fsp.edit();
                    editor.remove(location);
                    editor.apply();
                    Context context = getActivity().getApplicationContext();
                    Toast.makeText(context, location+" was removed form favorites", Toast.LENGTH_SHORT).show();
                    ((SummaryActivity)getActivity()).adapter.removeFragment(location);
                }
            }
        });
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

    private void getWeather(String deg) {
        String url = "http://dzyhw8.us-east-2.elasticbeanstalk.com/api/forecast/"+deg;
        JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        data = response;
                        setLayout(response);
                        getView().findViewById(R.id.s_prog_layout).setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                }
        );
        myQueue.add(json_request);
    }

    private void setLayout(JSONObject response) {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            int[] card3_date = {R.id.c30_date, R.id.c31_date, R.id.c32_date, R.id.c33_date,
                    R.id.c34_date, R.id.c35_date, R.id.c36_date, R.id.c37_date};
            int[] card3_icon = {R.id.c30_icon, R.id.c31_icon, R.id.c32_icon, R.id.c33_icon,
                    R.id.c34_icon, R.id.c35_icon, R.id.c36_icon, R.id.c37_icon};
            int[] card3_mintemp = {R.id.c30_mintemp, R.id.c31_mintemp, R.id.c32_mintemp, R.id.c33_mintemp,
                    R.id.c34_mintemp, R.id.c35_mintemp, R.id.c36_mintemp, R.id.c37_mintemp};
            int[] card3_maxtemp = {R.id.c30_maxtemp, R.id.c31_maxtemp, R.id.c32_maxtemp, R.id.c33_maxtemp,
                    R.id.c34_maxtemp, R.id.c35_maxtemp, R.id.c36_maxtemp, R.id.c37_maxtemp};

            ((TextView) getView().findViewById(R.id.c1_location)).setText(location);
            JSONObject tdata = response.getJSONObject("currently");
            String temp = tdata.getString("icon");
            ((ImageView) getView().findViewById(R.id.c1_icon)).setImageResource(getIconid(temp));
            temp = String.valueOf(tdata.getInt("temperature")).concat("°F");
            ((TextView) getView().findViewById(R.id.c1_temperature)).setText(temp);
            temp = tdata.getString("summary");
            ((TextView) getView().findViewById(R.id.c1_summary)).setText(temp);
            temp = String.valueOf((int)tdata.getDouble("humidity")*100).concat("%");
            ((TextView) getView().findViewById(R.id.humidity_value)).setText(temp);
            temp = df.format(tdata.getDouble("windSpeed")).concat(" mph");
            ((TextView) getView().findViewById(R.id.wind_speed_value)).setText(temp);
            temp = df.format(tdata.getDouble("visibility")).concat(" km");
            ((TextView) getView().findViewById(R.id.visibility_value)).setText(temp);
            temp = df.format(tdata.getDouble("pressure")).concat(" mb");
            ((TextView) getView().findViewById(R.id.pressure_value)).setText(temp);

            int time;
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            JSONArray tarray = response.getJSONObject("daily").getJSONArray("data");
            for(int i=0; i<8; i++) {
                time = tarray.getJSONObject(i).getInt("time");
                date.setTime(time * 1000L);
                ((TextView) getView().findViewById(card3_date[i])).setText(sdf.format(date));
                temp = String.valueOf(tarray.getJSONObject(i).getInt("temperatureMin"));
                ((TextView) getView().findViewById(card3_mintemp[i])).setText(temp);
                temp = String.valueOf(tarray.getJSONObject(i).getInt("temperatureMax"));
                ((TextView) getView().findViewById(card3_maxtemp[i])).setText(temp);
                temp = tarray.getJSONObject(i).getString("icon");
                ((ImageView) getView().findViewById(card3_icon[i])).setImageResource(getIconid(temp));
            }
        } catch(JSONException e) { e.printStackTrace();}
    }
}