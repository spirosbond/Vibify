package com.bigandroiddev.vibify.Utils;

import android.view.View;

import com.bigandroiddev.vibify.MainActivity;
import com.bigandroiddev.vibify.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by spiros on 11/30/14.
 */
public class Tutorial {
    private static int svCounter;
    private ShowcaseView sv;

    public Tutorial(final MainActivity activity) {
//        svCounter = 0;
//        sv = new ShowcaseView.Builder(activity).build();
//
//        sv.setContentTitle(activity.getResources().getString(R.string.tutorial1_title));
//        sv.setContentText(activity.getResources().getString(R.string.showcase_message));
//        sv.setStyle(R.style.CustomShowcaseTheme);
//        sv.setShowcase(ViewTarget.NONE, true);
//
//        //sv.setStyle(R.style.CustomShowcaseTheme);
//        sv.setButtonText(activity.getResources().getString(R.string.lets_begin));
//        activity.toggleDrawer(false);
//        //ViewTarget init = new ViewTarget(R.id.app_list, activity);
//        //ShowcaseView.insertShowcaseView(sv, activity);
//       /* ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
//        activity.toggleDrawer(false);
//
//        co.hideOnClickOutside = false;
//        co.block = true;
//        co.shotType = ShowcaseView.TYPE_ONE_SHOT;
//        //		sv = ShowcaseView.insertShowcaseView(new ViewTarget(findViewById(R.id.serviceState)), this);
//        ViewTarget init = new ViewTarget(R.id.app_list, activity);
//        sv = ShowcaseView.insertShowcaseView(init, activity, R.string.showcase_title,
//                R.string.showcase_message, co);
//        sv.setShowcase(ShowcaseView.NONE);
//        sv.setScaleMultiplier(0.7f);
//        sv.setButtonText(activity.getResources().getString(R.string.lets_begin));*/
//        /*RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        sv.setButtonPosition(lps);*/
//        sv.overrideButtonClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (svCounter) {
//                    case 0:
//                        sv.setContentTitle(activity.getResources().getString(R.string.tutorial2_title));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_2));
//                        sv.setButtonText(activity.getResources().getString(R.string.next));
//                        sv.setShowcase(ViewTarget.NONE, true);
//                        break;
//
//                    case 1:
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_3));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_3));
//                        sv.setShowcase(new ViewTarget(R.id.app_list, activity), true);
//                        //sv.setText(R.string.showcase_title_3, R.string.showcase_message_3);
//                        break;
//
//                    case 2:
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_enable));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_enable));
//                        sv.setShowcase(new ViewTarget(R.id.enable_service_button, activity), true);
//                        //sv.setText(R.string.showcase_title_enable, R.string.showcase_message_enable);
//                        break;
//                    case 3:
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_add_app));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_add_app));
//                        sv.setShowcase(new ViewTarget(R.id.add_app, activity), true);
//                        //sv.setText(R.string.showcase_title_add_app, R.string.showcase_message_add_app);
//                        break;
//                    case 4:
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_4));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_4));
//                        sv.setShowcase(ViewTarget.NONE, true);
//                        //sv.setText(R.string.showcase_title_4, R.string.showcase_message_4);
//                        break;
//
//                    case 5:
//                        activity.toggleDrawer(true);
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_5));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_5));
//                        sv.setShowcase(new ViewTarget(R.id.tutorial_spot, activity), true);
//                        //sv.setText(R.string.showcase_title_5, R.string.showcase_message_5);
//                        break;
//                    case 6:
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_more_prefs));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_more_prefs));
//                        sv.setShowcase(ViewTarget.NONE, true);
//                        //sv.setText(R.string.showcase_title_more_prefs, R.string.showcase_message_more_prefs);
//                        break;
//                    case 7:
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.showcase_title_prefs));
//                        sv.setContentText(activity.getResources().getString(R.string.showcase_message_prefs));
//                        sv.setShowcase(new ViewTarget(R.id.preferences, activity), true);
//                        //sv.setText(R.string.showcase_title_prefs, R.string.showcase_message_prefs);
//                        break;
//                    case 8:
//                        activity.toggleDrawer(false);
//                        //sv.setShowcase(new ViewTarget(android.R.id.home, activity), true);
//
//                        sv.setContentTitle(activity.getResources().getString(R.string.tutorial4_title));
//                        sv.setContentText(activity.getResources().getString(R.string.tutorial_thanks));
//                        sv.setShowcase(ViewTarget.NONE, true);
////                        sv.setShowcase(ShowcaseView.NONE);
////                        sv.setShowcase(new Target() {
////                                           @Override
////                                           public Point getPoint() {
////                                               Point mPoint = new Point();
////                                               mPoint.set(110,80);
////                                               return mPoint;
////                                           }
////                                       },
////                                true);
//                        sv.setButtonText("OK");
//                        //sv.setText(R.string.showcase_title_6, R.string.showcase_message_6);
//                        break;
//                    default:
//                        sv.hide();
//                        break;
//                }
//                svCounter++;
//            }
//        });
    }
}
