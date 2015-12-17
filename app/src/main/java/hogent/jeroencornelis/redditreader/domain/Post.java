package hogent.jeroencornelis.redditreader.domain;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class Post {
    private String title;
    private String selfText;
    private String author;
    private int score;
    private int comments;

    public Post() {
    }

    public Post(String title, String selfText, String author) {
        this.title = title;
        this.selfText = selfText;
        this.author = author;
    }

    public Post(String title, String author, int score, int comments) {
        this.title = title;
        this.author = author;
        this.score = score;
        this.comments = comments;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
