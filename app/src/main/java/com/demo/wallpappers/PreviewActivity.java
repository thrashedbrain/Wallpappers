package com.demo.wallpappers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class PreviewActivity extends AppCompatActivity {

    private ImageView previewImg;
    private FloatingActionButton fab;
    private Intent intent;
    private Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        previewImg = findViewById(R.id.previewImg);
        fab = findViewById(R.id.fab);
        intent = getIntent();

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bitmap1 = bitmap;
                previewImg.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if (intent != null){
            Picasso.get().load(intent.getStringExtra("url")).into(target);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(PreviewActivity.this);
                try {
                    if (bitmap1 != null){
                        myWallpaperManager.setBitmap(bitmap1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
