package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class weeklyFragment extends Fragment {

    private JSONObject data;
    weeklyFragment(JSONObject data) {
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag1_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String temp;
        int resid = R.drawable.clear_day;
        JSONArray tdata;
        try {
            temp = data.getString("icon");
            switch(temp)
            {
                case "clear-day": resid = R.drawable.clear_day; break;
                case "clear-night": resid = R.drawable.clear_night; break;
                case "fog": resid = R.drawable.fog; break;
                case "sleet": resid = R.drawable.sleet; break;
                case "wind": resid = R.drawable.wind; break;
                case "rain": resid = R.drawable.rain; break;
                case "cloudy": resid = R.drawable.cloudy; break;
                case "snow": resid = R.drawable.snow; break;
                case "partly-cloudy-day": resid = R.drawable.partly_cloudy_day; break;
                case "partly-cloudy-night": resid = R.drawable.partly_cloudy_night; break;
            }
            ((ImageView)view.findViewById(R.id.w_summary_icon)).setImageResource(resid);
            ((TextView)view.findViewById(R.id.w_summary)).setText(data.getString("summary"));
            tdata = data.getJSONArray("data");
            LineChart chart = ((LineChart) view.findViewById(R.id.tempChart));
            List<Entry> tempMax = new ArrayList<Entry>();
            List<Entry> tempMin = new ArrayList<Entry>();
            for(int i=0; i<tdata.length(); i++) {
                tempMax.add(new Entry(i, tdata.getJSONObject(i).getInt("temperatureHigh")));
                tempMin.add(new Entry(i, tdata.getJSONObject(i).getInt("temperatureLow")));
            }
            LineDataSet tempMaxSet = new LineDataSet(tempMax, "Maximum Temperature");
            tempMaxSet.setColor(chart.getResources().getColor(R.color.yellowCustom));
            tempMaxSet.setDrawValues(false);
            LineDataSet tempMinSet = new LineDataSet(tempMin, "Minimum Temperature");
            tempMinSet.setColor(chart.getResources().getColor(R.color.purpleCustom));
            tempMinSet.setDrawValues(false);
            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(tempMaxSet);
            dataSets.add(tempMinSet);
            LineData Chartdata = new LineData(dataSets);
            chart.setData(Chartdata);
            chart.getLegend().setTextColor(chart.getResources().getColor(android.R.color.white));
            chart.getXAxis().setTextColor(chart.getResources().getColor(android.R.color.white));
            chart.getAxisLeft().setTextColor(chart.getResources().getColor(android.R.color.white));
            chart.getAxisRight().setTextColor(chart.getResources().getColor(android.R.color.white));
            chart.getDescription().setEnabled(false);
            chart.invalidate();
        } catch(JSONException e) { e.printStackTrace();}
    }
}
