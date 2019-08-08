package com.mustafa.foodapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mustafa.foodapp.R;
//import com.mustafa.foodapp.databinding.ActivityBaseBinding;

public abstract class BaseActivity extends AppCompatActivity {
    public ProgressBar mProgressBar;
    public FrameLayout mFrameLayout;

    @Override
    public void setContentView(int layoutResID) {

        RelativeLayout mRelativeLayout  = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = mRelativeLayout.findViewById(R.id.activity_content);
        mProgressBar = mRelativeLayout.findViewById(R.id.progress_bar);

        /**
         * True means layoutResID should be inflated and made a part of the parent frameLayout
         */
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        mRelativeLayout.requestLayout();
        super.setContentView(mRelativeLayout);
    }

    @Override
    public void setContentView(View view) {

        RelativeLayout mRelativeLayout  = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        mFrameLayout = mRelativeLayout.findViewById(R.id.activity_content);
        mProgressBar = mRelativeLayout.findViewById(R.id.progress_bar);

        mFrameLayout.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));


        super.setContentView(mRelativeLayout);
    }

    public void showProgressBar(boolean visibility){
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        mFrameLayout.setVisibility(visibility ? View.INVISIBLE : View.VISIBLE);
    }
}
