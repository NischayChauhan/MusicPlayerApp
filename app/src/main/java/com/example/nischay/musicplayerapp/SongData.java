package com.example.nischay.musicplayerapp;

/**
 * Created by Nischay on 5/7/2020.
 */

public class SongData {
    private String Songname;
    private String Artistname;
    private String SongUrl;
    private String Extra;
    public SongData(String songname, String artistname, String songUrl, String extra) {
        Songname = songname;
        Artistname = artistname;
        SongUrl = songUrl;
        Extra = extra;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getSongname() {
        return Songname;
    }

    public void setSongname(String songname) {
        Songname = songname;
    }

    public String getArtistname() {
        return Artistname;
    }

    public void setArtistname(String artistname) {
        Artistname = artistname;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }
}
