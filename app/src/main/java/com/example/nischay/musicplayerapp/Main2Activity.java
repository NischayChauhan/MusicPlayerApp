package com.example.nischay.musicplayerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    TextView text_1,text_1_1,text_2,text_3;
    ImageView text_4,background_left,background_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        text_1 = findViewById(R.id.text_1);
        text_1_1 = findViewById(R.id.text_1_1);
        text_2 = findViewById(R.id.text_2);
        text_3 = findViewById(R.id.text_3);
        text_4 = findViewById(R.id.text_4);
        background_left = findViewById(R.id.background_left);
        background_right = findViewById(R.id.background_right);

        throughAll();
        bringAll();


        findViewById(R.id.main_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseAll();
            }
        });
    }


    private void throughAll() {
        text_1.setTranslationX(-500);
        text_1.setAlpha(0);
        text_1_1.setTranslationY(-500);
        text_1_1.setAlpha(0);

        text_2.setTranslationY(100);
        text_2.setAlpha(0);

        text_3.setTranslationY(500);
        text_3.setAlpha(0);

        text_4.setAlpha(0f);
    }

    private void bringAll() {
        final long DURATION = 1000;
        ObjectAnimator text_1_a = ObjectAnimator.ofFloat(text_1,"translationX",0);
        ObjectAnimator text_1_a_1 = ObjectAnimator.ofFloat(text_1,"alpha",1);
        text_1_a.setDuration(DURATION);
        text_1_a_1.setDuration(DURATION);

        ObjectAnimator text_1_1_a = ObjectAnimator.ofFloat(text_1_1,"translationY",0);
        ObjectAnimator text_1_1_a_1 = ObjectAnimator.ofFloat(text_1_1,"alpha",1);
        text_1_1_a.setDuration(DURATION);
        text_1_1_a_1.setDuration(DURATION);

        ObjectAnimator text_2_a = ObjectAnimator.ofFloat(text_2,"translationY",0);
        ObjectAnimator text_2_a_1 = ObjectAnimator.ofFloat(text_2,"alpha",1);
        text_2_a.setDuration(DURATION);
        text_2_a_1.setDuration(DURATION);

        ObjectAnimator text_3_a = ObjectAnimator.ofFloat(text_3,"translationY",0);
        ObjectAnimator text_3_a_1 = ObjectAnimator.ofFloat(text_3,"alpha",1);
        text_3_a.setDuration(DURATION);
        text_3_a_1.setDuration(DURATION);

        ObjectAnimator text_4_a = ObjectAnimator.ofFloat(text_4,"alpha",1f);
        text_4_a.setDuration(DURATION);

        AnimatorSet as = new AnimatorSet();
        as.play(text_1_1_a).with(text_1_1_a_1)
                .with(text_1_a).with(text_1_a_1)
                .with(text_2_a).with(text_2_a_1)
                .with(text_3_a).with(text_3_a_1)
                .with(text_4_a);
        as.start();
    }

    private void reverseAll() {
        long DURATION = 500;
        ObjectAnimator text_1_a = ObjectAnimator.ofFloat(text_1,"translationX",-500);
        ObjectAnimator text_1_a_1 = ObjectAnimator.ofFloat(text_1,"alpha",0);
        text_1_a.setDuration(DURATION);
        text_1_a_1.setDuration(DURATION);

        ObjectAnimator text_1_1_a = ObjectAnimator.ofFloat(text_1_1,"translationY",-500);
        ObjectAnimator text_1_1_a_1 = ObjectAnimator.ofFloat(text_1_1,"alpha",0);
        text_1_1_a.setDuration(DURATION);
        text_1_1_a_1.setDuration(DURATION);

        ObjectAnimator text_2_a = ObjectAnimator.ofFloat(text_2,"translationY",100);
        ObjectAnimator text_2_a_1 = ObjectAnimator.ofFloat(text_2,"alpha",0);
        text_2_a.setDuration(DURATION);
        text_2_a_1.setDuration(DURATION);

        ObjectAnimator text_3_a = ObjectAnimator.ofFloat(text_3,"translationY",500);
        ObjectAnimator text_3_a_1 = ObjectAnimator.ofFloat(text_3,"alpha",0);
        text_3_a.setDuration(DURATION);
        text_3_a_1.setDuration(DURATION);

        ObjectAnimator text_4_a = ObjectAnimator.ofFloat(text_4,"alpha",0f);
        text_4_a.setDuration(DURATION);

        AnimatorSet as = new AnimatorSet();
        as.play(text_1_1_a).with(text_1_1_a_1)
                .with(text_1_a).with(text_1_a_1)
                .with(text_2_a).with(text_2_a_1)
                .with(text_3_a).with(text_3_a_1)
                .with(text_4_a);

        background_left.setPivotX(0f);
        ObjectAnimator background_left_a = ObjectAnimator.ofFloat(background_left,"rotationY",90);
        background_left_a.setDuration(300);

        background_right.setPivotX(background_right.getWidth());
        ObjectAnimator background_right_a = ObjectAnimator.ofFloat(background_right,"rotationY",-90);
        background_right_a.setDuration(300);


        background_right_a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                if(val<=-80){
                    callActivity();
                }
            }
        });

        AnimatorSet door = new AnimatorSet();
        door.play(background_left_a).with(background_right_a);

        AnimatorSet total = new AnimatorSet();
        total.play(door).after(as);
        total.start();

    }

    boolean requested = false;
    private void callActivity() {
        if(requested)
            return;
        requested = true;
        Log.e("---->>","CALLED------------------------------>>>>>>>>>>");
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.enter,R.anim.exit);
        finish();
    }


//
//    public void startActivity(){
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        // Pass data object in the bundle and populate details activity.
//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());
//        getWindow().setReenterTransition(new Explode());
//
//
//        Transition ts = new ChangeClipBounds();         //Setting Element Animation
//        ts.setDuration(5000);
//        getWindow().setSharedElementExitTransition(ts);
//
//        ActivityOptionsCompat options = ActivityOptionsCompat.
//                makeSceneTransitionAnimation(this, (View)text_1, "profile");
//        startActivity(intent, options.toBundle());
//
////        startActivity(intent, options.toBundle());
//    }
}
