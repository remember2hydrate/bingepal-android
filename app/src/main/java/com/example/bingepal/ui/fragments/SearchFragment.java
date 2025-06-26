package com.example.bingepal.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bingepal.R;
import com.example.bingepal.data.SearchResult;
import com.example.bingepal.network.ApiClient;
import com.example.bingepal.network.SearchService;
import com.example.bingepal.ui.DetailActivity;
import com.example.bingepal.ui.adapters.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private static final String ARG_TYPE = "media_type";
    private String selectedType;

    private EditText searchInput;
    private LinearLayout searchButton;
    private RecyclerView searchResults;
    private SearchResultAdapter adapter;
    private List<SearchResult> resultList = new ArrayList<>();
    private SearchService api;

    public static SearchFragment newInstance(String type) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedType = getArguments() != null ? getArguments().getString(ARG_TYPE, "movie") : "movie";

        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        searchResults = view.findViewById(R.id.searchResults);

        adapter = new SearchResultAdapter(requireContext(), resultList, result -> {
            //Toast.makeText(getContext(), "Tapped: " + result.title, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("id", result.id);
            intent.putExtra("type", result.type);
            startActivity(intent);
        });

        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults.setAdapter(adapter);

        api = ApiClient.getClient().create(SearchService.class);

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(getContext(), "Enter a search term", Toast.LENGTH_SHORT).show();
                return;
            }
            performSearch(query);
        });
    }

    private void performSearch(String query) {
        searchResults.setVisibility(View.GONE);
        resultList.clear();
        adapter.notifyDataSetChanged();

        Call<List<SearchResult>> call = api.searchContent(query, selectedType);
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    searchResults.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "No results found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                Toast.makeText(getContext(), "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

