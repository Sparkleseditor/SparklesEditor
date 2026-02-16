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
import com.sparkleseditor.components.ExpandableLayout;
import com.sparkleseditor.databinding.FragmentMainBinding;
import com.sparkleseditor.navigation.Navigator;

public class MainFragment extends BaseFragment {

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
        setupToolbox();

    }

    private void setupToolbox() {
        binding.options.setExpansion(false);
        binding.options.setDuration(200);
        binding.options.setOrientatin(ExpandableLayout.VERTICAL);
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
                    if (id == R.id.action_settings){
                        Fragment frgSettings = new SettingsFragment();
                        Navigator.pushTo(
                                getParentFragmentManager(),
                                R.id.nav_host,
                                frgSettings
                        );
                    }
                    if (id == R.id.toolbar){
                        if (!binding.options.isExpanded()) {
                            binding.options.expand();
                        } else {
                            binding.options.collapse();
                        }
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