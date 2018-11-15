package com.example.sikander.firebasetutorial.Models;

import java.util.HashMap;
import java.util.Map;

public class MovieReview {
    public String uid;
    public Long movieid;
    public String reviewheadline;
    public String reviewtext;
    public MovieReview() {

    }
    public MovieReview(String uid, Long movieid, String reviewheadline, String reviewtext) {
        this.uid = uid;
        this.movieid = movieid;
        this.reviewheadline = reviewheadline;
        this.reviewtext = reviewtext;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("movieid", movieid);
        result.put("reviewheadline", reviewheadline);
        result.put("reviewtext", reviewtext);
        return result;
    }
}
