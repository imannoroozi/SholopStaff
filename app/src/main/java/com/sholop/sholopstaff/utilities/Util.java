package com.sholop.sholopstaff.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.sholop.sholopstaff.R;


/**
 * Created by Pooyan on 8/20/2016.
 */
public class Util {

    private static Util util;

    private Util(){}

    public static Util getInstance(){
        if(util==null) util = new Util();

        return util;
    }

    public boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public void rotateAnimate(View v, boolean animate, Context ctx){
        if( animate ){
            Animation rotation = AnimationUtils.loadAnimation(ctx, R.anim.rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            v.startAnimation(rotation);
        }
        else{
            v.clearAnimation();
        }
    }

    public void shakeAnimate(View v, boolean animate, Context ctx){
        if( animate ){
            Animation shake = AnimationUtils.loadAnimation(ctx, R.anim.shake);
            v.startAnimation(shake);
        }
        else{
            v.clearAnimation();
        }
    }

    public void animateGhost( ImageView ghost){
        Animation mAnimation;

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(5000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        ghost.setVisibility(View.VISIBLE);
        mAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.8f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f);
        mAnimation.setDuration(20000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());

        animation.addAnimation(mAnimation);
        ghost.setAnimation(animation);
    }

    public void animateOwl( ImageView owl){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(5000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        owl.setAnimation(animation);
        owl.setVisibility(View.VISIBLE);
    }

    public Typeface getFontRegular(Context ctx){
        return Typeface.createFromAsset( ctx.getAssets(), "Fonts/raleway.light.ttf");
    }
    public Typeface getFontBold(Context ctx){
        return Typeface.createFromAsset( ctx.getAssets(), "Fonts/raleway.medium.ttf");
    }
    public Typeface getFontAwesome(Context ctx){
        return Typeface.createFromAsset( ctx.getAssets(), "Fonts/fontawesome-webfont.ttf");
    }
}
