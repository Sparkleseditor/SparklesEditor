package com.sparkleseditor.navigation;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.transition.platform.MaterialSharedAxis;

public class Navigator {
    public static void pushTo(FragmentManager fragmentManager, int container, Fragment currentFragment, Fragment pushFragment){
        MaterialSharedAxis exitTransition = new MaterialSharedAxis(MaterialSharedAxis.X, true);
        MaterialSharedAxis enterTransition = new MaterialSharedAxis(MaterialSharedAxis.X, true);
        MaterialSharedAxis returnTransition = new MaterialSharedAxis(MaterialSharedAxis.X, false);
        MaterialSharedAxis reenterTransition = new MaterialSharedAxis(MaterialSharedAxis.X, false);

        currentFragment.setExitTransition(exitTransition);
        currentFragment.setReenterTransition(reenterTransition);
        pushFragment.setEnterTransition(enterTransition);
        pushFragment.setReturnTransition(returnTransition);

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(container, pushFragment)
                .addToBackStack(pushFragment.getClass().getSimpleName())
                .commit();
    }
}
