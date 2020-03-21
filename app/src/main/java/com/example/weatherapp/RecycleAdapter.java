package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.VH> {
    private List<String> photos;
    Context context;
    public RecycleAdapter(List<String> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView photo;
        public VH(View item) {
            super(item);
            photo = item.findViewById(R.id.p_photo);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Picasso.with(context).load(photos.get(position)).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
