package hogent.jeroencornelis.redditreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import hogent.jeroencornelis.redditreader.domain.Post;
import hogent.jeroencornelis.redditreader.network.RequestController;

//TODO: CLEANUP COMMENTS
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.linearLayout)
        LinearLayout linearLayout;
        @Bind(R.id.relativeLayout)
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

            //Add listeners
            relativeLayout.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v instanceof ImageView){
                mListener.onThumbnail((ImageView) v,position);
            } else {
                mListener.onText(v,position);
            }
        }

        public static interface IMyViewHolderClicks {
            public void onText(View caller,int pos);
            public void onThumbnail(ImageView callerImage,int pos);
        }
    }
    private Post post;

    // Store a member variable for the posts
    private List<Post> mPosts;

    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView,new PostAdapter.ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onText(View caller,int pos) {
                Log.d("listener", "Clicked on a textField");
                Intent intent = new Intent(context,SubredditPostActivity.class);
                intent.putExtra("post",mPosts.get(pos));
                context.startActivity(intent);
            }

            @Override
            public void onThumbnail(ImageView callerImage, int pos) {
                Log.d("listener","Clicked on a ImageView");
            }
        });
        return viewHolder;
    }
    //End View Holder

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        post = mPosts.get(position);

        // Set item views based on the data model
        TextView scoreTextView = viewHolder.scoreTextView;
        scoreTextView.setText(Integer.toString(post.getScore()));

        TextView authorTextView = viewHolder.authorTextView;
        authorTextView.setText(post.getAuthor());

        TextView titleTextView = viewHolder.titleTextView;
        titleTextView.setText(post.getTitle());

        TextView commentTextView = viewHolder.commentTextView;
        commentTextView.setText(
              String.format(Locale.ENGLISH,"%d %s", post.getComments(),"comments")
        );
        ImageView imageView = viewHolder.imageView;

        //Log.d("Image ulr","|"+post.getThumbnail()+"|");

        switch (post.getThumbnail())
        {
            case "":
            case "default":
            case "self":
                viewHolder.linearLayout.removeView(imageView);
                break;
            case "nsfw":
                imageView.setImageResource(R.drawable.nsfw_icon);
                break;
            default:
                imageRequest(post.getThumbnail(),imageView);
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
    public void imageRequest(String url, final ImageView imageView)
    {
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.no_image);
                    }
                });
        RequestController.getInstance().addToRequestQueue(request);
    }
}
