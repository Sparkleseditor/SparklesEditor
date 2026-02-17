package com.sparkleseditor.fragments;

import static android.R.color.transparent;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        binding.fab.setTranslationY(-12);


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