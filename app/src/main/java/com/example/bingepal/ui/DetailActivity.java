package com.example.bingepal.ui;

import static com.example.bingepal.network.UnsafeOkHttpClient.getUnsafeClient;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bingepal.R;
import com.example.bingepal.data.LogEntry;
import com.example.bingepal.data.SearchResult;
import com.example.bingepal.network.ApiClient;
import com.example.bingepal.network.SearchService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView titleText, metaText, descText;
    private ImageView posterImage;
    private SearchService api;
    //private OkHttpClient client = new OkHttpClient();
    //public static final String LOG_URL = "https://bingepal.onrender.com/api/log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleText = findViewById(R.id.detailTitle);
        metaText = findViewById(R.id.detailMeta);
        descText = findViewById(R.id.detailDescription);
        posterImage = findViewById(R.id.detailPoster);

        String id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");

        if (id == null || type == null) {
            Toast.makeText(this, "Missing detail info", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        api = ApiClient.getClient().create(SearchService.class);
        fetchDetails(id, type);
    }

    private void fetchDetails(String id, String type) {
        api.getDetail(type, id).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateUI(response.body());
                } else {
                    Toast.makeText(DetailActivity.this, "No details found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(SearchResult item) {
        logUserInteraction(item);
        titleText.setText(item.title);
        metaText.setText((item.year != null ? item.year + " • " : "") + item.type + " • " + item.source);
        descText.setText(item.description != null ? item.description : "No description available.");

        if (item.poster_url != null) {
            Glide.with(this)
                    .load(item.poster_url)
                    .placeholder(R.drawable.ic_bingepal_logo) // Optional default image
                    .centerCrop()
                    .transform(new RoundedCorners(24)) // needs import!
                    .into(posterImage);
        }

    }

    private void logUserInteraction(SearchResult item){
        LogEntry log = new LogEntry(
                item.source,
                item.id,
                item.type,
                item.title
        );

        SearchService api = ApiClient.getClient().create(SearchService.class);
        api.logSearch(log).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("LogTrack", "Detail view logged : "+ response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("LogTrack", "Log failed: " + t.getMessage());
                Toast.makeText(DetailActivity.this, "Log failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
