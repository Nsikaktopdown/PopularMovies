package ng.volymat.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Nsikak  Thompson on 5/10/2017.
 */

public class MovieSynIntentService extends IntentService {


    public MovieSynIntentService(){
        super("MovieSynIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieSyncTask.synMovie(this);

    }
}
