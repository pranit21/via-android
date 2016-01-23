package com.fierydevs.materialstartdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fierydevs.materialstartdemo.models.Photos;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class DisplayActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_IMG_URL = "img_url";
    public static final String EXTRA_PARAM_TITLE = "title";
    private String photo_url, photo_title;
    private ImageView imageView;
    private TextView editText, longText;
    private LinearLayout revealView;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_container;
    private boolean isEditTextVisible;
    private InputMethodManager mInputManager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        photo_url = intent.getStringExtra(EXTRA_PARAM_IMG_URL);
        photo_title = intent.getStringExtra(EXTRA_PARAM_TITLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing_container = (CollapsingToolbarLayout) findViewById(R.id.collapsing_container);

        setSupportActionBar(toolbar);
        collapsing_container.setTitle(photo_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.photoImage);
        revealView = (LinearLayout) findViewById(R.id.editTextHolder);
        editText = (TextView) findViewById(R.id.editText);
        longText = (TextView) findViewById(R.id.longText);

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditTextVisible) {
                    revealEditText(revealView);
                    editText.requestFocus();
                    mInputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    mInputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    hideEditText(revealView);
                }
            }
        });

        //editText.setText(photo_title);
        imageLoader.displayImage(photo_url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Generate background color for TextView using Palette API,
                // when Image loading is completed

                // Synchronous
                // Palette palette = Palette.from(bitmap).generate();

                // Asynchronous
                Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int color = palette.getLightVibrantColor(getResources().getColor(android.R.color.black));
                        revealView.setBackgroundColor(color);
                    }
                });
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        revealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            windowTransition();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTransitionEnd(Transition transition) {
                fab.animate().alpha(1.0f);
                getWindow().getEnterTransition().removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void hideEditText(final LinearLayout revealView) {
        int cx = revealView.getRight() - 30;
        int cy = revealView.getBottom() - 60;
        int initialRadius = revealView.getWidth();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    revealView.setVisibility(View.INVISIBLE);
                }
            });
            isEditTextVisible = false;
            anim.start();
        } else {
            revealView.setVisibility(View.INVISIBLE);
            isEditTextVisible = false;
        }
    }

    private void revealEditText(LinearLayout revealView) {
        int cx = revealView.getRight() - 30;
        int cy = revealView.getBottom() - 60;
        int finalRadius = Math.max(revealView.getWidth(), revealView.getHeight());
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, 0, finalRadius);
            revealView.setVisibility(View.VISIBLE);
            isEditTextVisible = true;
            anim.start();
        } else {
            revealView.setVisibility(View.VISIBLE);
            isEditTextVisible = true;
        }
    }
}
