package aditya.photobrowser.controller;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.List;

import aditya.photobrowser.R;
import aditya.photobrowser.adapter.AlbumAdapter;
import aditya.photobrowser.model.AlbumData;
import aditya.photobrowser.rest.AlbumApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static aditya.photobrowser.util.util.ALBUM_BASE_URL;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {


    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }

    public class VerticalSpacingDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        public VerticalSpacingDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = spacing;
        }
    }

    private static Retrofit retrofit = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.album_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpacingDecoration(64));
        connectAndGetApiData();
    }

    private void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ALBUM_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        AlbumApiInterface videoApiInterface = retrofit.create(AlbumApiInterface.class);

        Call<List<AlbumData>> call = videoApiInterface.getAlbumData();
        call.enqueue(new Callback<List<AlbumData>>() {
            @Override
            public void onResponse(Call<List<AlbumData>> call, Response<List<AlbumData>> response) {
                final List<AlbumData> albumData = response.body();
                RecyclerViewClickListener listener = (view, position) -> {
                    Intent myIntent = new Intent(getApplicationContext(), PhotosActivity.class);
                    Bundle bundle = new Bundle();
                    String id = albumData.get(position).getId();
                    bundle.putString("AlbumId", id);
                    myIntent.putExtras(bundle);
                    view.getContext().startActivity(myIntent);
                };
                AlbumAdapter myAdapter = new AlbumAdapter(albumData, getApplicationContext(), listener);

                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<List<AlbumData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Call failed", Toast.LENGTH_LONG).show();
            }


        });
    }

}