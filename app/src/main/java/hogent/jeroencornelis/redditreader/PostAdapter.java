package hogent.jeroencornelis.redditreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hogent.jeroencornelis.redditreader.domain.Post;
//TODO: CLEANUP COMMENTS
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.layout)
        RelativeLayout relativeLayout;
        @Bind(R.id.score)
        TextView scoreTextView;
        @Bind(R.id.author)
        TextView authorTextView;
        @Bind(R.id.title)
        TextView titleTextView;
        @Bind(R.id.comments)
        TextView commentTextView;
        @Bind(R.id.thumbnail)
        ImageView imageView;

        public IMyViewHolderClicks mListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);

            mListener = listener;

            //authorTextView = (TextView) itemView.findViewById(R.id.contact_name);
            //imageView = (ImageView) itemView.findViewById(R.id.thumbnail);

            //Add listeners
            relativeLayout.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView){
                mListener.onThumbnail((ImageView) v);
            } else {
                mListener.onText(v);
            }
        }

        public static interface IMyViewHolderClicks {
            public void onText(View caller);
            public void onThumbnail(ImageView callerImage);
        }
    }
    // Store a member variable for the contacts
    private List<Post> mPosts;

    // Pass in the contact array into the constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView,new PostAdapter.ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onText(View caller) {
                Log.d("listener","Clicked on a textField");
            }

            @Override
            public void onThumbnail(ImageView callerImage) {
                Log.d("listener","Clicked on a ImageView");
            }
        });
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Post post = mPosts.get(position);

        // Set item views based on the data model
        TextView scoreTextView = viewHolder.scoreTextView;
        scoreTextView.setText(Integer.toString(post.getScore()));

        TextView authorTextView = viewHolder.authorTextView;
        authorTextView.setText(post.getAuthor());

        TextView titleTextView = viewHolder.titleTextView;
        titleTextView.setText(post.getTitle());

        TextView commentTextView = viewHolder.commentTextView;
        commentTextView.setText(
              String.format("%d %s", post.getComments(),"comments")
        );

        ImageView imageView = viewHolder.imageView;

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
