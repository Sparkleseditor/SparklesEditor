package com.sparkleseditor.navigation;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.transition.platform.MaterialSharedAxis;

public class Navigator {
    public static void pushTo(FragmentManager fragmentManager, int container, Fragment pushFragment){

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(container, pushFragment)
                .addToBackStack(null)
                .commit();
    }
}
