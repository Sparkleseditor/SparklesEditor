package com.sparkleseditor.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zyron.filetree.events.FileTreeEventListener;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.sparkleseditor.R;
import com.sparkleseditor.components.ExpandableLayout;
import com.sparkleseditor.databinding.FragmentMainBinding;
import com.sparkleseditor.navigation.Navigator;
import com.zyron.filetree.events.FileTreeEventListener;
import com.zyron.filetree.provider.FileTreeIconProvider;

import java.io.File;

import io.github.rosemoe.sora.widget.SymbolInputView;

public class MainFragment extends BaseFragment implements FileTreeEventListener {


    private final ActivityResultLauncher<Uri> folderPickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocumentTree(),
                    treeUri -> {
                        if (treeUri == null) return;

                        int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

                        requireContext().getContentResolver()
                                .takePersistableUriPermission(treeUri, flags);

                        path = getRealPathFromUri(treeUri);
                        setupFileTree();
                        Toast.makeText(requireContext(), "Folder selected!", Toast.LENGTH_LONG).show();

                    }
            );
    private String getRealPathFromUri(Uri treeUri) {
        String docId = DocumentsContract.getTreeDocumentId(treeUri);
        String[] split = docId.split(":");
        String type = split[0];
        String relativePath = split.length > 1 ? split[1] : "";
        if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/" + relativePath;
        } else {
            String externalStorage = System.getenv("SECONDARY_STORAGE");
            if (externalStorage == null) {
                externalStorage = System.getenv("EXTERNAL_STORAGE");
            }
            return externalStorage + "/" + relativePath;
        }
    }

    private FragmentMainBinding binding;
    private FileTreeIconProvider fileIconProvider;
    private String path ="";

    private static final int REQUEST_CODE_OPEN_DIRECTORY = 1001;

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

    public interface FileTreeEventListener {
        void onFileClick(File file);
        void onFolderClick(File folder);
    }

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

        //AppStruct
        setupToolbar();
        setupToolbox();
        setupInputView();
        setupTabLayoutTemp();
        slideXDrawer();
        drawerLeftContent();
        binding.fab.setTranslationY(-12);
    }

    private void drawerLeftContent() {

        binding.fileTree.setVisibility(View.VISIBLE);
        binding.contentGit.setVisibility(View.GONE);
        binding.btmOptions.setOnNavigationItemSelectedListener(
                item -> {
                    var sharedAxis = new MaterialSharedAxis(MaterialSharedAxis.X, true);
                    TransitionManager.beginDelayedTransition(binding.container, sharedAxis);
                    if (item.getItemId() == R.id.option_file_tree) {
                        binding.contentGit.setVisibility(View.GONE);
                        binding.fileTree.setVisibility(View.VISIBLE);
                    } else if (item.getItemId() == R.id.option_git) {
                        binding.contentGit.setVisibility(View.VISIBLE);
                        binding.fileTree.setVisibility(View.GONE);
                    }
                    return true;
                }
        );
        setupFileTree();
    }

    private void setupFileTree() {
        if (path != null && !path.isEmpty()) {
            binding.contentFileTree.setVisibility(View.VISIBLE);
            binding.requireFolder.setVisibility(View.GONE);
            binding.fileTreeView.initializeFileTree(path, this , fileIconProvider);
        }else{
            binding.contentFileTree.setVisibility(View.GONE);
            binding.requireFolder.setVisibility(View.VISIBLE);
            binding.openFolder.setOnClickListener(v -> {
                folderPickerLauncher.launch(null);
            });
        }
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

    public void onFileClick(File file) {
    }


    public void onFolderClick(File folder) {}


    public boolean onFileLongClick(File file) {
        return true;
    }


    public boolean onFolderLongClick(File folder) {

        return true;
    }


    public void onFileTreeViewUpdated(int startPosition, int itemCount) {}





}