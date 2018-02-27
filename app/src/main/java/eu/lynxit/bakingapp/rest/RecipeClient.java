package eu.lynxit.bakingapp.rest;

import java.util.List;

import eu.lynxit.bakingapp.model.Recipe;
import io.reactivex.Observable;

/**
 * Created by lynx on 15/02/18.
 */

public class RecipeClient {
    private static RecipeClient mInstance;
    private RecipeAPI mClient;

    private RecipeClient() {
        mClient = RestClient.getRetrofit().create(RecipeAPI.class);
    }

    public static RecipeClient getRecipeClient() {
        if (mInstance == null) {
            mInstance = new RecipeClient();
        }
        return mInstance;
    }

    public Observable<List<Recipe>> getRecipes(String url) {
        return mClient.getRecipes(url);
    }
}
