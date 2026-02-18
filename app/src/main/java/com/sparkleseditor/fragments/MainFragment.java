package com.sparkleseditor.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.sparkleseditor.R;
import com.sparkleseditor.components.ExpandableLayout;
import com.sparkleseditor.databinding.FragmentMainBinding;
import com.sparkleseditor.navigation.Navigator;

import io.github.rosemoe.sora.widget.SymbolInputView;

public class MainFragment extends BaseFragment {

    private FragmentMainBinding binding;

    public static final String[] SYMBOLS = {
            "TAB","↵", "{", "}", "(", ")",
            ",", ".", ";", "\"", "?",
            "+", "-", "*", "/", "<",
            ">", "[", "]", ":"
    };

    public static final String[] SYMBOL_INSERT_TEXT = {
            "\t","\n", "{}", "}", "(", ")",
            ",", ".", ";", "\"", "?",
            "+", "-", "*", "/", "<",
            ">", "[", "]", ":"
    };

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
        setupInputView();
        setupTabLayoutTemp();
        slideXDrawer();
        binding.fab.setTranslationY(-12);


    }

    private void slideXDrawer() {
        /*int statusBarHeight =
                getResources()
                        .getDimensionPixelSize(
                                getResources().getIdentifier("status_bar_height", "dimen", "android"));*/
        int navigationBarHeight =
                getResources()
                        .getDimensionPixelSize(
                                getResources().getIdentifier("navigation_bar_height", "dimen", "android"));
        ViewGroup.LayoutParams layoutParams = binding.leftDrawerMenu.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            binding.leftDrawerMenu.setLayoutParams(marginLayoutParams);
        }

        binding.drawer.setScrimColor(Color.TRANSPARENT);
        binding.drawer.setDrawerElevation(0f);
        binding.drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            float slideX;
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                int gravity = ((DrawerLayout.LayoutParams) drawerView.getLayoutParams()).gravity;
                if (gravity == GravityCompat.START) {
                    binding.coordinator.setTranslationX(slideOffset * drawerView.getWidth());
                    binding.drawer.bringChildToFront(drawerView);
                    binding.drawer.requestLayout();
                } else if (gravity == GravityCompat.END) {
                    binding.coordinator.setTranslationX(-slideOffset * drawerView.getWidth());
                    binding.drawer.bringChildToFront(drawerView);
                    binding.drawer.requestLayout();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                binding.coordinator.setTranslationX(0f);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (!binding.drawer.isDrawerOpen(GravityCompat.START) &&
                            !binding.drawer.isDrawerOpen(GravityCompat.END)) {
                        binding.coordinator.setTranslationX(0f);
                    }
                }

            }


        });

        binding.toolbar.setNavigationOnClickListener(v ->{
            binding.drawer.openDrawer(GravityCompat.START);
        });
    }

    private void setupTabLayoutTemp() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("My Tab Title"));

    }


    private void setupInputView() {

        SymbolInputView inputView = binding.inputer;
        inputView.bindEditor(binding.editor);
        inputView.addSymbols(SYMBOLS, SYMBOL_INSERT_TEXT);
        inputView.setBackgroundColor(Color.TRANSPARENT);

    }

    private void setupToolbox() {
        binding.options.setExpansion(false);
        binding.options.setDuration(200);
        binding.options.setOrientatin(ExpandableLayout.VERTICAL);
        binding.settings.setOnClickListener(v->{
            Fragment frgSettings = new SettingsFragment();
            Navigator.pushTo(
                    getParentFragmentManager(),
                    R.id.nav_host,
                    frgSettings
            );
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener(
                item->{
                    int id = item.getItemId();
                    if (id == R.id.undo){
                        if (binding.editor.canUndo()){
                            binding.editor.undo();
                        }
                        return true ;
                    }
                    if (id == R.id.redo) {
                        if (binding.editor.canRedo()) {
                            binding.editor.redo();
                        }
                    }
                    if (id == R.id.right_drawer) {
                        binding.drawer.openDrawer(GravityCompat.END);
                        return true;
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