package com.bigandroiddev.vibify.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bigandroiddev.vibify.R;

/**
 * Created by spiros on 11/2/14.
 */
public class CustomProgressBar extends ImageView {

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.setBackgroundResource(R.drawable.custom_progress_bar);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) this.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();
    }


}
