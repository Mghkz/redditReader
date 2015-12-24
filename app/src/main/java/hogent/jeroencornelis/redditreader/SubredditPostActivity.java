package hogent.jeroencornelis.redditreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hogent.jeroencornelis.redditreader.domain.Post;

public class SubredditPostActivity extends Activity {
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.score)
    TextView tvScore;
    @Bind(R.id.author)
    TextView tvAuthor;
    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.comments)
    TextView tvComment;
    @Bind(R.id.thumbnail)
    ImageView imageView;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit_post);
        Intent intent = getIntent();
        post = (Post) intent.getParcelableExtra("post");
        ButterKnife.bind(this);


        tvAuthor.setText(post.getAuthor());
        tvScore.setText(post.getScore().toString());
        tvComment.setText(post.getComments().toString());

        tvTitle.setText(post.getTitle());
    }
}
