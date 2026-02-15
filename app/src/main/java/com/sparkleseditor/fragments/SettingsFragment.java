package com.sparkleseditor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sparkleseditor.R;
import com.sparkleseditor.databinding.FragmentSecondBinding;
import com.sparkleseditor.databinding.FragmentSettingsBinding;
import com.sparkleseditor.navigation.Navigator;

public class SettingsFragment extends Fragment {

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
        binding.buttonSecond.setOnClickListener(
                v-> {
                    Fragment frghome = new MainFragment();
                    Navigator.pushTo(
                            getParentFragmentManager(),
                            R.id.nav_host,
                            this,
                            frghome
                    );
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}