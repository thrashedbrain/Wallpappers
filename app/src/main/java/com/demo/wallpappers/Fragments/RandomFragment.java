package com.demo.wallpappers.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.demo.wallpappers.Adapters.PhotosAdapter;
import com.demo.wallpappers.Models.PhotosModel;
import com.demo.wallpappers.R;
import com.demo.wallpappers.ViewModel.PhotosViewModel;

import java.util.ArrayList;
import java.util.List;


public class RandomFragment extends Fragment {

    public RandomFragment() {
        // Required empty public constructor
    }

    private View view;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<PhotosModel> models;
    private PhotosAdapter adapter;
    private PhotosViewModel viewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_random, container, false);
        viewModel = new ViewModelProvider(getViewModelStore(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PhotosViewModel.class);
        recyclerView = view.findViewById(R.id.random_rv);
        progressBar = view.findViewById(R.id.progress);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        models = new ArrayList<>();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        adapter = new PhotosAdapter(getContext(), models, width/2);

        recyclerView.setAdapter(adapter);

        viewModel.init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == models.size() - 1) {
                        viewModel.getRandomPhotos();
                    }
                }
            });
        }

        viewModel.getPhotos().observe(this, new Observer<PhotosModel>() {
            @Override
            public void onChanged(PhotosModel photosModel) {
                models.add(photosModel);
                progressBar.setVisibility(View.GONE);
                //adapter.setUrls(models);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getRandomPhotos();
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
