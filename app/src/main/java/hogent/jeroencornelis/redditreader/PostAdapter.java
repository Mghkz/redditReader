package hogent.jeroencornelis.redditreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hogent.jeroencornelis.redditreader.domain.Post;

/**
 * Created by Jeroen Cornelis on 17/12/2015.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView imageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
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
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Post post = mPosts.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(post.getTitle());

        ImageView imageView = viewHolder.imageView;

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
