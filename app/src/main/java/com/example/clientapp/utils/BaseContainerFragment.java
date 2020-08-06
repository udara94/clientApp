package com.example.clientapp.utils;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.clientapp.R;

public class BaseContainerFragment extends Fragment {

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commitAllowingStateLoss();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceChildFragment(Fragment fragment, boolean addToBackStack, String TAG) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left );
        transaction.add(R.id.container_framelayout, fragment,TAG);
        transaction.commitAllowingStateLoss();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    public boolean getBackStackEntryCount() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void cleanBackStack() {
        if (!isAdded()) return;
        for(int i = 0; i < getChildFragmentManager().getBackStackEntryCount(); ++i) {
            getChildFragmentManager().popBackStack();
        }
    }
}
