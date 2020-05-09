package com.example.nischay.musicplayerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nischay on 5/7/2020.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{

    private ArrayList<SongData> _songs = new ArrayList<SongData>();
    private Context context;
    private OnItemClickListener monItemClickListener;

    // Basically becomes a list view adapter
    // which is inefficient, but who cares!!!!!
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public SongAdapter(ArrayList<SongData> _songs, Context context) {
        this._songs = _songs;
        this.context = context;
    }


    public interface OnItemClickListener{
        void onItemClick(ImageView b ,SeekBar s,TextView t,CardView c,View view, SongData obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.act_main_row,parent,false);
        return new SongHolder(myView);
    }
    @Override
    public void onBindViewHolder(final SongHolder holder, int position) {
        final SongData s = _songs.get(position);

        holder.tvSongName.setText(s.getSongname());
        holder.tvSongArtist.setText(s.getArtistname());
        holder.extra.setText(s.getExtra());
        holder.imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In order to access this listener from main thread
                // i am passing it the interface which is linked to on
                // item click listener of recycler view
                // Defined as the function above by name of setOnItemClickListener


                if(monItemClickListener!=null)
                    monItemClickListener.onItemClick(holder.imView,holder.seekBar,holder.extra,holder.card_view, v, s, holder.getAdapterPosition());

            }
        });
    }


    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{
        TextView tvSongName,tvSongArtist;
        SeekBar seekBar;
        TextView extra;
        ImageView imView;
        CardView card_view;

        public SongHolder(View myView) {
            super(myView);
            tvSongName = (TextView) itemView.findViewById(R.id.song_name);
            tvSongArtist = (TextView) itemView.findViewById(R.id.artist_name);
            imView = (ImageView) itemView.findViewById(R.id.img_play);
            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
            extra = (TextView) itemView.findViewById(R.id.extra);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            card_view.setElevation(0);
        }
    }
}