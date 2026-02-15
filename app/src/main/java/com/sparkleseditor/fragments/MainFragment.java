package com.sparkleseditor.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.sparkleseditor.R;
import com.sparkleseditor.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();

    }

    @SuppressLint("NonConstantResourceId")
    private void setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener(
                item->{
                    int id = item.getItemId();
                    if (id == R.id.undo) return true;
                    if (id == R.id.redo) return true;
                    if (id == R.id.right_drawer) {
                        binding.drawer.openDrawer(GravityCompat.END);
                        return true;
                    }
                    return false;
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}