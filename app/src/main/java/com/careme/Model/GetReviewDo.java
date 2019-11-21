package com.careme.Model;


import java.io.Serializable;

public class GetReviewDo implements Serializable {

    private String postId = "";
    private String name = "";
    private String email = "";
    private String review = "";
    private float rating;

    public GetReviewDo(String postId, String name, String email, String review, float rating) {
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.review = review;
        this.rating = rating;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
