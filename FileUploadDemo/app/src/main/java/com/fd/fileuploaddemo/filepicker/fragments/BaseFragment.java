package com.fd.fileuploaddemo.filepicker.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Pranit on 12-09-2016.
 */

public abstract class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }

    protected void fadeIn(View view)
    {
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in);

        view.startAnimation(bottomUp);
        view.setVisibility(View.VISIBLE);
    }

    protected void fadeOut(View view)
    {
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_out);

        view.startAnimation(bottomUp);
        view.setVisibility(View.GONE);
    }
}