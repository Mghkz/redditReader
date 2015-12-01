package hogent.jeroencornelis.redditreader.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class Posts {

    private List<Post> posts;

    public Posts() {
        this.posts = new LinkedList<>();
    }

    public Posts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post){
        posts.add(post);
    }
    public void removePost(Post post){
        posts.remove(post);
    }
}
