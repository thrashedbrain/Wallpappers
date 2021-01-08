package com.demo.wallpappers.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.wallpappers.Models.PhotosModel;
import com.demo.wallpappers.Repository.PhotoRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotosViewModel extends ViewModel {

    private MutableLiveData<PhotosModel> photoLiveData;
    private int pages;

    public void init(){
        photoLiveData = new MutableLiveData<>();
    }

    public void getSearchPhotos(String q, int page){
        if (pages == 0 || page < pages){
            new PhotoRepo.getData(new PhotoRepo.onDataListener() {
                @Override
                public void returnData(String data) {
                    try {
                        if (!data.isEmpty()){
                            JSONObject array = new JSONObject(data);
                            pages = array.getInt("total_pages");
                            if (array.getInt("total") == 0) {
                                photoLiveData.setValue(null);
                            }
                            else {
                                JSONArray array1 = array.getJSONArray("results");
                                for (int i = 0; i < array1.length(); i++){
                                    JSONObject jsonArray = array1.getJSONObject(i).getJSONObject("urls");
                                    photoLiveData.setValue(new PhotosModel(jsonArray.getString("small"), jsonArray.getString("regular")));
                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, q, page).execute("https://api.unsplash.com/search/photos");
        }

    }

    public void getRandomPhotos(){
        new PhotoRepo.getData(new PhotoRepo.onDataListener() {
            @Override
            public void returnData(String data) {
                try {
                    if (!data.isEmpty()){
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++){
                            JSONObject jsonArray = array.getJSONObject(i).getJSONObject("urls");
                            photoLiveData.setValue(new PhotosModel(jsonArray.getString("small"), jsonArray.getString("regular")));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://api.unsplash.com/photos/random");
    }

    public LiveData<PhotosModel> getPhotos(){
        return photoLiveData;
    }
}
