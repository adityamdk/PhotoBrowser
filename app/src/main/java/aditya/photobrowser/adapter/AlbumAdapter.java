package aditya.photobrowser.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import aditya.photobrowser.R;
import aditya.photobrowser.controller.MainActivity;
import aditya.photobrowser.model.AlbumData;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {


    private List<AlbumData> albums;

    private static Context mContext;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private MainActivity.RecyclerViewClickListener mListener;

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_album_item, viewGroup, false);
        return new AlbumHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder albumHolder, int i) {
        albumHolder.title.setText(albums.get(i).getTitle());
        albumHolder.title.setTextColor(Color.BLACK);
        // Here you apply the animation when the view is bound
        setAnimation(albumHolder.itemView, i);
        return;

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position != lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class AlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MainActivity.RecyclerViewClickListener mListener;
        LinearLayout albumLayout;
        TextView title;

        public AlbumHolder(@NonNull View itemView, MainActivity.RecyclerViewClickListener listener) {
            super(itemView);
            albumLayout = itemView.findViewById(R.id.album_layout);
            title = itemView.findViewById(R.id.title);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    public AlbumAdapter(List<AlbumData> albums, Context context, MainActivity.RecyclerViewClickListener listener) {
        this.albums = albums;
        mContext = context;
        mListener = listener;
    }


}