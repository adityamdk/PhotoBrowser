package aditya.photobrowser.rest;

import java.util.List;

import aditya.photobrowser.model.AlbumData;
import aditya.photobrowser.model.PhotosData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface AlbumApiInterface {
    @GET("albums/")
    Call<List<AlbumData>> getAlbumData();
}
