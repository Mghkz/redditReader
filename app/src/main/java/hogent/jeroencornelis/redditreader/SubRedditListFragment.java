package hogent.jeroencornelis.redditreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hogent.jeroencornelis.redditreader.domain.Post;
import hogent.jeroencornelis.redditreader.domain.Posts;
import hogent.jeroencornelis.redditreader.domain.Subreddit;
import hogent.jeroencornelis.redditreader.network.RedditPostsDeserializer;
import hogent.jeroencornelis.redditreader.network.RequestController;
import hogent.jeroencornelis.redditreader.persistency.DaoMaster;
import hogent.jeroencornelis.redditreader.persistency.DaoSession;
import hogent.jeroencornelis.redditreader.persistency.PostDao;
import hogent.jeroencornelis.redditreader.persistency.SubredditDao;


public class SubRedditListFragment extends Fragment {

    @Bind(R.id.rvPosts)
    RecyclerView rvPosts;

    private OnFragmentInteractionListener mListener;
    //Subreddit name
    private String rNaam;
    //Attribute for loading posts after a certain post
    private String sAfter = "";
    //GET Url for Reddit API
    String url = "";
    //Posts
    Posts posts;
    //Subreddit
    Subreddit subreddit;

    private Context context;
    private LinearLayoutManager mLayoutManager;

    //Gson parser for json data
    private Gson gson;

    private StringBuilder stringBuilder;

    //variables needed for endless scrolling
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    //Database variables
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private PostDao postDao;
    private SubredditDao subredditDao;



    public SubRedditListFragment() {
        // Required empty public constructor
    }
    public static SubRedditListFragment newInstance(String param1, String param2) {
        SubRedditListFragment fragment = new SubRedditListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Making the gson parser
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Posts.class, new RedditPostsDeserializer());
        gson = gsonBuilder.create();

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_reddit_list, container, false);
        ButterKnife.bind(this, view);
        this.context = container.getContext();
        Bundle args = this.getArguments();
        if(args != null) {
            rNaam = args.getString("rNaam");
            this.subreddit = new Subreddit(null,rNaam,"");
            //doJsonRequest(rNaam,false);
        }

        //Database
        //TODO: Make this singleton
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "post-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        postDao = daoSession.getPostDao();
        subredditDao = daoSession.getSubredditDao();

        //Clear database
        //postDao.deleteAll();
        //subredditDao.deleteAll();

        //Init Recyclerview
        mLayoutManager = new LinearLayoutManager(context);
        rvPosts.setLayoutManager(mLayoutManager);

        rvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        Log.d("Recyclerview!", "End of List Reached");
                        doJsonRequest(rNaam, true);
                        loading = true;
                    }
                }
            }
        });

        //Look if the database contains this subreddit
        boolean foundSubredditInDatabase = false;
        List<Subreddit> subreddits = subredditDao.loadAll();
        for(Subreddit s : subreddits)
        {
            if(s.getName().toLowerCase().equals(rNaam.toLowerCase()))
            {
                foundSubredditInDatabase = true;
                subreddit = s;
            }
        }
        if(!foundSubredditInDatabase)
            subredditDao.insert(subreddit);

        //If the subreddit contains posts load these instead of json
        List<Post> postsFromDb = subreddit.getPosts();
        if(postsFromDb.isEmpty())
            doJsonRequest(rNaam,false);
        else
        {
            posts = new Posts();
            posts.setPosts(new ArrayList<>(postsFromDb));
            loadPostInView();
        }



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //region Action Bar
    //Loading the action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_subreddit,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    //Handling on click of an item in the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refreshBtn:
                doJsonRequest(rNaam,false);
                // FIX FOR ENDLESS SCROLL AFTER REFRESH BUTTON PRESS
                previousTotal = 0;
                return true;
            case R.id.clearBtn:
                //Clear database
                postDao.deleteAll();
                //subredditDao.deleteAll();
                previousTotal = 0;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    public void doJsonRequest(final String rNaam, final boolean after)
    {
        stringBuilder = new StringBuilder("");
        stringBuilder.append("https://www.reddit.com/r/");
        stringBuilder.append(rNaam);
        stringBuilder.append("/hot.json");

        if(after) {
            stringBuilder.append("?after=");
            stringBuilder.append(sAfter);

        }
        url = stringBuilder.toString();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            /* GSON */
                            Posts postsFromJson = gson.fromJson(response.toString(), Posts.class);

                            //Decides if posts should be renewed or added to the already existing posts
                            if(!after) {
                                posts = postsFromJson;
                                //Delete previously stored posts and replace them with new ones
                                subreddit.getPosts().clear();
                                //subreddit.setPosts(posts.getPosts());
                                storeNotesInDatabase(posts.getPosts());
                                subredditDao.update(subreddit);
                            }
                            else {
                                posts.getPosts().addAll(postsFromJson.getPosts());
                                //Add extra posts
                                storeNotesInDatabase(postsFromJson.getPosts());
                            }

                            subredditDao.refresh(subreddit);
                            sAfter = postsFromJson.getAfter();
                            subreddit.setAfter(sAfter);
                            subredditDao.update(subreddit);

                            loadPostInView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        RequestController.getInstance().addToRequestQueue(jsObjRequest);
    }

    public void loadPostInView(){
        //Fill recyclerview with posts
        PostAdapter adapter = new PostAdapter(posts.getPosts());
        rvPosts.setAdapter(adapter);
        //Refresh
        adapter.notifyDataSetChanged();
        //Set position to start of new posts
        mLayoutManager.scrollToPosition(posts.getPosts().size()-25);
    }

    public void storeNotesInDatabase(ArrayList<Post> posts)
    {
        for (Post p : posts) {
            p.setSubreddit(subreddit);
            postDao.insert(p);
            Log.d("DaoExample", "Inserted new note, ID: " + p.getId());
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
