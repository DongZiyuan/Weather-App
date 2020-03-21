package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import com.google.android.material.tabs.TabLayout;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {
    private String curloc;
    public SummaryPagerAdapter adapter;
    private SharedPreferences fsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1000);
        } catch(Exception e) { e.printStackTrace();}
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_layout);

        curloc = getResources().getString(R.string.curloc);
        adapter = new SummaryPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(curloc, getResources().getString(R.string.degree));

        fsp = getSharedPreferences("fav", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fsp.edit();
        editor.clear();
        editor.commit();

        Toolbar toolbar = findViewById(R.id.summary_toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.s_viewpager);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.citylist);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_menu, menu);
        androidx.appcompat.widget.SearchView sv;
        sv = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.s_input).getActionView();
        sv.setBackgroundColor(getResources().getColor(R.color.colorCardBackground));
        androidx.appcompat.widget.SearchView.SearchAutoComplete sac = sv.findViewById(R.id.search_src_text);
        sac.setTextColor(Color.WHITE);
        sac.setHintTextColor(Color.WHITE);
        sac.setThreshold(3);
        //sac.setDropDownBackgroundResource(Color.WHITE);

        sv.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SummaryActivity.this, ResultActivity.class);
                intent.putExtra("location", query);
                startActivityForResult(intent, 1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            switch(data.getStringExtra("type")) {
                case "add":
                    adapter.addFragment(data.getStringExtra("location"), data.getStringExtra("deg"));
                    return;
                case "remove":
                    adapter.removeFragment(data.getStringExtra("location"));
                    return;
            }
        }
    }
}