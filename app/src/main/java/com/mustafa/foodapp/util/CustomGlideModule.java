package com.mustafa.foodapp.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.mustafa.foodapp.R;

import androidx.databinding.BindingAdapter;

@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
//        url = url.substring(0, 4) + "s" + url.substring(4);
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(imageView);
    }
}

