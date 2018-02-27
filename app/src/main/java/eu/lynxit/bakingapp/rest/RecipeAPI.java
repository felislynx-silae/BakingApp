package eu.lynxit.bakingapp.rest;

import java.util.List;

import eu.lynxit.bakingapp.model.Recipe;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by lynx on 15/02/18.
 */

public interface RecipeAPI {
    @GET
    public Observable<List<Recipe>> getRecipes(@Url String url);
}
