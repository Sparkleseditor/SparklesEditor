
package com.sparkleseditor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.fragment.NavHostFragment;

import com.sparkleseditor.databinding.ActivityMainBinding;
import com.sparkleseditor.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.nav_host, MainFragment.class, null)
                    .commit();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        return navHostFragment.getNavController().navigateUp() || super.onSupportNavigateUp();
    }
}