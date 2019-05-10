package aditya.photobrowser.controller;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aditya.photobrowser.R;
import aditya.photobrowser.adapter.PhotosAdapter;
import aditya.photobrowser.model.PhotosData;
import aditya.photobrowser.rest.PhotosApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static aditya.photobrowser.util.util.ALBUM_BASE_URL;

public class PhotosActivity extends AppCompatActivity {

    private int idOfAlbum = 0;

    public interface PhotosRecyclerViewClickListener {

        void onClick(View view, int position);
    }

    public class PhotosVerticalSpacingDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        public PhotosVerticalSpacingDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = spacing;
        }
    }


    TextView tv;
    private static Retrofit retrofit = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //getting data from intent :
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("AlbumId");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("AlbumId");
        }
        if (newString != null) {
            idOfAlbum = Integer.valueOf(newString);
        }

        //setting recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new PhotosVerticalSpacingDecoration(64));
        connectAndGetApiData();
    }


    private void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ALBUM_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        PhotosApiInterface videoApiInterface = retrofit.create(PhotosApiInterface.class);

        Call<List<PhotosData>> call = videoApiInterface.getPhotosData();
        call.enqueue(new Callback<List<PhotosData>>() {
            @Override
            public void onResponse(Call<List<PhotosData>> call, Response<List<PhotosData>> response) {
                final List<PhotosData> photosData = response.body();
                List<PhotosData> modifiedData = new ArrayList<>();
                for (PhotosData photos : photosData) {
                    String tempId = photos.getAlbumId();
                    if (Integer.valueOf(tempId) == idOfAlbum) {
                        modifiedData.add(photos);
                    }
                }
                PhotosRecyclerViewClickListener listener = (view, position) -> {
                    Intent myIntent = new Intent(getApplicationContext(), ImageDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    String url = modifiedData.get(position).getUrl();
                    String imageTitle = modifiedData.get(position).getTitle();
                    bundle.putString("URL", url);
                    bundle.putString("Imagetitle", imageTitle);
                    myIntent.putExtras(bundle);
                    view.getContext().startActivity(myIntent);
                };
                //Refining the photosData here based on the input received by bundle.
                PhotosAdapter myAdapter = new PhotosAdapter(modifiedData, getApplicationContext(), listener);
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<List<PhotosData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Call failed", Toast.LENGTH_LONG).show();
            }

        });
    }


}
