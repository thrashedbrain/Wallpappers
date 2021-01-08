package com.demo.wallpappers.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.demo.wallpappers.Adapters.PhotosAdapter;
import com.demo.wallpappers.Models.PhotosModel;
import com.demo.wallpappers.R;
import com.demo.wallpappers.ViewModel.PhotosViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private LinearLayout queryHolder;
    private FloatingSearchView searchView;
    private List<PhotosModel> models;
    private PhotosAdapter adapter;
    private PhotosViewModel viewModel;
    private int page;
    private String query;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);
        viewModel = new ViewModelProvider(getViewModelStore(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PhotosViewModel.class);
        recyclerView = view.findViewById(R.id.search_rv);
        queryHolder = view.findViewById(R.id.queryLay);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        models = new ArrayList<>();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        adapter = new PhotosAdapter(getContext(), models, width/2);

        recyclerView.setAdapter(adapter);

        viewModel.init();
        viewModel.getPhotos().observe(this, new Observer<PhotosModel>() {
            @Override
            public void onChanged(PhotosModel photosModel) {
                if (photosModel == null){
                    queryHolder.setVisibility(View.VISIBLE);
                }
                else {
                    models.add(photosModel);

                    //adapter.setUrls(models);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == models.size() - 1) {
                        page = page + 1;
                        viewModel.getSearchPhotos(query, page);
                    }
                }
            });
        }

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                query  = currentQuery;
                page = 0;
                queryHolder.setVisibility(View.GONE);
                viewModel.getSearchPhotos(currentQuery, 0);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
