package hogent.jeroencornelis.redditreader.domain;

import java.util.UUID;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class Post {
    private String id;
    private String title;
    private String selfText;
    private String author;
    private String thumbnail;
    private Integer score;
    private Integer comments;

    public Post() {

    }

    public Post(String id, String title, String selfText, String author) {
        this.id = id;
        this.title = title;
        this.selfText = selfText;
        this.author = author;
    }

    public Post(String id, String title, String selfText, String author, String thumbnail, int score, int comments) {
        this(id, title, selfText, author);
        this.thumbnail = thumbnail;
        this.score = score;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelfText() {
        return selfText;
    }

    public void setSelfText(String selfText) {
        this.selfText = selfText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }
}