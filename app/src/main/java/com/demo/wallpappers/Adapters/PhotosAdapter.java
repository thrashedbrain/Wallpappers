package com.demo.wallpappers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.demo.wallpappers.Models.PhotosModel;
import com.demo.wallpappers.PreviewActivity;
import com.demo.wallpappers.R;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    private List<PhotosModel> urls;
    private int width;
    private Context context;

    public PhotosAdapter(Context context, List<PhotosModel> urls, int width){
        this.urls = urls;
        this.width = width;
        this.context = context;
    }

    public void clearAll(){

    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_photo, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotosViewHolder holder, final int position) {
        holder.imageView.setMaxWidth(width);
        Picasso.get().load(urls.get(position).smallImg).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putExtra("url", urls.get(position).bigImg);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public void setUrls (List<PhotosModel> urls){
        this.urls = urls;
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public PhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPhoto);
        }
    }
}
