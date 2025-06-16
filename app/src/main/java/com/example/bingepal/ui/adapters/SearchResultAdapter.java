package com.example.bingepal.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bingepal.R;
import com.example.bingepal.data.SearchResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private final List<SearchResult> results;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SearchResult item);
    }

    public SearchResultAdapter(Context context, List<SearchResult> results, OnItemClickListener listener) {
        this.context = context;
        this.results = results;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView textTitle, textYear;

        public ViewHolder(View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textYear = itemView.findViewById(R.id.textYear);
        }

        public void bind(SearchResult result, Context context, OnItemClickListener listener) {
            textTitle.setText(result.title != null ? result.title : "Unknown");
            textYear.setText(result.year != null ? result.year.toString() : "—");

            // Load poster if available
            if (result.poster_url != null && !result.poster_url.isEmpty()) {
                String thumbnail = result.poster_url.contains("/w500")
                        ? result.poster_url.replace("/w500", "/w92")
                        : result.poster_url;

                Glide.with(context)
                        .load(thumbnail)
                        .placeholder(R.drawable.ic_bingepal_logo)
                        .override(92, 138) // fallback sizing
                        .centerCrop()
                        .transform(new RoundedCorners(16))
                        .into(imagePoster);}

            itemView.setOnClickListener(v -> listener.onItemClick(result));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(results.get(position), context, listener);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
