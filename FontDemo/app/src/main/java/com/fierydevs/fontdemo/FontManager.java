package com.fierydevs.fontdemo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Pranit on 03-10-2015.
 */
public class FontManager {
    public static final String ROOT = "fonts/",
    FONT_AWESOME = ROOT + "fontawesome-webfont.ttf",
    ADMIRATION_PAINS = ROOT + "admiration-pains.ttf",
    FRIDAY_NIGHT = ROOT + "friday-night.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static void markAsIconContainer(View view, Typeface typeface) {
        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                markAsIconContainer(child, typeface);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }
}
