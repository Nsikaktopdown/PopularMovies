package ng.volymat.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import ng.volymat.popularmovies.R;

/**
 * Created by Nsikak  Thompson on 5/10/2017.
 */

public class MoviePreferences {


    public static String sortOrder(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortOrder = sharedPreferences.getString(context.getString(R.string.pref_sort_key), context.getResources().getString(R.string.pref_sort_popular_value));
        return sortOrder;
    }


}
