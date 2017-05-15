package ng.volymat.popularmovies.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ng.volymat.popularmovies.sync.FavoritesService;

/**
 * Created by Nsikak  Thompson on 5/14/2017.
 */
@Module
public class ServiceModule {
    @Provides
    @Singleton
    public FavoritesService providesFavoritesService(Application application) {
        return new FavoritesService(application.getApplicationContext());
    }
}
