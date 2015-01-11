package com.bigandroiddev.vibify.Lists;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bigandroiddev.vibify.R;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

/**
 * Created by spiros on 11/30/14.
 */
public class MyListItemAnimationListener implements Animation.AnimationListener {

    private static final String TAG = MyListItemAnimationListener.class.getSimpleName();
    private SwingBottomInAnimationAdapter mAdapter;
    private View mView;
    private Context context;

    public MyListItemAnimationListener(View view, SwingBottomInAnimationAdapter adapter, Context ctx) {
        mAdapter = adapter;
        mView = view;
        context = ctx;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "onAnimationEnd");
        mView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_to_normal));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

//		AppListItem item = (AppListItem) mAdapter.getItem(position);


    }
}
