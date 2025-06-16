package com.example.bingepal.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bingepal.R;
import com.example.bingepal.ui.fragments.CategoryFragment;
import com.example.bingepal.ui.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load CategoryFragment as the entry point
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CategoryFragment())
                    .commit();
        }
    }

    // Called from CategoryFragment
    public void openSearchFragment(String type) {
        SearchFragment fragment = SearchFragment.newInstance(type);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
