package com.example.bingepal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bingepal.R;
import com.example.bingepal.ui.MainActivity;

public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCategoryClick(view, R.id.catMovies, "movie");
        setupCategoryClick(view, R.id.catSeries, "series");
        setupCategoryClick(view, R.id.catAnime, "anime");
        setupCategoryClick(view, R.id.catGames, "game");
        setupCategoryClick(view, R.id.catManga, "manga");
        setupCategoryClick(view, R.id.catBooks, "book");
    }

    private void setupCategoryClick(View root, int viewId, String type) {
        View btn = root.findViewById(viewId);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openSearchFragment(type);
                }
            });
        }
    }

}
