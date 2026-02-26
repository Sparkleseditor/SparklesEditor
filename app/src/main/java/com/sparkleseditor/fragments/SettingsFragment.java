package com.sparkleseditor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sparkleseditor.R;
import com.sparkleseditor.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends BaseFragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(
                v -> {
                    getParentFragmentManager().popBackStack();
                }
        );
        setupUi();


    }

    private void setupUi() {
        binding.i1.updateAppearance(0,5);
        binding.i2.updateAppearance(1,5);
        binding.i3.updateAppearance(2,5);
        binding.i4.updateAppearance(3,5);
        binding.i5.updateAppearance(4,5);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}