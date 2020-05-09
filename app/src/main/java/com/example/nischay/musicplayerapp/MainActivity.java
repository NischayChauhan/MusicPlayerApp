package com.example.nischay.musicplayerapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "";
    RecyclerView recyclerView;
    private ArrayList<SongData> _songs = new ArrayList<SongData>();;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();

    // Refressncesw for toggle
    SeekBar seekBar=null;
    ImageView action=null;
    CardView cardView=null;
    CardView cardView_h=null;
    int cardView_h_l = 0;
    int idx=-1;

    final int duration = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        songAdapter = new SongAdapter(_songs,this);
        recyclerView.setAdapter(songAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),layout.getOrientation()));

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageView im, SeekBar s,TextView t,CardView c, View view, final SongData obj, int position) {
                change_play_img(im);
                change_seekbar(s);
                change_elevation(c);
                change_height(c);

                if(seekBar==null){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }else{
                    if(mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(obj.getSongUrl());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                        seekBar.setProgress(0);
                                        seekBar.setMax(mediaPlayer.getDuration());
                                        //Log.d("Prog", "run: " + mediaPlayer.getDuration());
                                    }
                                });
                            }catch (Exception e){}
                        }

                    };
                    myHandler.postDelayed(runnable,5);
                }
            }
        });

        checkUserpermission();
        Thread t = new runThread();
        t.start();
    }



    private class runThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (seekBar!=null && mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer!=null)
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    });
                }
            }
        }
    }

    private void change_height(final CardView c){
        if(cardView_h==null){
            change_height_open(c);
            //cardView_h = c;
            //carddview_h_l = c.height
        }else{
            if(cardView_h==c){
                change_height_close(c);
                //cardView_h = null;
                //cardView_h_l = 0;
            }else{
                change_height_close_open(cardView_h,c);
                //cardView_h = c;
                //cardView_h_l = c.height
            }
        }

    }

    private void change_height_open(final CardView c){
        // 140dp
        final int init = c.getMeasuredHeight();
        final float factor = getApplicationContext().getResources().getDisplayMetrics().density;

        ValueAnimator v = ValueAnimator.ofFloat(c.getMeasuredHeight(),140*factor);
        v.setDuration(duration);
        final int[] count = {40};
//        Log.e("TAG-->",init/factor+"");
        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) (init + (count[0]*factor));
                count[0]++;
//                Log.e("TAG--->",value/factor+"");
                ViewGroup.LayoutParams param = c.getLayoutParams();
                param.height = value;
                c.setLayoutParams(param);
            }
        });
        v.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cardView_h = c;
                cardView_h_l = init;
            }
        });
        v.start();
    }

    private void change_height_close(final CardView c2){
        // 140dp
        final int init = c2.getMeasuredHeight();
        final float factor = getApplicationContext().getResources().getDisplayMetrics().density;

        ValueAnimator v = ValueAnimator.ofFloat(c2.getMeasuredHeight(),cardView_h_l);
        v.setDuration(duration);
        final int[] count = {40};
//        Log.e("TAG-->",init/factor+"");
        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) (init - (count[0]*factor));
                count[0]++;
//                Log.e("TAG--->",value/factor+"");
                ViewGroup.LayoutParams param = c2.getLayoutParams();
                param.height = value;
                c2.setLayoutParams(param);
            }
        });
        v.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cardView_h = null;
                cardView_h_l = 0;
            }
        });
        v.start();
    }
    private void change_height_close_open(final CardView c2, final CardView c){
        // 140dp
        final int init2 = c2.getMeasuredHeight();
        final float factor2 = getApplicationContext().getResources().getDisplayMetrics().density;

        ValueAnimator v1 = ValueAnimator.ofFloat(c2.getMeasuredHeight(),cardView_h_l);
        v1.setDuration(duration);
        final int[] count2 = {40};
//        Log.e("TAG-->",init/factor+"");
        v1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) (init2 - (count2[0]*factor2));
                count2[0]++;
//                Log.e("TAG--->",value/factor+"");
                ViewGroup.LayoutParams param = c2.getLayoutParams();
                param.height = value;
                c2.setLayoutParams(param);
            }
        });


        // Open Animation
// 140dp
        final int init = c.getMeasuredHeight();
        final float factor = getApplicationContext().getResources().getDisplayMetrics().density;

        ValueAnimator v = ValueAnimator.ofFloat(c.getMeasuredHeight(),140*factor);
        v.setDuration(duration);
        final int[] count = {40};
//        Log.e("TAG-->",init/factor+"");
        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) (init + (count[0]*factor));
                count[0]++;
