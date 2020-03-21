package com.example.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private RequestQueue myQueue;
    private String location;
    private JSONObject data;
    private List<String> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        myQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        getPhotos(location);

        try {
            data = new JSONObject(intent.getStringExtra("data"));
            ViewPager viewPager = findViewById(R.id.viewpager);
            DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new todayFragment(data.getJSONObject("currently")));
            adapter.addFragment(new weeklyFragment(data.getJSONObject("daily")));
            adapter.addFragment(new photosFragment(photos));
            viewPager.setAdapter(adapter);
            Toolbar toolbar = findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(location);

            TabLayout tabLayout = findViewById(R.id.tablist);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.today);
            tabLayout.getTabAt(1).setIcon(R.drawable.weekly);
            tabLayout.getTabAt(2).setIcon(R.drawable.photos);
        } catch(JSONException e) { e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.d_twitter: {
                try {
                    String query = "https://twitter.com/intent/tweet?text=Check+Out+";
                    query = query.concat(location.split(", ")[0].replaceAll(" ", "+"));
                    query = query.concat("'s+Weather!+It+is+");
                    query = query.concat(String.valueOf(data.getJSONObject("currently").getInt("temperature")));
                    query = query.concat("°F!&hashtags=CSCI571WeatherSearch");
                    Uri uri = Uri.parse(query);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch(JSONException e) { e.printStackTrace();}
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void getPhotos(String loc) {
        String url = "http://dzyhw8.us-east-2.elasticbeanstalk.com/api/customsearch/".concat(loc);
        JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for(int i=0; i<8; i++) {
                                photos.add(response.getJSONArray("items").
                                        getJSONObject(i).getString("link"));
                            }
                        } catch (JSONException e) { e.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DetailActivity", "getPhotos Error");
                    }
                }
        );
        myQueue.add(json_request);
    }
}