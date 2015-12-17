package hogent.jeroencornelis.redditreader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import hogent.jeroencornelis.redditreader.domain.Post;
import hogent.jeroencornelis.redditreader.domain.Posts;
import hogent.jeroencornelis.redditreader.network.RedditPostsDeserializer;
import hogent.jeroencornelis.redditreader.network.RequestController;
import hogent.jeroencornelis.redditreader.network.ResponsePosts;


public class SubRedditListFragment extends Fragment {


    private TextView content;
    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_reddit_list, container, false);
        content = (TextView) view.findViewById(R.id.contentFld);
        Bundle args = this.getArguments();
        if(content != null)
            doJsonRequest(args.getString("rNaam"));
            //content.setText(args.getString("rNaam"));
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
    public void doJsonRequest(String rNaam)
    {
        String url = "https://www.reddit.com/r/" + rNaam + "/new.json";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            //content.setText(response.toString());
                            /* GSON */
                            /*
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(Posts.class, new RedditPostsDeserializer());
                            Gson gson = gsonBuilder.create();
                            ResponsePosts responsePosts = gson.fromJson(response.toString(), ResponsePosts.class);
                            content.setText(responsePosts.getResponse().getPosts().get(0).getTitle());
                            */
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());

                            JsonObject data = (JsonObject) jsonObject.get("data");
                            JsonArray children = data.getAsJsonArray("children");
                            Posts posts = new Posts();

                            for(JsonElement obj : children)
                            {
                                JsonObject jObj = (JsonObject) obj;
                                JsonObject jObjData = (JsonObject) jObj.get("data");
                                Post p = new Post(
                                        jObjData.get("title").getAsString(),
                                        jObjData.get("selftext").getAsString(),
                                        jObjData.get("author").getAsString()
                                );
                                posts.addPost(p);

                            }
                            content.setText(posts.getPosts().get(0).getTitle());
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
}
