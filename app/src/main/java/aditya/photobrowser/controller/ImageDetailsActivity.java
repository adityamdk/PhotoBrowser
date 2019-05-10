package aditya.photobrowser.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import aditya.photobrowser.R;

public class ImageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        // Get intent data
        Intent i = getIntent();
        // Get Selected Image Id
        String url = i.getExtras().getString("URL");
        String imageTitle = i.getExtras().getString("Imagetitle");

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
            }
        });

        TextView tv = (TextView) findViewById(R.id.titleOfPhoto);
        Picasso.with(getApplicationContext())
                .load(url)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(imageView);
        tv.setText(imageTitle);
    }
}
