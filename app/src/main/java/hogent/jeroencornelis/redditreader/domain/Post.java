package hogent.jeroencornelis.redditreader.domain;

import java.util.UUID;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class Post {
    private Long id;
    private String postId;
    private String title;
    private String author;
    private String thumbnail;
    private Integer score;
    private Integer comments;

    public Post() {

    }
    public Post(Long id) {
        this.id = id;
    }

    public Post(Long id,String postId, String title, String author) {
        this.id = id;
        this.postId = postId;
        this.title = title;
        this.author = author;
    }

    public Post(Long id,String postId, String title, String author, String thumbnail, Integer score, Integer comments) {
        this(id,postId, title, author);
        this.thumbnail = thumbnail;
        this.score = score;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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