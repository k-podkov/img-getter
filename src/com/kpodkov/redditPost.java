package com.kpodkov;

/**
 * Created by Kirill on 11/30/2015.
 */
public class redditPost {

    private String Score;
    private String Link;
    private String CommentLink;
    private String Title;

    public redditPost(String score, String link, String title, String commentLink) {
        Score = score;
        Link = link;
        Title = title;
        CommentLink = commentLink;
    }
    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getCommentLink() {
        return CommentLink;
    }

    public void setCommentLink(String commentLink) {
        CommentLink = commentLink;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
