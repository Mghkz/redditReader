package hogent.jeroencornelis.redditreader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import hogent.jeroencornelis.redditreader.domain.Posts;
import hogent.jeroencornelis.redditreader.network.RedditPostsDeserializer;
import hogent.jeroencornelis.redditreader.network.RequestController;


public class SubRedditListFragment extends Fragment {

    @Bind(R.id.rvPosts)
    RecyclerView rvPosts;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private String rNaam;
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
            doJsonRequest(rNaam);
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
                doJsonRequest(rNaam);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    public void doJsonRequest(final String rNaam)
    {
        String url = "https://www.reddit.com/r/" + rNaam + "/hot.json";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            /* GSON */
                            //TODO: MOVE GSONBUILDER
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(Posts.class, new RedditPostsDeserializer());
                            Gson gson = gsonBuilder.create();
                            Posts posts = gson.fromJson(response.toString(), Posts.class);
                            PostAdapter adapter = new PostAdapter(posts.getPosts());
                            rvPosts.setAdapter(adapter);
                            rvPosts.setLayoutManager(new LinearLayoutManager(context));

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
