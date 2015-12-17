package hogent.jeroencornelis.redditreader.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import hogent.jeroencornelis.redditreader.domain.Post;
import hogent.jeroencornelis.redditreader.domain.Posts;

public class RedditPostsDeserializer implements JsonDeserializer<Posts> {
    @Override
    public Posts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        JsonObject jsonObject = (JsonObject) json;

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
        return posts;
    }
}
