package ng.volymat.popularmovies.app;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import ng.volymat.popularmovies.MainActivity;
import ng.volymat.popularmovies.Movie_Details;

/**
 * Created by Nsikak  Thompson on 5/14/2017.
 */

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface NetworkComponent {





    void inject(Movie_Details movie_details);



}