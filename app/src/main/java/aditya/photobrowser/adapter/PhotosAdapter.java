package aditya.photobrowser.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aditya.photobrowser.R;
import aditya.photobrowser.controller.PhotosActivity;
import aditya.photobrowser.model.PhotosData;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosHolder> {


    private List<PhotosData> photos;

    private static Context mContext;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    private PhotosActivity.PhotosRecyclerViewClickListener mListener;

    @NonNull
    @Override
    public PhotosAdapter.PhotosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_photos_item, viewGroup, false);
        return new PhotosHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.PhotosHolder photosHolder, int i) {

        String thumbnailUrl = photos.get(i).getThumbnailUrl();
        Picasso.with(mContext)
                .load(thumbnailUrl)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .resize(150, 150)
                .into(photosHolder.image);
        setAnimation(photosHolder.itemView, i);


        photosHolder.title.setText(photos.get(i).getTitle());
        photosHolder.title.setTextColor(Color.BLACK);
        return;
    }


    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position != lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PhotosActivity.PhotosRecyclerViewClickListener mListener;
        LinearLayout photosLayout;
        TextView title;
        ImageView image;

        public PhotosHolder(@NonNull View itemView, PhotosActivity.PhotosRecyclerViewClickListener listener) {
            super(itemView);
            photosLayout = itemView.findViewById(R.id.photos_layout);
            title = itemView.findViewById(R.id.phototitle);
            image = itemView.findViewById(R.id.Albumimage);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    public PhotosAdapter(List<PhotosData> photos, Context context, PhotosActivity.PhotosRecyclerViewClickListener listener) {
        this.photos = photos;
        mContext = context;
        mListener = listener;
    }

}
