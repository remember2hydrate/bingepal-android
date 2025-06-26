package com.example.bingepal.ui;

import static com.example.bingepal.network.UnsafeOkHttpClient.getUnsafeClient;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bingepal.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final String HEALTH_URL = "https://bingepal.onrender.com/health";
    private static final int RETRY_DELAY_MS = 15000;

    private TextView statusText;
    private ImageView logoView;
    private Handler handler = new Handler();
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        statusText = findViewById(R.id.statusText);
        logoView = findViewById(R.id.logoView);

        // Spin the logo
        Glide.with(this)
                .load(R.drawable.ic_bingepal_logo)
                .circleCrop()
                .into(logoView);
        //Log.e("config-check", "Cleartext permitted: " + (Build.VERSION.SDK_INT >= 28));
        client = getUnsafeClient();
        checkHealth();
    }

    private void checkHealth() {
        Request request = new Request.Builder().url(HEALTH_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("api health splash", "FAILED: " + e.getMessage());
                retryLater();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("api health splash", "response incoming....." + response.message());
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    retryLater();
                }
            }
        });
        /* 301 permanent redirect to https
        new Thread(() -> {
            try {
                URL url = new URL("http://bingepal.onrender.com/health");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();

                int code = connection.getResponseCode();
                Log.e("manual-test", "Code: " + code);
            } catch (Exception e) {
                Log.e("manual-test", "Error: " + e.getMessage());
            }
        }).start();*/

    }

    private void retryLater() {
        runOnUiThread(() -> statusText.setText("Connecting to API..."));
        handler.postDelayed(this::checkHealth, RETRY_DELAY_MS);
    }
}
