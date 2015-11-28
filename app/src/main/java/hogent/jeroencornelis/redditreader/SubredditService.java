package hogent.jeroencornelis.redditreader;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;

import android.os.IBinder;

import java.util.Random;

/**
 * Created by Jeroen-Lenovo on 27/11/2015.
 */
public class SubredditService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    RequestController controller;



    public class LocalBinder extends Binder {
        SubredditService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SubredditService.this;
        }
    }
    @Override
    public void onCreate() {
        controller = new RequestController(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getHello()
    {
        return controller.getRequest();
    }

}
