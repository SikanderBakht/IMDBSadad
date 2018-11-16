package com.example.sikander.firebasetutorial;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable {
    public static final String SITE_YOUTUBE = "YouTube";
    private String id;
    private String name;
    private String site;
    private String key;
    private String videoId;
    private int size;
    private String type;
    public MovieTrailer() {
    }
    protected MovieTrailer(Parcel in) {
        id = in.readString();
        name = in.readString();
        site = in.readString();
        key = in.readString();
        videoId = in.readString();
        size = in.readInt();
        type = in.readString();
    }
    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
    public static String getUrl(MovieTrailer movieTrailer) {
        if (SITE_YOUTUBE.equalsIgnoreCase(movieTrailer.getSite())) {
            return String.format(Api.YOUTUBE_VIDEO_URL, movieTrailer.getKey());
        } else {
            return "";
        }
    }
    public static String getThumbnailUrl(MovieTrailer trailer) {
        if (SITE_YOUTUBE.equalsIgnoreCase(trailer.getSite())) {
            return String.format(Api.YOUTUBE_THUMBNAIL_URL, trailer.getKey());
        } else {
            return "";
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(key);
        dest.writeString(videoId);
        dest.writeInt(size);
        dest.writeString(type);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
