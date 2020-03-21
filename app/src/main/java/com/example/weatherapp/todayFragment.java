package com.example.weatherapp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class todayFragment extends Fragment {

    private JSONObject data;
    todayFragment(JSONObject data) {
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag0_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String temp, icon_name = "clear day";
        int resid = R.drawable.clear_day;
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            temp = df.format(data.getDouble("windSpeed")).concat(" mph");
            ((TextView)view.findViewById(R.id.d_wind_speed_value)).setText(temp);
            temp = df.format(data.getDouble("pressure")).concat(" mb");
            ((TextView)view.findViewById(R.id.d_pressure_value)).setText(temp);
            temp = df.format(data.getDouble("precipProbability")).concat(" mmph");
            ((TextView)view.findViewById(R.id.d_precipitation_value)).setText(temp);
            temp = String.valueOf(data.getInt("temperature")).concat("°F");
            ((TextView)view.findViewById(R.id.d_temperature_value)).setText(temp);
            temp = String.valueOf((int)data.getDouble("humidity")*100).concat("%");
            ((TextView)view.findViewById(R.id.d_humidity_value)).setText(temp);
            temp = df.format(data.getDouble("visibility")).concat(" km");
            ((TextView)view.findViewById(R.id.d_visibility_value)).setText(temp);
            temp = String.valueOf((int)data.getDouble("cloudCover")*100).concat("%");
            ((TextView)view.findViewById(R.id.d_cloud_cover_value)).setText(temp);
            temp = df.format(data.getDouble("ozone")).concat(" DU");
            ((TextView)view.findViewById(R.id.d_ozone_value)).setText(temp);
            temp = data.getString("icon");
            switch(temp)
            {
                case "clear-day": resid = R.drawable.clear_day; icon_name = "clear day"; break;
                case "clear-night": resid = R.drawable.clear_night; icon_name = "clear night"; break;
                case "fog": resid = R.drawable.fog; icon_name = "fog"; break;
                case "sleet": resid = R.drawable.sleet; icon_name = "sleet"; break;
                case "wind": resid = R.drawable.wind; icon_name = "wind"; break;
                case "rain": resid = R.drawable.rain; icon_name = "rain"; break;
                case "cloudy": resid = R.drawable.cloudy; icon_name = "cloudy"; break;
                case "snow": resid = R.drawable.snow; icon_name = "snow"; break;
                case "partly-cloudy-day": resid = R.drawable.partly_cloudy_day; icon_name = "cloudy day"; break;
                case "partly-cloudy-night": resid = R.drawable.partly_cloudy_night; icon_name = "cloudy night"; break;
            }
            ((ImageView)view.findViewById(R.id.d_icon)).setImageResource(resid);
            ((TextView)view.findViewById(R.id.d_icon_name)).setText(icon_name);
        } catch(JSONException e) { e.printStackTrace();}
    }
}