//                Log.e("TAG--->",value/factor+"");
                ViewGroup.LayoutParams param = c.getLayoutParams();
                param.height = value;
                c.setLayoutParams(param);
            }
        });
        v.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cardView_h = c;
                cardView_h_l = init;
            }
        });

        AnimatorSet as = new AnimatorSet();

        as.play(v1).before(v);
        as.start();
    }





    private void change_elevation(CardView c) {
        if(cardView==null){
            c.setElevation(10);
            cardView = c;
        }else{
            if(cardView==c){
                c.setElevation(0);
                cardView = null;
            }else{
                cardView.setElevation(0);
                c.setElevation(10);
                cardView = c;
            }
        }
    }
    private void change_seekbar(SeekBar s) {
        if(seekBar==null){
            s.setVisibility(View.VISIBLE);
            seekBar = s;
        }else{
            if(seekBar==s){
                s.setVisibility(View.GONE);
                seekBar = null;
            }else{
                seekBar.setVisibility(View.GONE);
                s.setVisibility(View.VISIBLE);
                seekBar = s;
            }
        }
    }



    private void change_play_img(final ImageView im) {
        if(action==null){
            // called first time
            // call open animation on im
            // store in action
            change_play_img_open(im);
            // action = im
        }else{
            if(action==im){
                // called task on same object
                change_play_img_close(im);
                // action = null;
            }else{
                // close previously opened
                // open im close action
                change_play_img_closeopen(action,im);
                // action = im;
            }
        }
    }
    private void change_play_img_open(final ImageView im){
        ObjectAnimator imView_a = ObjectAnimator.ofFloat(im,"alpha",0);
        imView_a.setDuration(duration);
        imView_a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                im.setBackgroundResource(R.drawable.pause);
            }
        });

        ObjectAnimator imView_a_1 = ObjectAnimator.ofFloat(im,"alpha",1);
        imView_a_1.setDuration(duration);
        imView_a_1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                action = im;
            }
        });
        AnimatorSet as = new AnimatorSet();
        as.play(imView_a).before(imView_a_1);
        as.start();
    }
    private void change_play_img_close(final ImageView im){
        ObjectAnimator imView_a = ObjectAnimator.ofFloat(im,"alpha",0);
        imView_a.setDuration(duration);
        imView_a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                im.setBackgroundResource(R.drawable.play);
            }
        });

        ObjectAnimator imView_a_1 = ObjectAnimator.ofFloat(im,"alpha",1);
        imView_a_1.setDuration(duration);
        imView_a_1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                action = null;
            }
        });
        AnimatorSet as = new AnimatorSet();
        as.play(imView_a).before(imView_a_1);
        as.start();
    }
    private void change_play_img_closeopen(final ImageView c,final ImageView o){
        ObjectAnimator imView_a_1 = ObjectAnimator.ofFloat(c,"alpha",0);
        imView_a_1.setDuration(duration);
        imView_a_1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                c.setBackgroundResource(R.drawable.play);
            }
        });

        ObjectAnimator imView_a_1_1 = ObjectAnimator.ofFloat(c,"alpha",1);
        imView_a_1_1.setDuration(duration);
        AnimatorSet as_1 = new AnimatorSet();
        as_1.play(imView_a_1).before(imView_a_1_1);

        // open Anim
        ObjectAnimator imView_a = ObjectAnimator.ofFloat(o,"alpha",0);
        imView_a.setDuration(duration);
        imView_a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                o.setBackgroundResource(R.drawable.pause);
            }
        });

        ObjectAnimator imView_a_2 = ObjectAnimator.ofFloat(o,"alpha",1);
        imView_a_2.setDuration(duration);
        imView_a_2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                action=o;
            }
        });
        AnimatorSet as = new AnimatorSet();
        as.play(imView_a).before(imView_a_2);


        AnimatorSet tot = new AnimatorSet();
        tot.play(as_1).before(as);
        tot.start();
    }


    private void checkUserpermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }
        loadSongs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserpermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadSongs() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cur = getApplicationContext().getContentResolver().query(uri,null,
                    MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

            if (cur == null) {
                // Query failed...
//                Log.e("TAG", "Failed to retrieve music: cursor is null :-(");
                return;
            }
            if (!cur.moveToFirst()) {
                // Nothing to query. There is no music on the device. How boring.
//                Log.e("TAG", "Failed to move cursor to first row (no query results).");
                return;
            }
            Log.e("this", "loadSongs: " );
            do{
                String _name = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String got = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                got = got.replace(" ","").toString();
                String _uri = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));


                int idx = 0;
                String _art = "";
                String _extra="";

                while(idx<got.length() && got.charAt(idx)!='|'){
                    _art+=got.charAt(idx);
                    idx++;
                }
                idx++;

                while(idx<got.length() && got.charAt(idx)!='|'){
                    _extra+=got.charAt(idx);
                    idx++;
                }
                idx++;


//                Log.e("TAG Data",got);
//
//                Log.e("TAG Name",_name);
//                Log.e("TAG Artist","A:"+_art);
//                Log.e("TAG Extra",_extra);
//
//                Log.e("____","__----------------------->");

                _songs.add(new SongData(_name,_art,_uri,_extra));
                songAdapter.notifyItemChanged(_songs.size());
            }while (cur.moveToNext());
            cur.close();
        }else{
            Log.e("TAG:: -------------->  ","BHAAK");
        }
    }
}
