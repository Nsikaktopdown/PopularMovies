package ng.volymat.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Nsikak  Thompson on 5/10/2017.
 */

public class MovieSynIntentUtil {

    public static void startImmediateSync(@NonNull final Context context) {
//      COMPLETED (11) Within that method, start the SunshineSyncIntentService
        Intent intentToSyncImmediately = new Intent(context, MovieSynIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}