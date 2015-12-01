package hogent.jeroencornelis.redditreader.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import hogent.jeroencornelis.redditreader.domain.Posts;

/**
 * Created by Jeroen-Lenovo on 1/12/2015.
 */
public class RedditPostsDeserializer implements JsonDeserializer<Posts> {
    @Override
    public Posts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
