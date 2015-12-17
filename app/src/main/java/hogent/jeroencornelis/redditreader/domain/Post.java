package hogent.jeroencornelis.redditreader.domain;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class Post {
    private String title;
    private String selfText;
    private String author;

    public Post() {
    }

    public Post(String title, String selfText, String author) {
        this.title = title;
        this.selfText = selfText;
        this.author = author;
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
}
