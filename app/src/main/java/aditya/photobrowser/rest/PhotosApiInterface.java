package aditya.photobrowser.rest;


import java.util.List;

import aditya.photobrowser.model.PhotosData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotosApiInterface {
    @GET("photos/")
    Call<List<PhotosData>> getPhotosData();
}
