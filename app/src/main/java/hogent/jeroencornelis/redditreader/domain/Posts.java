package hogent.jeroencornelis.redditreader.domain;

import java.util.ArrayList;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class Posts {

    private ArrayList<Post> posts;

    public Posts() {

        this.posts = new ArrayList<>();
    }

    public Posts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post){
        posts.add(post);
    }
    public void removePost(Post post){
        posts.remove(post);
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
